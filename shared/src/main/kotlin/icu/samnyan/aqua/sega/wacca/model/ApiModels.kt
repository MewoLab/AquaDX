package icu.samnyan.aqua.sega.wacca.model

open class BaseRequest {
    var requestNo: Int = 0
    var appVersion: String = ""
    var boardId: String = ""
    var chipId: String = ""
    var params: List<Any> = mutableListOf()
}

