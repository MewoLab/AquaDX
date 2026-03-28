package icu.samnyan.aqua.sega.diva.model.db.gamedata

import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "DivaPvInfo")
@Table(name = "diva_pv_info")
class Pv : Serializable {
    @Id
    var pvId: Int = 0
    var bpm: Int = 0
    var songName: String = ""
    var songNameEng: String = ""
    var songNameReading: String = ""
    var arranger: String = ""
    var lyrics: String = ""
    var music: String = ""
    var performerNumber: Int = 0

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "pv")
    @MapKey(name = "diff")
    var difficulty: MutableMap<String, Difficulty> = mutableMapOf()
}
