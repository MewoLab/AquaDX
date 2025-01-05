package icu.samnyan.aqua.sega.wacca

import ext.*
import icu.samnyan.aqua.net.db.AquaGameOptions
import icu.samnyan.aqua.net.games.wacca.Wacca
import icu.samnyan.aqua.net.utils.ApiException
import icu.samnyan.aqua.net.utils.simpleDescribe
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.wacca.WaccaItemType.*
import icu.samnyan.aqua.sega.wacca.WaccaItemType.NOTE_COLOR
import icu.samnyan.aqua.sega.wacca.WaccaItemType.NOTE_SOUND
import icu.samnyan.aqua.sega.wacca.WaccaItemType.TOUCH_EFFECT
import icu.samnyan.aqua.sega.wacca.WaccaOptionType.*
import icu.samnyan.aqua.sega.wacca.model.BaseRequest
import icu.samnyan.aqua.sega.wacca.model.db.*
import icu.samnyan.aqua.spring.Metrics
import io.ktor.client.utils.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.math.max
import kotlin.math.min

@RestController
@API("/g/wacca/")
class WaccaServer {
    // These are lateinit autowired instead of constructor injection because the tests depend on creating
    // an instance of this class with no arguments.
    @Autowired lateinit var cardRepo: CardRepository
    @Autowired lateinit var rp: WaccaRepos
    @Autowired lateinit var wacca: Wacca

    val handlerMap = mutableMapOf<String, (BaseRequest, List<Any>) -> Any>()
    val cacheMap = mutableMapOf<String, String>()

    val log = logger()
    val season = 3
    val enabledGates = 1..24
    // [[stageId, danLevel], ...]
    @Suppress("UNCHECKED_CAST")
    val enabledStages = ("[[3014, 14], [3013, 13], [3012, 12], [3011, 11], [3010, 10], [3009, 9], [3008, 8], [3007, 7], [3006, 6], [3005, 5], [3004, 4], [3003, 3], [3002, 2], [3001, 1], [210001, 0], [210002, 0], [210003, 0], [310001, 0], [310002, 0], [310003, 0], [310004, 0], [310005, 0], [310006, 0]]"
        .jsonArray() as List<List<Int>>).associate { it[0].int() to it[1].int() }
    val enabledTickets = listOf(106001, 106002, 206001, 206002)

    init { init() }

    // DSL Functions
    fun user(uid: Any) = if (uid == 0) null else rp.user.findByCardExtId(uid.uint32())
    fun options(u: WaccaUser?) = u?.let { rp.option.findByUser(u).associate { it.optId to it.value } } ?: emptyMap()
    fun itmGrp(u: WaccaUser) = rp.item.findByUser(u).groupBy { it.type }.mapValues { it.value.associateBy { it.itemId } }
    operator fun Map<Int, Int>.get(type: WaccaOptionType) = getOrDefault(type.id, type.default)
    operator fun String.minus(value: Any) = value
    operator fun String.invoke(block: (BaseRequest, List<Any>) -> Any) { handlerMap[this.lowercase()] = block }
    infix fun String.cached(block: () -> Any) { cacheMap[this.lowercase()] =
        block().let { if (it is String) it else it.toJson() } }
    infix fun String.empty(block: (BaseRequest, List<Any>) -> Any)
    { handlerMap[this.lowercase()] = { req, lst -> empty.also { block(req, lst) } } }
    infix fun String.redirect(path: String) {
        handlerMap[this.lowercase()] = handlerMap[path.lowercase()] ?: error("Server badly configured")
    }

    /** Convert "3.07.01.JPN.26935.S" into "3.7.1" */
    fun String.shortVer() = split('.').let { if (it.size < 3) "1.0.0" else
        "${it[0]}.${it[1].toInt()}.${it[2].toInt()}" }

    /** Generate response message */
    fun resp(paramsJson: String, status: Int = 0, message: String = ""): ResponseEntity<String> {
        val resp = """{"status":$status,"message":"${message.replace("\"", "\\\"")}","serverTime":${millis() / 1000},""" +
            """"maintNoticeTime":0,"maintNotPlayableTime":0,"maintStartTime":0,"params":$paramsJson}"""

        return ResponseEntity.ok().headers(
            "X-Wacca-Hash" to resp.md5(),
            "Content-Type" to "application/json; charset=utf-8"
        ).body(resp)
    }

