package icu.samnyan.aqua.sega.diva.model.common.collection

import ext.csv
import icu.samnyan.aqua.sega.diva.model.Internalizable

class ClearSet {
    var clear = 0
    var great = 0
    var excellent = 0
    var perfect = 0

    fun addClear() {
        this.clear += 1
    }

    fun addGreat() {
        this.clear += 1
        this.great += 1
    }

    fun addExcellent() {
        this.clear += 1
        this.great += 1
        this.excellent += 1
    }

    fun addPerfect() {
        this.clear += 1
        this.great += 1
        this.excellent += 1
        this.perfect += 1
    }
}

class ClearTally : Internalizable {
    val easy = ClearSet()
    val normal = ClearSet()
    val hard = ClearSet()
    val extreme = ClearSet()
    val extraExtreme = ClearSet()

    override fun toInternal() = listOf(
        easy.clear, easy.great, easy.excellent, easy.perfect,
        normal.clear, normal.great, normal.excellent, normal.perfect,
        hard.clear, hard.great, hard.excellent, hard.perfect,
        extreme.clear, extreme.great, extreme.excellent, extreme.perfect,
        extraExtreme.clear, extraExtreme.great, extraExtreme.excellent, extraExtreme.perfect
    ).csv
}
