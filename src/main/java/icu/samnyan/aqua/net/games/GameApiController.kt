package icu.samnyan.aqua.net.games

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.general.model.Card
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
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

    @API("trend")
    abstract suspend fun trend(@RP username: String): List<TrendOut>
    @API("user-summary")
    abstract suspend fun userSummary(@RP username: String, @RP token: String?): GenericGameSummary

    @API("recent")
    suspend fun recent(@RP username: String): List<IGenericGamePlaylog> = us.cardByName(username) { card ->
        playlogRepo.findByUserCardExtId(card.extId)
    }

    // List<Pair<should_hide, player>>>
    private var rankingCache: List<Pair<Bool, GenericRankingPlayer>> = emptyList()
    private var rankingCacheLock = ReentrantLock()
    // Sorted index List<Rating> = Rank
    private var rankingSortedIndex: List<Int> = emptyList()
    @API("ranking")
    fun ranking(@RP token: String?): List<GenericRankingPlayer> {
        val time = millis()

        // Check cache validity
        if (rankingCache.isEmpty()) (500 - "Ranking is computing... please wait")

        val reqUser = token?.let { us.jwt.auth(it) }?.let { u ->
            // Optimization: If the user is not banned, we don't need to process user information
            if (!u.ghostCard.rankingBanned && !u.cards.any { it.rankingBanned }) null
            else u
        }

        // Read from cache if we just computed it less than duration ago
        // Shadow-ban: Do not show banned cards in the ranking except for the user who owns the card
        // TODO: pagination
        return rankingCache.filter { !it.l || it.r.username == reqUser?.username }
            .mapIndexed { i, it -> it.r.apply { rank = i + 1 } }
            .also { logger.info("Ranking returned in ${millis() - time}ms") }
    }

    @PostConstruct
    fun rakingCacheInit() = thread { rankingCacheRun() }

    // Every 20 minutes
    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.MINUTES)
    fun rankingCacheRun() = rankingCacheLock.maybeLock { rankingCacheCompute() }

    fun rankingCacheCompute() {
        val time = millis()
        val tableName = when (name) { "mai2" -> "maimai2"; "chu3" -> "chusan"; else -> name }
        rankingCache = us.em.createNativeQuery(
            """
                SELECT
                    c.id,
                    u.user_name,
                    u.player_rating,
                    u.last_play_date,
                    AVG(p.achievement) / 10000.0 AS acc,
                    SUM(p.is_full_combo) AS fc,
                    SUM(p.is_all_perfect) AS ap,
                    c.ranking_banned or a.opt_out_of_leaderboard AS hide,
                    a.username
                FROM ${tableName}_user_playlog_view p
                     JOIN ${tableName}_user_data_view u ON p.user_id = u.id
                     JOIN sega_card c ON u.aime_card_id = c.id
                     LEFT JOIN aqua_net_user a ON c.net_user_id = a.au_id
                GROUP BY p.user_id, u.player_rating
                ORDER BY u.player_rating DESC;
            """
        ).exec.mapIndexed { i, it ->
            it[7].truthy to GenericRankingPlayer(
                rank = i + 1,
                name = it[1].toString(),
                rating = it[2]!!.int,
                lastSeen = it[3].toString(),
                accuracy = it[4]!!.double,
                fullCombo = it[5]!!.int,
                allPerfect = it[6]!!.int,
                username = it[8]?.toString() ?: "user${it[0]}"
            )
        }
        rankingSortedIndex = rankingCache.filter { !it.l }.map { it.r.rating }.reversed()
        logger.info("Ranking for $name computed in ${millis() - time}ms")
    }

    @API("playlog")
    fun playlog(@RP id: Long): IGenericGamePlaylog = playlogRepo.findById(id).getOrNull() ?: (404 - "Playlog not found")

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
            SUCCESS
        }
    }

    @API("user-music-from-list")
    suspend fun userMusicFromList(@RP username: Str, @RB musicList: List<Int>) = us.cardByName(username) { card ->
        userMusicRepo.findByUser_Card_ExtIdAndMusicIdIn(card.extId, musicList)
    }

    fun genericUserSummary(card: Card, ratingComp: Map<String, String>, rival: Boolean? = null): GenericGameSummary {
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

        // Find serverRank by binary-searching in the rankingSortedIndex to find the minimal index that
        // is greater than or equal to the user's rating
        var serverRank = rankingSortedIndex.binarySearch(user.playerRating).let { if (it < 0) -it - 1 else it + 1 }
        serverRank = rankingSortedIndex.size - serverRank

        return GenericGameSummary(
            name = user.userName,
            aquaUser = card.aquaUser?.publicFields,
            serverRank = serverRank.long,
            accuracy = plays.acc(),
            rating = user.playerRating,
            ratingHighest = user.highestRating,
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
            lastPlayedHost = user.lastClientId?.let { us.userRepo.findByKeychip(it)?.username },
            rival = rival
        )
    }
}
