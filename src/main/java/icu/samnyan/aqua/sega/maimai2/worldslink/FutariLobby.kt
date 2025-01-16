
package icu.samnyan.aqua.sega.maimai2.worldslink

import ext.*
import icu.samnyan.aqua.net.utils.PathProps
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


// KotlinX Serialization
@OptIn(ExperimentalSerializationApi::class)
private val KJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
    coerceInputValues = true
}

// Maximum time to live for a recruit record
const val MAX_TTL = 30 * 1000

data class RecruitRecord(val d: RecruitInfo, val time: Long = millis())

@RestController
@RequestMapping(path = ["/mai2-futari"])
class FutariLobby(paths: PathProps) {
    // <IP Address, RecruitInfo>
    val recruits = mutableMapOf<UInt, RecruitRecord>()
    // Append writer
    lateinit var writer: BufferedWriter
    val mutex = ReentrantLock()
    val log = logger()

    init {
        paths.init()
        writer = FileOutputStream(File(paths.recruitLog), true).bufferedWriter()
    }

    fun log(data: String) = mutex.withLock {
        log.info(data)
        writer.write(data)
        writer.newLine()
        writer.flush()
    }

    fun log(data: StartRecruit, msg: String) =
        log("${LocalDateTime.now().isoDateTime()}: $msg: ${data.RecruitInfo.toJson()}")

    val StartRecruit.ip get() = RecruitInfo.MechaInfo.IpAddress

    @API("recruit/start")
    fun startRecruit(@RB data: String) {
        val d = parsing { KJson.decodeFromString<StartRecruit>(data) }
        val exists = d.ip in recruits
        recruits[d.ip] = RecruitRecord(d.RecruitInfo)

        if (!exists) log(d, "StartRecruit")
        d.RecruitInfo.MechaInfo.UserIDs = d.RecruitInfo.MechaInfo.UserIDs.map { it.str.hashToUInt().toLong() }
    }

    @API("recruit/finish")
    fun finishRecruit(@RB data: String) {
        val d = parsing { KJson.decodeFromString<StartRecruit>(data) }
        if (d.ip !in recruits) 400 - "Recruit not found"
        recruits.remove(d.ip)
        log(d, "EndRecruit")
    }

    @API("recruit/list")
    fun listRecruit(): String {
        val time = millis()
        recruits.filterValues { time - it.time > MAX_TTL }.keys.forEach { recruits.remove(it) }
        return recruits.values.toList().joinToString("\n") { KJson.encodeToString(it.d) }
    }
}

fun main(args: Array<String>) {
    val json = """{"RecruitInfo":{"MechaInfo":{"IsJoin":true,"IpAddress":1820162433,"MusicID":11692,"Entrys":[true,false],"UserIDs":[281474976710657,281474976710657],"UserNames":["ＧＵＥＳＴ","ＧＵＥＳＴ"],"IconIDs":[1,1],"FumenDifs":[0,-1],"Rateing":[0,0],"ClassValue":[0,0],"MaxClassValue":[0,0],"UserType":[0,0]},"MusicID":11692,"GroupID":0,"EventModeID":false,"JoinNumber":1,"PartyStance":0,"_startTimeTicks":638725464510308001,"_recvTimeTicks":0}}"""
    println(json.jsonMap().toJson())
    val data = KJson.decodeFromString<StartRecruit>(json)
    println(json)
    println(KJson.encodeToString(StartRecruit.serializer(), data))
    println(data)
}
