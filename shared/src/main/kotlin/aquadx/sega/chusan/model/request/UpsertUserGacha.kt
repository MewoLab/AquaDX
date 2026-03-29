package aquadx.sega.chusan.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import aquadx.sega.chusan.model.GameGachaCard
import aquadx.sega.chusan.model.userdata.Chu3UserData
import aquadx.sega.chusan.model.userdata.Chu3UserItem
import aquadx.sega.chusan.model.userdata.UserGacha
import java.io.Serializable

class UpsertUserGacha : Serializable {
    var userData: Chu3UserData? = null
    var userGacha: UserGacha? = null
    var userCharacterList: List<Any>? = null
    var userCardList: List<Any>? = null
    var gameGachaCardList: List<GameGachaCard>? = null
    var userItemList: List<Chu3UserItem>? = null

    @JsonProperty("isNewCharacterList")
    var isNewCharacterList: String? = null
    @JsonProperty("isNewCardList")
    var isNewCardList: String? = null
}
