package icu.samnyan.aqua.sega.diva.model.db.gamedata

import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.FestaKind
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaFesta")
@Table(name = "diva_festa")
class Festa : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = -1
    var enable = true
    var name: String = "xxx"

    @Enumerated(EnumType.STRING)
    var kind: FestaKind = FestaKind.PINK_FESTA

    @Enumerated(EnumType.STRING)
    var difficulty: Difficulty = Difficulty.UNDEFINED
    var pvList: String = "ALL"
    var attributes: String = "7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
    var addVP: Int = 0
    var vpMultiplier: Int = 1
    var start: LocalDateTime = LocalDateTime.of(2005, 1, 1, 0, 0)
    var end: LocalDateTime = LocalDateTime.of(2005, 1, 1, 0, 0)
    var createDate: LocalDateTime = LocalDateTime.of(2005, 1, 1, 0, 0)
}
