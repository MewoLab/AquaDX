package icu.samnyan.aqua.sega.diva.model.db.gamedata

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity(name = "DivaPvLevel")
@Table(name = "diva_pv_info_level")
class Difficulty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long = 0

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pv_id")
    var pv: Pv = Pv()
    var edition = 0
    var level: String = ""
    var version = 0
    var diff: String = ""

    constructor(id: Long, pv: Pv, edition: Int, level: String, version: Int, diff: String) {
        this.id = id
        this.pv = pv
        this.edition = edition
        this.level = level
        this.version = version
        this.diff = diff
    }

    constructor()
}
