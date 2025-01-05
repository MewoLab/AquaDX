package icu.samnyan.aqua.sega.chusan.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3MatchingMemberReq

class MatchingWaitState(
    var matchingMemberInfoList: List<Chu3MatchingMemberReq> = listOf(),

    @JsonProperty("isFinish")
    var isFinish: Boolean = false,
    var restMSec: Int = 120,
    var pollingInterval: Int = 1,
)
