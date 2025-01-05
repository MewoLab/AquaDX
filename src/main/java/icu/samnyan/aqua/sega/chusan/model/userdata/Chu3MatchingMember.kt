package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty

open class Chu3MatchingMember {
    var userId: Long = 0
    var regionId = 0
    var placeId = 0
    var userName: String = ""
    var playerRating = 0
    var battleRankId = 0
    var battleCorrection = 0
    var battleRatingAvg = 0
    var optRatingId = 0
    var ratingEffectColorId = 0
    var trophyId = 0
    var nameplateId = 0
    var emblemMedal = 0
    var emblemBase = 0
    var characterId = 0
    var characterRank = 0
    var skillId = 0
    var skillLv = 0
    var skillIdForChara = 0

    @JsonProperty("isJoinTeam")
    var isJoinTeam = false
    var teamName: String? = null
    var teamRank = 0
    var messageId = 0
    var clientId: String? = null
    var romVersion: String? = null
    var dataVersion: String? = null
    var errCnt = 0
    var hostErrCnt = 0
    var joinTime = 0
}

class AvatarEquip {
    var wearID = 0
    var headID = 0
    var faceID = 0
    var skinID = 0
    var itemID = 0
    var frontID = 0
    var backID = 0
}

class GenreGraph {
    var genreId = 0
    var musicCount = 0
}

class Chu3MatchingMemberReq : Chu3MatchingMember() {
    var avatarEquip: AvatarEquip? = null
    var genreGraphList: List<GenreGraph>? = null
}