    /** Handle all requests */
    @API("/api/**")
    fun handle(req: HttpServletRequest, @RB body: String): Any {
        // Normalize path
        val path = req.requestURI.removePrefix("/g/wacca").removePrefix("/WaccaServlet")
            .removePrefix("/api").removePrefix("/").lowercase()

        if (path !in cacheMap && path !in handlerMap) {
            return resp("[]", 1, "Not Found")
        }

        // Only record the counter metrics if the API is known.
        Metrics.counter("aquadx_wacca_api_call", "api" to path).increment()

        if (path in cacheMap) return resp(cacheMap[path]!!)

        log.info("Wacca < $path : $body")

        return try {
            Metrics.timer("aquadx_wacca_api_latency", "api" to path).recordCallable {
                val br = JACKSON.parse<BaseRequest>(body)
                handlerMap[path]!!(br, br.params).let { when (it) {
                    is String -> resp(it)
                    is List<*> -> resp(it.toJson())
                    else -> error("Invalid response type ${it.javaClass}")
                } }.also { log.info("Wacca > $path : ${it.body}") }
            }
        } catch (e: Exception) {
            Metrics.counter(
                "aquadx_wacca_api_error",
                "api" to path, "error" to e.simpleDescribe()
            ).increment()

            if (e is ApiException) {
                resp("[]", e.code, e.message ?: "")
            } else {
                log.error("Wacca > Error", e)
                resp("[]", 500, e.message ?: "")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun WaccaServer.init() {
    "housing/get" cached { ls("housingId" - 39, "isNewCab" - 0) }
    "housing/start" cached { ls("regionId" - 1, "recommendSongList" - ls(1269, 1007, 1270, 1002, 1020, 1003, 1008,
        1211, 1018, 1092, 1056, 32, 1260, 1230, 1258, 1251, 2212, 1264, 1125, 1037, 2001, 1272, 1126, 1119, 1104, 1070,
        1047, 1044, 1027, 1004, 1001, 24, 2068, 2062, 2021, 1275, 1249, 1207, 1203, 1107, 1021, 1009, 9, 4, 3, 23, 22,
        2014, 13, 1276, 1247, 1240, 1237, 1128, 1114, 1110, 1109, 1102, 1045, 1043, 1036, 1035, 1030, 1023, 1015))
    }
    "advertise/GetRanking" cached { "[]" }
    "advertise/GetNews" cached { "[[], [], [], [], [], [], [], [], []]" }
    "user/info/GetMyRoom" cached { ls(0, 0, 0, 0, 0, empty, 0, 0, 0) }
    "user/status/Logout" cached { "[]" }
    "user/info/GetRanking" cached { ls("totalScore#" - 0, "highScoreBySong#" - 0, "cumulativeScore#" - 0,
        "stateUpScore#" - 0, "otherScore#" - 0, "waccaPoints#" - 0) }
    "competition/status/login" cached { "[]" }
    "competition/status/update" cached { "[]" }

    "user/status/get" { req, (uid) ->
        val ru = user(uid)
        val u = ru ?: WaccaUser()
        val o = options(ru)
        u.run { ls(u.lStatus(),
            o[SET_TITLE_ID], o[SET_ICON_ID],
            "status" - if (ru == null) 1 else 0, // 0 = GOOD, 1 = Register
            "version" - ls(req.appVersion.shortVer().compareTo(this.lastRomVersion.shortVer())
                .let { if (it < 0) 1 else if (it > 0) 2 else 0 }, // 0 = Version GOOD, 1 = Game is newer, 2 = Game is older
                this.lastRomVersion.shortVer()),
            o.map { (k, v) -> ls(k, v) }
        ) }
    }

    "user/status/create" { _, (uid, name) ->
        if (user(uid) != null) 404 - "User already exists"

        val u = rp.user.save(WaccaUser().apply {
            card = cardRepo.findByExtId(uid.uint32())() ?: (404 - "Card not found")
            userName = name.toString()
        })

        // Starter items
        rp.item.saveAll(
            ls(104001, 104002, 104003, 104005).map { TITLE(u, it) } +
            ls(102001, 102002).map { ICON(u, it) } +
            ls(103001, 203001).map { NOTE_COLOR(u, it) } +
            ls(105001, 205005).map { NOTE_SOUND(u, it) } +
            (ls(210001, 210002, 310001, 310002) + (210054..210061)).map { NAVIGATOR(u, it) } +
            ls(211001).map { USER_PLATE(u, it) } +
            ls(312000, 312001).map { TOUCH_EFFECT(u, it) }
        )

        ls(u.lStatus())
    }

    "user/status/login" api@ { req, (uid) ->
        val u = user(uid)
        if (uid == 0 || u == null) return@api "[[], [], [], 0, [2077, 1, 1, 1, [], []], 0, []]"

        // Record login
        rp.user.save(u.apply {
            lastClientId = req.chipId
            loginCount++
            if (millis() - lastConsecDate.time > 23 * 60 * 60 * 1000) {
                loginCountDays++
                loginCountToday = 0
                lastConsecDate = Date()
                if (millis() - this.lastPlayDate.time < 2 * 24 * 60 * 60 * 1000) loginCountDaysConsec++
            }
            loginCountToday++
            this.lastPlayDate = Date()
        })

        "[[], [], [], 0, [2077, 1, 1, 1, [], []], ${u.lastPlayDate.time / 1000}, []]"
    }

    "user/status/GetDetail" api@ { _, (uid) ->
        val u = user(uid) ?: return@api "[]"
        val o = options(u)
        val items = rp.item.findByUser(u).groupBy { it.type }.mut
        val scores = rp.bestScore.findByUser(u)
        val scoreMap = scores.associateBy { it.musicId to it.level }
        val gates = rp.gate.findByUser(u)
        val bingo = rp.bingo.findByUser(u).firstOrNull()
        val go = u.card?.aquaUser?.gameOptions ?: AquaGameOptions()

        // All unlock
        if (go.unlockMusic && wacca.musicMapping.isNotEmpty()) {
            items[MUSIC_UNLOCK()] = wacca.musicMapping.map { (id, v) -> MUSIC_UNLOCK(u, id, p1 = v.notes.size.long() - 1) }
        }
        if (go.unlockTickets) {
            var i = 0
            items[TICKET()] = enabledTickets.flatMap { (1..5).map { TICKET(u, it).apply { id = (i++).toLong() } } }
        }
        if (go.unlockChara) {
            wacca.itemMapping["plates"]?.let { items[USER_PLATE()] = it.map { (k, _) -> USER_PLATE(u, k.int()) } }
        }
        if (go.unlockCollectables) {
            // TODO: Add titles
            mapOf("icon" to ICON, "plates" to USER_PLATE, "trophy" to TROPHY).map { (name, type) ->
                wacca.itemMapping[name]?.let { items[type()] = it.map { (k, _) -> type(u, k.int()) } }
            }
        }

        u.run { ls(
            "0 status" - lStatus(),
            "1 options" - o.map { (k, v) -> ls(k, v) },
            "2 seasonalPlayModeCounts" - (playCounts.mapIndexed { i, it -> ls(season, i + 1, it) } + ls(ls(0, 1, 1))),
            "3 items" - ls(MUSIC_UNLOCK, TITLE, ICON, TROPHY, SKILL, TICKET, NOTE_COLOR, NOTE_SOUND, NAVIGATOR, USER_PLATE, TOUCH_EFFECT).map {
                if (it == MUSIC_UNLOCK) items[it()]?.flatMap { song ->
                    // Add all difficulties up to the highest unlocked
                    (1..song.p1).map { diff -> ls(song.itemId, diff, 0, song.acquiredDate.sec) }
                } ?: empty
                else items[it()]?.map { it.ls() } ?: empty
            },
            "4 scores" - (scores.map { it.ls() } + (items[MUSIC_UNLOCK()]?.flatMap { song ->
                // If the song is unlocked but hasn't been played, add a song in the score list with 0 clears
                (WaccaDifficulty.HARD()..song.p1).filter { scoreMap[song.itemId to it.int()] == null }.map { diff ->
                    WcUserScore().apply { user = u; musicId = song.itemId; level = diff.int() }.ls()
                }
            } ?: empty)),
            "5 songPlayStatus" - ls(lastSongInfo[0], 1),
            "6 seasonInfo" - ls(xp, wpTotal, wpSpent, u.totalScore,
                items[TITLE()]?.size ?: 0, items[ICON()]?.size ?: 0, 0,
                items[NOTE_COLOR()]?.size ?: 0, items[NOTE_SOUND()]?.size ?: 0, items[USER_PLATE()]?.size ?: 0,
                gates.sumOf { it.totalPoints }),
            "7 playAreaList" - "[[0],[0,0,0,0,0,0],[0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0],[0,0,0,0],[0,0,0,0,0,0,0],[0]]".jsonArray(),
            "8 songUpdateTime" - this.lastPlayDate.time / 1000,
            "9 favorites" - u.favoriteSongs,
            "10 stoppedSongIds" - empty,
            "11 events" - empty,
            "12 gate" - gates.associateBy { it.gateId }.let { gateMap -> enabledGates.map {
                gateMap[it]?.ls() ?: WcUserGate().apply { gateId = it }.ls()
            } },
            "13 lastSongInfo" - lastSongInfo,
            "14 gateTutorialFlags" - gateTutorialFlags.jsonArray(),
            "15 gatchaInfo" - empty,
            "16 friendList" - empty,
            "17 bingoStatus" - ls(
                "pageNumber" - (bingo?.pageNumber ?: 0),
                "pageStatus" - (bingo?.pageProgress?.jsonArray() ?: empty)
            )
        ) }
    }

    fun addItems(recv: List<List<Int>>, u: WaccaUser, items: Map<Int, Map<Int, WcUserItem>>) {
        if (recv.isEmpty()) return
        val newItems = mutableListOf<WcUserItem>()
        recv.forEach { (type, id, param) ->
            var ex = items[type]?.get(id)
            if (type == MUSIC_DIFFICULTY_UNLOCK()) ex = ex ?: items[MUSIC_UNLOCK()]?.get(id)
            when (type) {
                WP() -> { u.wp += param; u.wpTotal += param }
                XP() -> u.xp += param
                MUSIC_DIFFICULTY_UNLOCK(),
                MUSIC_UNLOCK() -> newItems += (ex ?: MUSIC_UNLOCK(u, id))
                    .apply { p1 = max(param.long(), p1).coerceAtLeast(WaccaDifficulty.HARD.value.long()) }
                TROPHY() -> newItems += (ex ?: TROPHY(u, id)).apply { p1 = season.long(); p2 = param.long() }
                else -> newItems += (ex ?: WcUserItem(type, id)).apply { user = u }
            }
        }
        rp.user.save(u)
        rp.item.saveAll(newItems)
    }

    fun useItems(recv: List<List<Int>>, u: WaccaUser, items: Map<Int, Map<Int, WcUserItem>>) {
        if (recv.isEmpty()) return
        val toDelete = mutableListOf<WcUserItem>()
        recv.forEach { (type, id, param) ->
            val ex = items[type]?.get(id)
            val oldWp = u.wp
            // Only WP and Tickets can be used.
            when (type) {
                WP() -> { u.wp = (u.wp - param).coerceAtLeast(0); u.wpSpent += oldWp - u.wp }
                TICKET() -> ex?.let { toDelete += it }
                else -> log.warn("User ${u.id} tried to use an unusable item of type $type.")
            }
        }
        rp.user.save(u)
        rp.item.deleteAll(toDelete)
    }

    "user/sugoroku/update" empty { _, (uid, gid, page, progress, loops, _, itemsRecv, totalPts, missionFlag) ->
        val u = user(uid) ?: (404 - "User not found")
        val g = rp.gate.findByUserAndGateId(u, gid.int()) ?: WcUserGate().apply { user = u; gateId = gid.int() }
        val items = itmGrp(u)

        // Update gate
        rp.gate.save(g.also {
            it.page = page.int()
            it.progress = progress.int()
            it.loops = loops.int()
            it.missionFlag = missionFlag.int()
            it.totalPoints = totalPts.int()
            it.lastUsed = Date()
        })

        operator fun <T> MutableList<T>.plusAssign(item: T) { add(item) }

        // Update items
        addItems(itemsRecv as List<List<Int>>, u, items)
    }

    "user/mission/update" empty { _, (uid, bingoDetail, items, tutFlags) ->
        val u = user(uid) ?: (404 - "User not found")
        rp.user.save(u.apply { gateTutorialFlags = tutFlags.toJson() })
        addItems(items as List<List<Int>>, u, itmGrp(u))

        // Update bingo
        val (page, prog) = bingoDetail as List<Any>
        val bingo = rp.bingo.findByUser(u).firstOrNull() ?: WcUserBingo().apply { user = u; pageNumber = page.int() }
        rp.bingo.save(bingo.apply { pageProgress = prog.toJson() })
    }

    "user/music/update" { _, (uid, _, details, items) ->
        val u = user(uid) ?: (404 - "User not found")
        addItems(items as List<List<Int>>, u, itmGrp(u))

        // Upsert playlog
        val pl = WcUserPlayLog.parse(details as List<*>).apply { user = u }

        // Update best record
        val best = rp.bestScore.save((rp.bestScore.findByUserAndMusicIdAndLevel(u, pl.musicId, pl.level)
            ?: WcUserScore().apply { user = u; musicId = pl.musicId; level = pl.level }).apply {

            grades[WaccaGrades.valueMap[pl.grade]?.ordinal ?: (400 - "Grade ${pl.grade} invalid")]++
            clears = clears.zip(pl.clears()) { a, b -> a + b }.mut
            achievement = max(achievement, pl.achievement)
            bestCombo = max(bestCombo, pl.maxCombo)
            lowestMissCt = min(lowestMissCt, pl.judgements[3])
            rating = waccaRating(achievement, pl.levelConst)
        })

        // ⭐ Calculate player rating ⭐
        // Take the top 50 scores ranked by rating
        pl.beforeRating = u.playerRating
        val top50 = rp.bestScore.findTop50(u)
        u.playerRating = top50.sumOf { it.rating }
        pl.afterRating = u.playerRating

        // Re-calculate user total score
        u.totalScore = rp.bestScore.sumScoreByUser(u)

        rp.user.save(u)
        rp.playLog.save(pl)

        ls(best.lsMusicUpdate(), ls(pl.musicId, best.clears[0]), "seasonalInfo" - (1..11).map { 0 }, "ranking" - empty)
    }
    "user/music/UpdateCoop" redirect "user/music/update"
    "user/music/UpdateVersus" redirect "user/music/update"
    "user/music/UpdateTrial" redirect "user/music/update"

    "user/music/unlock" { _, (uid, songId, diff, itemUsed) ->
        val u = user(uid) ?: (404 - "User not found")
        val items = itmGrp(u)

        addItems(ls(ls(MUSIC_UNLOCK(), songId.int(), diff.int())), u, items)
        useItems(itemUsed as List<List<Int>>, u, items)

        ls(u.moddedWp, rp.item.findByUserAndType(u, TICKET()).map { it.ls() })
    }

    "user/goods/purchase" { _, (uid, pid, count, purchaseType, cost, items) ->
        val u = user(uid) ?: (404 - "User not found")
        val itemsMap = itmGrp(u)

        addItems(items as List<List<Int>>, u, itemsMap)
        // WP Purchase = 2, Credit purchase = 1
        if (purchaseType == 2) useItems(ls(ls(WP(), 0, cost.int())), u, itemsMap)

        ls(u.moddedWp, rp.item.findByUserAndType(u, TICKET()).map { it.ls() })
    }

    "user/rating/update" empty { _, (uid, newRating, songs) ->
        val u = user(uid) ?: (404 - "User not found")

        // Update best record
        (songs as List<List<Any>>).forEach { (songId, diff, newRating) ->
            val best = rp.bestScore.findByUserAndMusicIdAndLevel(u, songId.int(), diff.int()) ?: return@forEach
            best.rating = newRating.int()
            rp.bestScore.save(best)
        }

        rp.user.save(u.apply {
            playerRating = newRating.int()
            highestRating = max(highestRating, newRating.int())
            totalScore = rp.bestScore.sumScoreByUser(u)
        })
    }

    fun incrUses(u: WaccaUser, opts: Map<Int, Int>) {
        rp.item.findByUserAndItemIdAndType(u, opts[SET_ICON_ID], ICON())
            ?.let { rp.item.save(it.apply { p1++ }) }
        rp.item.findByUserAndItemIdAndType(u, opts[SET_NAV_ID], NAVIGATOR())
            ?.let { rp.item.save(it.apply { p1++ }) }
    }

    fun afterPlay(u: WaccaUser, items: List<List<Int>>, playType: Int, version: String) {
        rp.user.save(u.apply {
            playCounts[playType.int() - 1]++
            addItems(items, u, itmGrp(u))
            lastRomVersion = version
            incrUses(u, options(u))
        })
    }

    "user/status/update" empty { req, (uid, playType, items, isContinue, isFirstPlayFree, itemsUsed, lastSong) ->
        val u = user(uid) ?: (404 - "User not found")
        u.lastSongInfo = (lastSong as List<Int>).mut
        afterPlay(u, items as List<List<Int>>, playType.int(), req.appVersion)
    }

    "user/info/update" empty { _, (uid, opts, _, dates, favAdd, favRem) ->
        val u = user(uid) ?: (404 - "User not found")

        // Update options
        val o = rp.option.findByUser(u).associate { it.optId to it }
        rp.option.saveAll((opts as List<List<Int>>).map { (k, v) -> o[k]?.apply { value = v }
            ?: WcUserOption(k, v).apply { user = u } })

        // Update favorite songs
        rp.user.save(u.apply { favoriteSongs.apply {
            addAll(favAdd as List<Int>)
            removeAll(favRem as List<Int>)
        } })
    }

    // TODO: Test this
    "user/trial/get" { _, (uid) ->
        val u = user(uid) ?: (404 - "User not found")
        val stage = rp.stageUp.findByUser(u).associateBy { it.stageId }

        enabledStages.map { (stageId, danLevel) -> (stage[stageId] ?: WcUserStageUp()).ls(danLevel) }
    }

    "user/trial/update" { req, (uid, sid, dan, clearType, scoresRaw, clearCt, items) ->
        val u = user(uid) ?: (404 - "User not found")
        val stage = rp.stageUp.findByUser(u).associateBy { it.stageId }
        val scores = scoresRaw as List<Int>

        val s = stage[sid.int()] ?: WcUserStageUp().apply { user = u; stageId = sid.int() }
        rp.stageUp.save(s.apply {
            clearStatus = clearType.int() // 0..3: Fail, Blue, Silver, Gold
            clearSongCt = clearCt.int()
            playCt++
            if (scores.sum() > s.songScores.sum()) songScores = scores.mut
        })

        if (dan.int() > u.danLevel || (dan.int() == u.danLevel && clearType.int() > u.danType)) {
            rp.user.save(u.apply {
                danLevel = dan.int()
                danType = clearType.int()
            })
        }

        afterPlay(u, items as List<List<Int>>, 4, req.appVersion)
    }

    // TODO: Test this
    "user/vip/get" { _, (uid) ->
        val u = user(uid) ?: (404 - "User not found")
        val vipDays = (u.moddedVipExpire.time - millis()) / (24 * 60 * 60 * 1000)
        ls(vipDays, ls(1, 1, "presents" - empty))
    }

    "user/vip/start" { _, (uid, cost, days) ->
        val u = user(uid) ?: (404 - "User not found")
        rp.user.save(u.apply { vipExpireTime = Date(millis() + days.int() * (24 * 60 * 60 * 1000)) })
        ls(u.moddedVipExpire.sec, "presents" - empty)
    }
}
