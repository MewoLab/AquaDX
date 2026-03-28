package icu.samnyan.aqua.sega.diva.handler

import ext.csv
import icu.samnyan.aqua.sega.diva.model.BaseRequest
import icu.samnyan.aqua.sega.diva.model.PingResponse
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository
import icu.samnyan.aqua.sega.general.model.PropertyEntry
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

fun gameBalanceParameter(): String {
    val name_change_price = 100
    // Trial bids: clear.pay, clear.win, great.pay, great.win, excellent.pay, excellent.win, perfect.pay, perfect.win
    val easy_trials = listOf(5, 10, 10, 25, 20, 50, 30, 90)
    val normal_trials = listOf(5, 10, 10, 25, 20, 50, 30, 90)
    val hard_trials = listOf(5, 10, 10, 25, 20, 50, 30, 90)
    val extreme_trials = listOf(5, 10, 10, 25, 20, 50, 30, 90)
    val extra_extreme_trials = listOf(5, 10, 10, 25, 20, 50, 30, 90)

    return (listOf(name_change_price, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 1, 1, 1, 3, 4, 5, 1, 1, 1, 4, 5, 6, 1, 1, 1, 5, 6, 7, 4, 4, 4, 9, 10, 14) +
        easy_trials + normal_trials + hard_trials +
        extreme_trials + extra_extreme_trials + listOf(10, 30) + Collections.nCopies(100, 0)).take(100).csv
}

fun EtcParameter(): String {
    val module_shop_close = false
    val card_reissue_close = true
    val card_renewal_close = true
    val reset_passwd_close = true
    val change_passwd_close = false
    val change_name_close = false
    val encore_mode_close = true
    val third_stg_mode_close = false
    val slow_down_threshold = 0
    val log_write_flag = false
    val daily_quest_close = true
    val weekly_quest_close = true
    val special_quest_close = true
    val nppg_close = false

    val list = listOf(module_shop_close, card_reissue_close, card_renewal_close, reset_passwd_close,
        change_passwd_close, change_name_close, encore_mode_close, third_stg_mode_close,
        slow_down_threshold, log_write_flag, daily_quest_close, weekly_quest_close,
        special_quest_close, nppg_close) + Collections.nCopies(100, 0)
    return list.take(100).map {
        if (it is Boolean) if (it) "1" else "0"
        else if (it is Int) it.toString()
        else "0"
    }.csv
}

@Component
class AttendHandler {
    fun handle() = mapOf(
        "atnd_prm1" to EtcParameter(),
        "atnd_prm2" to mapOf(
            "max_pd_items" to 30, "" to 1, "max_ps_rankings" to 100,
            "max_screenshots" to 2, "ss_upload_delay" to 1, "" to 1
        ).values.plus(Collections.nCopies(100, 0)).take(100).csv,
        "atnd_prm3" to gameBalanceParameter(),
        "atnd_lut" to LocalDateTime.now()
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