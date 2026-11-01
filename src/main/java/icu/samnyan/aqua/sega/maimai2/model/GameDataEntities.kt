package icu.samnyan.aqua.sega.maimai2.model

import com.fasterxml.jackson.annotation.JsonIgnore

class Mai2GameEvent {
    @JsonIgnore(false)
    var id = 0L

    var type = 0
    var startDate: String? = "2019-01-01 00:00:00.000000"
    var endDate: String? = "2029-01-01 00:00:00.000000"
    var disableArea = ""
}

class Mai2GameCharge {
    var chargeId = 0L
    var orderId = 0L
    var price = 0
    var startDate: String? = "2019-01-01 00:00:00.000000"
    var endDate: String? = "2029-01-01 00:00:00.000000"
}

class Mai2GamePassCharge {
    var orderId: Long = 0
    var passTypeId = 0
    var price = 0
    var startDate: String? = "2019-01-01 00:00:00.000000"
    var endDate: String? = "2099-01-01 00:00:00.000000"
}

class Mai2GameSellingPassPack {
    var passPackId = 0
    var startDate: String? = "2019-01-01 00:00:00.000000"
    var endDate: String? = "2099-01-01 00:00:00.000000"
    var noticeStartDate: String? = "2019-01-01 00:00:00.000000"
    var noticeEndDate: String? = "2099-01-01 00:00:00.000000"
}

class Mai2GameSellingCard {
    var cardId = 0L
    var startDate: String? = "2019-01-01 00:00:00.000000"
    var endDate: String? = "2029-01-01 00:00:00.000000"
    var noticeStartDate: String? = "2019-01-01 00:00:00.000000"
    var noticeEndDate: String? = "2029-01-01 00:00:00.000000"
}
