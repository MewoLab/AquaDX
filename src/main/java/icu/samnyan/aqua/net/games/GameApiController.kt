package icu.samnyan.aqua.net.games

import ext.*
import icu.samnyan.aqua.net.BotProps
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.allnet.UserKeychipRepo
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.general.service.CardService
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass

abstract class GameApiController<T : IUserData>(val name: String, userDataClass: KClass<T>) {
    val musicMapping = resJson<Map<String, GenericMusicMeta>>("/meta/$name/music.json")
        ?.mapKeys { it.key.toInt() } ?: emptyMap()
    val logger = LoggerFactory.getLogger(javaClass)

    val itemMapping = resJson<Map<String, Map<String, GenericItemMeta>>>("/meta/$name/items.json") ?: emptyMap()

    abstract val us: AquaUserServices
    abstract val userDataRepo: GenericUserDataRepo<T>
    abstract val playlogRepo: GenericPlaylogRepo<*>
    abstract val userMusicRepo: GenericUserMusicRepo<*>
    abstract val shownRanks: List<Pair<Int, String>>

    abstract val settableFields: Map<String, (T, String) -> Unit>
    open val gettableFields: Set<String> = setOf()

    @Autowired lateinit var cardService: CardService
    @Autowired lateinit var userKeychipRepo: UserKeychipRepo

    @API("trend")
    abstract suspend fun trend(@RP username: String): List<TrendOut>
    @API("user-summary")
    abstract suspend fun userSummary(@RP username: String, @RP token: String?): GenericGameSummary

    @API("recent")
    suspend fun recent(@RP username: String): List<IGenericGamePlaylog> = us.cardByName(username) { card ->
        playlogRepo.findByUserCardExtId(card.extId)
    }

    // List<Pair<should_hide, player>>>
    private var rankingCache: List<GenericRankingPlayer> = emptyList()
    private var rankingCacheLock = ReentrantLock()
    // Sorted index List<Rating> = Rank
    private var rankingLookupCache: Map<Long, GenericRankingPlayer> = emptyMap()
    private val pageSize = 100

    @API("ranking")
    fun ranking(@RP token: String?, @RP page: Int?): List<GenericRankingPlayer> {
        val time = millis()

        // Check cache validity
        if (rankingCache.isEmpty()) (500 - "Rank is empty or is currently computing.")

        return page?.let {
            if (it < 0) (400 - "Invalid page number")
            rankingCache.drop(it * pageSize).take(pageSize)
        } ?: rankingCache
    }

    @PostConstruct
    fun rankingCacheInit() = thread { rankingCacheRun() }

