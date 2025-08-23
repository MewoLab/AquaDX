package icu.samnyan.aqua.sega.ongeki.model

class OngekiCMUpsertUserGacha {
    var userData: List<UserData>? = null
    var userCharacterList: List<UserCharacter>? = null
    var userCardList: List<UserCard>? = null
    var gameGachaCardList: List<GameGachaCard>? = null
    var userItemList: List<UserItem>? = null

    // These are strings with as many 1s as new elements are present
    var isNewCharacterList: String? = null
    var isNewCardList: String? = null
    var isNewItemList: String? = null
}

class OngekiCMUpsertAll {
    var userData: List<UserData>? = null
    var userActivityList: List<UserActivity>? = null
    var userCharacterList: List<UserCharacter>? = null
    var userCardList: List<UserCard>? = null
    var userItemList: List<UserItem>? = null

    // These are strings with as many 1s as new elements are given
    var isNewCharacterList: String? = null
    var isNewCardList: String? = null
    var isNewItemList: String? = null
}

class OngekiCMUpsertUserSelectGacha {
    var userData: List<UserData>? = null
    var userCharacterList: List<UserCharacter>? = null
    var userCardList: List<UserCard>? = null

    // These are strings with as many 1s as new elements are given
    var isNewCharacterList: String? = null
    var isNewCardList: String? = null
}

class OngekiCMSelectGachaLog {
    var gachaId: Long = 0
    var useSelectPoint: Int = 0
    var convertType: Int = 0
    var convertItem: Int = 0
}