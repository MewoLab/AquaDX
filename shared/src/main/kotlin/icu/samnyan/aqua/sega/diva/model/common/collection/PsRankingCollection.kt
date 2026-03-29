package icu.samnyan.aqua.sega.diva.model.common.collection

import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerPvRecord


class PsRankingCollection(pvId: Int, edition: Edition, list: MutableList<PlayerPvRecord>) {
    var first: PlayerPvRecord
    var second: PlayerPvRecord
    var third: PlayerPvRecord

    init {
        this.first = PlayerPvRecord(pvId, edition)
        this.second = PlayerPvRecord(pvId, edition)
        this.third = PlayerPvRecord(pvId, edition)
        if (list.size >= 1) {
            this.first = list[0]
        }
        if (list.size >= 2) {
            this.second = list.get(1)
        }
        if (list.size >= 3) {
            this.third = list.get(2)
        }
    }
}
