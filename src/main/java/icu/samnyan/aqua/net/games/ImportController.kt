package icu.samnyan.aqua.net.games

import ext.*
import icu.samnyan.aqua.net.db.AquaNetUser
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.Fedy
import icu.samnyan.aqua.net.utils.AquaNetProps
import icu.samnyan.aqua.net.utils.SUCCESS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.writeText
import kotlin.reflect.KClass
import org.springframework.context.annotation.Lazy

data class ExportOptions(
    val playlogAfter: String? = null
)

// Import class with renaming
data class ImportClass<T : Any>(
    val type: KClass<T>,
    val renames: Map<String, String?>? = null,
    val name: String = type.simpleName!!.removePrefix("Mai2").removePrefix("Chu3").lowercase()
)

interface IUserEntity<UserModel: IUserData> {
    var id: Long
    var user: UserModel
}

interface IExportClass<UserModel: IUserData> {
    var gameId: String
    var userData: UserModel
}

@NoRepositoryBean
interface IUserRepo<UserModel, ThisModel>: JpaRepository<ThisModel, Long> {
    fun findByUser(user: UserModel): List<ThisModel>
    fun findSingleByUser(user: UserModel): Optional<ThisModel>
}

/**
 * Import controller for a game
 *
 * @param game: 4-letter Game ID
 * @param exportFields: Mapping of type names to variables in the export model
 *      (e.g. "Mai2UserCharacter" -> Mai2DataExport::userCharacterList)
 * @param exportRepos: Mapping of variables to repositories that can be used to find the data
 * @param artemisRenames: Mapping of Artemis table names to import classes
 */
