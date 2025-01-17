@file:Suppress("PropertyName")

package icu.samnyan.aqua.sega.maimai2.worldslink

import ext.*
import kotlinx.serialization.Serializable

object Command {
    // Control plane
    const val CTL_START = 1u
    const val CTL_BIND = 2u
    const val CTL_HEARTBEAT = 3u
    const val CTL_TCP_CONNECT = 4u  // Accept a new multiplexed TCP stream
    const val CTL_TCP_ACCEPT = 5u
    const val CTL_TCP_ACCEPT_ACK = 6u
    const val CTL_TCP_CLOSE = 7u

    // Data plane
    const val DATA_SEND = 21u
    const val DATA_BROADCAST = 22u
}

object Proto {
    const val TCP = 6u
    const val UDP = 17u
}

data class Msg(
    var cmd: UInt,
    var proto: UInt? = null,
    var sid: UInt? = null,
    var src: UInt? = null,
    var sPort: UInt? = null,
    var dst: UInt? = null,
    var dPort: UInt? = null,
    var data: String? = null
) {
    override fun toString() = ls(
        1, cmd, proto, sid, src, sPort, dst, dPort,
        null, null, null, null, null, null, null, null,  // reserved for future use
        data
    ).joinToString(",") { it?.str ?: "" }.trimEnd(',')

    companion object {
        val fields = arr(Msg::proto, Msg::sid, Msg::src, Msg::sPort, Msg::dst, Msg::dPort)

        fun fromString(str: String): Msg {
            val sp = str.split(',')
            return Msg(0u).apply {
                cmd = sp[1].toUInt()
                fields.forEachIndexed { i, f -> f.set(this, sp.getOrNull(i + 2)?.some?.toUIntOrNull()) }
                data = sp.drop(16).joinToString(",")
            }
        }
    }
}

@Serializable
data class MechaInfo(
    val IsJoin: Bool,
    val IpAddress: UInt,
    val MusicID: Int,
    val Entrys: List<Bool>,
    var UserIDs: List<Long>,
    val UserNames: List<String>,
    val IconIDs: List<Int>,
    val FumenDifs: List<Int>,
    val Rateing: List<Int>,
    val ClassValue: List<Int>,
    val MaxClassValue: List<Int>,
    val UserType: List<Int>
)

@Serializable
data class RecruitInfo(
    val MechaInfo: MechaInfo,
    val MusicID: Int,
    val GroupID: Int,
    val EventModeID: Boolean,
    val JoinNumber: Int,
    val PartyStance: Int,
    val _startTimeTicks: Long,
    val _recvTimeTicks: Long
)

@Serializable
data class RecruitRecord(
    val RecruitInfo: RecruitInfo,
    val Keychip: String,
    var Server: RelayServerInfo? = null,
    var Time: Long = 0,
)

@Serializable
data class RelayServerInfo(
    val name: String,
    val addr: String,
    val port: Int = 20101,
    val official: Bool = true
)
