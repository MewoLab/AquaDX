package icu.samnyan.aqua.sega.maimai2.model.request

class Mai2UserPhoto {
    var orderId = 0
    var userId: Long = 0
    var divNumber = 0
    var divLength = 0
    var divData: String? = null
    var placeId = 0
    var clientId: String? = null
    var uploadDate: String? = null
    var playlogId: Long = 0
    var trackNo = 0
}

class Mai2UserPortrait {
    var userId: Long = 0
    var divNumber = 0
    var divLength = 0
    var divData: String? = null
    var placeId = 0
    var clientId: String = ""
    var uploadDate: String = "1970-01-01 09:00:00.0"
    var fileName: String = "portrait.jpg"
}