abstract class ImportController<ExportModel: IExportClass<UserModel>, UserModel: IUserData>(
    val game: String,
    val exportClass: KClass<ExportModel>,
    val exportFields: Map<String, Var<ExportModel, Any>>,
    val exportRepos: Map<Var<ExportModel, Any>, IUserRepo<UserModel, *>>,
    val artemisRenames: Map<String, ImportClass<*>>,
    val customExporters: Map<Var<ExportModel, Any>, (UserModel, ExportOptions) -> Any?> = emptyMap(),
    val customImporters: Map<Var<ExportModel, Any>, (ExportModel, UserModel) -> Unit> = emptyMap()
) {
    abstract fun createEmpty(): ExportModel
    abstract val userDataRepo: GenericUserDataRepo<UserModel>

    @Autowired lateinit var us: AquaUserServices
    @Autowired lateinit var netProps: AquaNetProps
    @Autowired lateinit var transManager: PlatformTransactionManager
    val trans by lazy { TransactionTemplate(transManager) }
    @Autowired @Lazy lateinit var fedy: Fedy

    init {
        artemisRenames.values.forEach {
            if (it.name !in exportFields) error("Code error! Export fields incomplete: missing ${it.name}")
        }
    }

    val listRepos = exportRepos.filter { it.key returns List::class }
    val singleRepos = exportRepos.filter { !(it.key returns List::class) }

    fun export(u: AquaNetUser): ExportModel = export(u, ExportOptions())

    fun export(u: AquaNetUser, options: ExportOptions) = createEmpty().apply {
        gameId = game
        userData = userDataRepo.findByCard(u.ghostCard) ?: (404 - "User not found")
        exportRepos.forEach { (f, u) ->
            if (f returns List::class) f.set(this, u.findByUser(userData))
            else u.findSingleByUser(userData)()?.let { f.set(this, it) }
        }
        customExporters.forEach { (f, exporter) ->
            exporter(userData, options)?.let { f.set(this, it) }
        }
    }

    @API("export")
    fun exportUserData(@RP token: Str) = us.jwt.auth(token) { u ->
        log.info("Exporting user data for ${u.auId}")
        export(u)
    }

    @Suppress("UNCHECKED_CAST")
    @API("import")
    fun importUserData(@RP token: Str, @RB json: Str) = us.jwt.auth(token) { u ->
        val export = json.parseJackson(exportClass.java)
        if (!export.gameId.equals(game, true)) 400 - "Invalid game ID"

        val lists = listRepos.toList().associate { (f, r) -> r to f.get(export) as List<IUserEntity<UserModel>> }.vNotNull()
        val singles = singleRepos.toList().associate { (f, r) -> r to f.get(export) as IUserEntity<UserModel> }.vNotNull()
        var repoFieldMap = exportRepos.toList().associate { (f, r) -> r to f }

        // Validate new user data
        // Check that all ids are 0 (this should be true since all ids are @JsonIgnore)
        if (export.userData.id != 0L) 400 - "User ID must be 0"
        lists.values.flatten().forEach { if (it.id != 0L) 400 - "ID must be 0" }
        singles.values.forEach { if (it.id != 0L) 400 - "ID must be 0" }

        // Set user card
        export.userData.card = u.ghostCard

        // Check existing data
        userDataRepo.findByCard(u.ghostCard)?.also { gu ->
            // Store a backup of the old data
            val fl = "${game}-backup-${u.auId}-${LocalDateTime.now().urlSafeStr()}.json"
            (Path(netProps.importBackupPath) / fl).writeText(export(u).toJson())

            // Delete the old data (After migration v1000.7, all user-linked entities have ON DELETE CASCADE)
            log.info("$game Import: Deleting old data for user ${u.auId}")
            userDataRepo.delete(gu)
            userDataRepo.flush()
        }

        trans.execute {
            // Insert new data
            val nu = userDataRepo.save(export.userData)
            // Set user fields
            lists.values.flatten().forEach { it.user = nu }
            singles.values.forEach { it.user = nu }
            // Save new data
            singles.forEach { (repo, single) -> (repo as IUserRepo<UserModel, Any>).save(single) }
            lists.forEach { (repo, list) -> (repo as IUserRepo<UserModel, Any>).saveAll(list) }
            // Handle custom importers
            customImporters.forEach { (field, importer) ->
                importer(export, nu)
            }
        }

        Fedy.getGameName(game)?.let { fedy.onImported(it, u.ghostCard.extId) }

        SUCCESS
    }

    /**
     * Read an artemis SQL dump file and return Aqua JSON
     */
    @Suppress("UNCHECKED_CAST")
    @API("convert-artemis")
    fun importArtemisSql(@RB sql: String): ImportResult {
        val data = createEmpty()
        val errors = ArrayList<String>()
        val warnings = ArrayList<String>()
        fun err(msg: String) { errors.add(msg) }
        fun warn(msg: String) { warnings.add(msg) }

        val lists = exportFields.filter { it.value returns List::class }
            .mapValues { it.value.get(data) as ArrayList<Any> }

        val statements = sql.splitLines().mapNotNull {
            try { it.asSqlInsert() }
            catch (e: Exception) { err("Failed to parse insert: $it\n${e.message}"); null }
        }

        // For each insert statement, we will try to parse the values
        statements.forEachIndexed fi@{ i, insert ->
            // Try to map tables
            val tb = artemisRenames[insert.table] ?: return@fi warn("Unknown table ${insert.table} in insert $i")
            val field = exportFields[tb.name]!!
            val obj = tb.mapTo(insert.mapping)

            // Add value to list or set field
            lists[tb.name]?.add(obj) ?: field.set(data, obj)
        }

        return ImportResult(errors, warnings, JACKSON_ARTEMIS.writeValueAsString(data))
    }

    companion object
    {
        // Map a dictionary to a class
        fun <T : Any> ImportClass<T>.mapTo(rawDict: Map<String, String>): T {
            // Process renaming
            var dict = renames?.let { rawDict
                .filter { (k, _) -> if (k in it) it[k] != null else true }
                .mapKeys { (k, _) -> it[k] ?: k } } ?: rawDict

            // Process Nones
            dict = dict.filterValues { it != "None" }

            return JACKSON_ARTEMIS.convertValue(dict, type.java)
        }

        val log = logger()
    }
}
