package icu.samnyan.aqua.sega.chusan.model.userdata

import icu.samnyan.aqua.sega.general.IntegerListConverter
import jakarta.persistence.Convert
import jakarta.persistence.Entity


@Entity(name = "ChusanUserMisc")
class Chu3UserMisc(
    @Convert(converter = IntegerListConverter::class)
    var recentNbSelect: MutableList<Int> = mutableListOf(),

    @Convert(converter = IntegerListConverter::class)
    var recentNbMusic: MutableList<Int> = mutableListOf(),

    @Convert(converter = IntegerListConverter::class)
    var favMusic: MutableList<Int> = mutableListOf()
): Chu3UserEntity()