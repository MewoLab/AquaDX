package icu.samnyan.aqua.sega.chusan.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import icu.samnyan.aqua.sega.chusan.model.GameGachaCard
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData
import icu.samnyan.aqua.sega.chusan.model.userdata.UserGacha
import icu.samnyan.aqua.sega.chusan.model.userdata.UserItem
import java.io.Serializable

class UpsertUserGacha : Serializable {
    var userData: Chu3UserData? = null
    var userGacha: UserGacha? = null
    var userCharacterList: List<Any>? = null
    var userCardList: List<Any>? = null
    var gameGachaCardList: List<GameGachaCard>? = null
    var userItemList: List<UserItem>? = null

    @JsonProperty("isNewCharacterList")
    var isNewCharacterList: String? = null
    @JsonProperty("isNewCardList")
    var isNewCardList: String? = null
}
