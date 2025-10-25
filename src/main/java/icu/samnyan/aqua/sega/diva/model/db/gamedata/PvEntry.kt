package icu.samnyan.aqua.sega.diva.model.db.gamedata

import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaPvEntry")
@Table(name = "diva_pv_entry")
class PvEntry : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    var pvId = 0

    @Enumerated(EnumType.STRING)
    var difficulty: Difficulty = Difficulty.NORMAL
    var version = 0

    @Enumerated(EnumType.STRING)
    var edition: Edition = Edition.ORIGINAL
    var demoStart: LocalDateTime = LocalDateTime.now()
    var demoEnd: LocalDateTime = LocalDateTime.now()
    var playableStart: LocalDateTime = LocalDateTime.now()
    var playableEnd: LocalDateTime = LocalDateTime.now()
}