    // Every 20 minutes
    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.MINUTES)
    fun rankingCacheRun() = rankingCacheLock.maybeLock { rankingCacheCompute() }

    private val tableName = when (name) { "mai2" -> "maimai2"; "chu3" -> "chusan"; else -> name }
    fun rankingCacheCompute() {
        val time = millis()
        rankingCache = us.em.createNativeQuery(
            """
                SELECT
                    c.id,
                    u.user_name,
                    ${if (name == "ongeki") "u.new_player_rating" else "u.player_rating"} AS rating,
                    u.last_play_date,
                    AVG(p.achievement) / 10000.0 AS acc,
                    SUM(p.is_full_combo) AS fc,
                    SUM(p.is_all_perfect) AS ap,
                    c.ranking_banned or a.opt_out_of_leaderboard or c.status = 12 AS hide,
                    a.username
                FROM ${tableName}_user_playlog_view p
                     JOIN ${tableName}_user_data_view u ON p.user_id = u.id
                     JOIN sega_card c ON u.aime_card_id = c.id
                     LEFT JOIN aqua_net_user a ON c.net_user_id = a.au_id
                GROUP BY p.user_id, u.player_rating
                HAVING NOT hide ${if (name == "ongeki") "AND rating > 0" else "" /* Hide users on Ongeki 1.45 and below */}
                ORDER BY u.player_rating DESC;
            """
        ).exec.mapIndexed { i, it ->
            GenericRankingPlayer(
                rank = i + 1,
                id = it[0]!!.long,
                name = it[1].toString(),
                rating = it[2]!!.int,
                lastSeen = it[3].toString(),
                accuracy = it[4]!!.double,
                fullCombo = it[5]!!.int,
                allPerfect = it[6]!!.int,
                username = it[8]?.toString() ?: "user${it[0]}"
            )
        }
        rankingLookupCache = rankingCache.associateBy { it.id }
        logger.info("Ranking for $name computed in ${millis() - time}ms")
    }

    @API("playlog")
    fun playlog(@RP id: Long): IGenericGamePlaylog = playlogRepo.findById(id)() ?: (404 - "Playlog not found")

    val userDetailFields by lazy { userDataClass.gettersMap().let { vm ->
        (settableFields.keys.toSet() + gettableFields)
            .associateWith { k -> (vm[k] ?: error("Field $k not found")) }
    } }
    @API("user-detail")
    suspend fun userDetail(@RP username: String) = us.cardByName(username) { card ->
        val u = userDataRepo.findByCard(card) ?: (404 - "User not found")
        userDetailFields.toList().associate { (k, f) -> k to f.invoke(u) }
    }
    @API("user-detail-set")
    suspend fun userDetailSet(@RP token: String, @RP field: String, @RP value: String): Any {
        val prop = settableFields[field] ?: (400 - "Invalid field $field")

        return us.jwt.auth(token) { u ->
            val user = async { userDataRepo.findByCard(u.ghostCard) } ?: (404 - "User not found")
            prop(user, value)
            async { userDataRepo.save(user) }
            cardService.updateCardTimestamp(u.ghostCard, name)
            SUCCESS
        }
    }

    @API("user-option")
    open suspend fun userOption(@RP token: String): Any? = 400 - "Unsupported by this game"
    @API("user-option-set")
    open suspend fun userOptionSet(@RP token: String, @RP field: String, @RP value: Int): Any = 400 - "Unsupported by this game"

    @API("user-music-from-list")
    suspend fun userMusicFromList(@RP username: Str, @RB musicList: List<Int>) = us.cardByName(username) { card ->
        userMusicRepo.findByUser_Card_ExtIdAndMusicIdIn(card.extId, musicList)
    }

    open fun getRating(user: T, isHighest: Bool): Int {
        return if (isHighest) user.highestRating else user.playerRating;
    }

    fun genericUserSummary(card: Card, ratingComp: Map<String, String>, rival: Boolean? = null, favorites: List<Int>? = null): GenericGameSummary {
        // Summary values: total plays, player rating, server-wide ranking
        // number of each rank, max combo, number of full combo, number of all perfect
        val user = userDataRepo.findByCard(card) ?: (404 - "Game data not found")
        val plays = playlogRepo.findByUserCardExtId(card.extId)

        // Detailed ranks: Find the number of each rank in each level category
        // map<level, map<rank, count>>
        val rankMap = shownRanks.associate { (_, v) -> v to 0 }
        val detailedRanks = HashMap<Int, MutableMap<String, Int>>()
        plays.forEach { play ->
            val lvl = musicMapping[play.musicId]?.notes?.getOrNull(if (play.level == 10) 0 else play.level)?.lv ?: return@forEach
            shownRanks.find { (s, _) -> play.achievement > s }?.let { (_, v) ->
                val ranks = detailedRanks.getOrPut(lvl.toInt()) { rankMap.mut }
                ranks[v] = ranks[v]!! + 1
            }
        }

        // Collapse detailed ranks to get non-detailed ranks map<rank, count>
        val ranks = shownRanks.associate { (_, v) -> v to 0 }.mut.also { ranks ->
            plays.forEach { play ->
                shownRanks.find { (s, _) -> play.achievement > s }?.let { (_, v) -> ranks[v] = ranks[v]!! + 1 }
            }
        }

        return GenericGameSummary(
            name = user.userName,
            aquaUser = card.aquaUser?.publicFields,
            serverRank = rankingLookupCache[user.card!!.id]?.rank?.str ?: "-",
            accuracy = plays.acc(),
            rating = getRating(user, false),
            ratingHighest = getRating(user, true),
            ratingNotGeneric = getRating(user, false) != user.playerRating,
            ranks = ranks.map { (k, v) -> RankCount(k, v) },
            detailedRanks = detailedRanks,
            maxCombo = plays.maxOfOrNull { it.maxCombo } ?: 0,
            fullCombo = plays.count { it.isFullCombo },
            allPerfect = plays.count { it.isAllPerfect },
            totalScore = user.totalScore,
            plays = plays.size,
            totalPlayTime = plays.count() * 3L, // TODO: Give a better estimate
            joined = user.firstPlayDate.toString(),
            lastSeen = user.lastPlayDate.toString(),
            lastVersion = user.lastRomVersion,
            ratingComposition = ratingComp,
            recent = plays.sortedBy { it.userPlayDate.toString() }.takeLast(100).reversed(),
            lastPlayedHost = user.lastClientId?.let { userKeychipRepo.findByKeychipId(it)?.user?.username },
            rival = rival,
            favorites = favorites
        )
    }

    // Recommender System Integration
    @Autowired lateinit var botProps: BotProps
    // Map<userId, List<musicId>>
    var recommendedMusic: Map<Long, List<Int>> = emptyMap()

    @API("recommender-fetch")
    fun recommenderFetchPlays(@RP botSecret: String) = run {
        if (botSecret != botProps.secret) 403 - "Invalid Secret"

        us.em.createNativeQuery("""
            SELECT user_id, music_id, count(*) as count
            FROM ${tableName}_user_playlog_view
            GROUP BY user_id, music_id;
        """.trimIndent()).exec.also {
            logger.info("Recommender fetched ${it.size} plays")
        }.numCsv("user_id", "music_id", "count")
    }

    @API("recommender-update")
    fun recommenderUpdate(@RP botSecret: String, @RB data: Map<Long, List<Int>>) {
        if (botSecret != botProps.secret) 403 - "Invalid Secret"
        recommendedMusic = data
        logger.info("Recommender updated with ${data.size} users")
    }

    @API("song-pop")
    fun songPopRanking() = us.pop.ranking[tableName]
}
