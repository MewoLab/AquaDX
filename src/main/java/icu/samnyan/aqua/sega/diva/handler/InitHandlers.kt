package icu.samnyan.aqua.sega.diva.handler

import icu.samnyan.aqua.sega.diva.model.common.attend.DispersalParameter
import icu.samnyan.aqua.sega.diva.model.common.attend.EtcParameter
import icu.samnyan.aqua.sega.diva.model.common.attend.GameBalanceParameter
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.boot.AttendResponse
import icu.samnyan.aqua.sega.diva.model.response.boot.GameInitResponse
import icu.samnyan.aqua.sega.diva.model.response.operation.PingResponse
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository
import icu.samnyan.aqua.sega.general.model.PropertyEntry
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GameInitHandler {
    fun handle(request: BaseRequest) = GameInitResponse(
        "0,0",
        "FFFF"
    )
}

@Component
class AttendHandler {
    fun handle(request: BaseRequest) = AttendResponse(
        EtcParameter().toInternal(),
        DispersalParameter().toInternal(),
        GameBalanceParameter().toInternal(),
        LocalDateTime.now()
    )
}

@Component
class PingHandler(val rp: PropertyEntryRepository) {
    fun handle(request: BaseRequest): Any {
        val news: PropertyEntry = rp.findByPropertyKey("diva_news") ?: PropertyEntry("diva_news", "xxx")
        val warning: PropertyEntry = rp.findByPropertyKey("diva_warning") ?: PropertyEntry("diva_warning", "xxx")

        return PingResponse(
            news.propertyValue,
            warning.propertyValue
        )
    }
}