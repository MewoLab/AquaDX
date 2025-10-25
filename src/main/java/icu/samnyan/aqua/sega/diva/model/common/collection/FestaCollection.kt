package icu.samnyan.aqua.sega.diva.model.common.collection

import icu.samnyan.aqua.sega.diva.model.db.gamedata.Festa
import icu.samnyan.aqua.sega.diva.util.DivaTime.getString
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode

class FestaCollection {
    var firstFesta = Festa()
    var secondFesta = Festa()

    constructor(festas: MutableList<Festa>) {
        if (festas.size >= 2) {
            this.firstFesta = festas.get(0)
            this.secondFesta = festas.get(1)
        } else if (festas.size == 1) {
            this.firstFesta = festas.get(0)
        }
    }

    val ids: String
        get() = this.firstFesta.id.toString() + "," + this.secondFesta.id

    val names: String
        get() = encode(this.firstFesta.name) + "," + encode(this.secondFesta.name)

    val kinds: String
        get() = this.firstFesta.kind.value.toString() + "," + this.secondFesta.kind.value

    val diffs: String
        get() = this.firstFesta.difficulty.value.toString() + "," + this.secondFesta.difficulty.value

    val pvIds: String
        get() = this.firstFesta.pvList + "," + this.secondFesta.pvList

    val attr: String
        get() = this.firstFesta.attributes + "," + this.secondFesta.attributes

    val addVps: String
        get() = this.firstFesta.addVP.toString() + "," + this.secondFesta.addVP

    val vpMultipliers: String
        get() = this.firstFesta.vpMultiplier.toString() + "," + this.secondFesta.vpMultiplier

    val starts: String
        get() = getString(this.firstFesta.start) + "," + getString(this.secondFesta.start)

    val ends: String
        get() = getString(this.firstFesta.end) + "," + getString(this.secondFesta.end)

    val lastUpdateTime: String
        get() = getString(if (this.firstFesta.createDate.isBefore(this.secondFesta.createDate)) this.firstFesta.createDate else this.secondFesta.createDate)
}
