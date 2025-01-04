@file:Suppress("UNCHECKED_CAST")

package icu.samnyan.aqua.sega.chusan

import ext.JDict
import ext.int
import ext.millis
import ext.parsing
import icu.samnyan.aqua.sega.chusan.model.request.MatchingWaitState
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3MatchingMemberReq
import kotlin.collections.MutableList
import kotlin.collections.find
import kotlin.collections.indices
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.mapOf
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set


fun ChusanController.matchingApiInit() {
    if (props.externalMatching.isNullOrBlank()) serverOnlyMatching()
}

/**
 * Matching implementation that matches you to players in this server only (not tested very well)
 */
fun ChusanController.serverOnlyMatching() {
    // Matching
    data class MatchingRoom(val members: MutableList<Chu3MatchingMemberReq>, val startTime: Long)
    val matchingRooms = mutableMapOf<Int, MatchingRoom>()
    var matchingLast = 0
    val matchingTime = 120  // Seconds

    "BeginMatching" {
        val memberInfo = parsing { mapper.convert<Chu3MatchingMemberReq>(data["matchingMemberInfo"] as JDict) }

        // Check if there are any room available with less than 4 members and not started
        var id = matchingRooms.entries.find { it.value.members.size < 4 && it.value.startTime == 0L }?.key
        if (id == null) {
            matchingLast += 1
            id = matchingLast
            matchingRooms[id] = MatchingRoom(mutableListOf(memberInfo), millis())
        }

        mapOf("roomId" to id, "matchingWaitState" to MatchingWaitState(listOf(memberInfo)))
    }

    "GetMatchingState" api@ {
        val roomId = parsing { data["roomId"]!!.int }
        val room = matchingRooms[roomId] ?: return@api null
        val dt = matchingTime - (millis() - room.startTime) / 1000
        val ended = room.members.size == 4 || dt <= 0

        mapOf("roomId" to roomId, "matchingWaitState" to MatchingWaitState(room.members, ended, dt.int, 1))
    }

    "EndMatching" api@ {
        val roomId = parsing { data["roomId"]!!.int }
        val room = matchingRooms[roomId] ?: return@api null
        mapOf(
            "matchingMemberInfoList" to room.members,
            "matchingMemberRoleList" to room.members.indices.map { mapOf("role" to it) },
            "matchingResult" to 1,
            "reflectorUri" to props.reflectorUrl
        )
    }
}
