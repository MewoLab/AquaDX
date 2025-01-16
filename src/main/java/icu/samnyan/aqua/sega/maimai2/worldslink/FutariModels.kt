@file:Suppress("PropertyName")

package icu.samnyan.aqua.sega.maimai2.worldslink

import ext.Bool
import kotlinx.serialization.Serializable

@Serializable
data class MechaInfo(
    val IsJoin: Bool,
    val IpAddress: UInt,
    val MusicID: Int,
    val Entrys: List<Bool>,
    val UserIDs: List<Long>,
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
data class StartRecruit(
    val RecruitInfo: RecruitInfo,
)
