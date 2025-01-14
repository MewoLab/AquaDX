package icu.samnyan.aqua.sega.maimai2.worldslink

import ext.arr
import ext.ls
import ext.some
import ext.str

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