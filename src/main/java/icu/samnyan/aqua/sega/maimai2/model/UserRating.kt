package icu.samnyan.aqua.sega.maimai2.model

import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserRate
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserUdemae

class UserRating {
    var rating = 0
    var ratingList: List<Mai2UserRate> = emptyList()
    var newRatingList: List<Mai2UserRate> = emptyList()
    var nextRatingList: List<Mai2UserRate> = emptyList()
    var nextNewRatingList: List<Mai2UserRate> = emptyList()
    var udemae: Mai2UserUdemae = Mai2UserUdemae()
}
