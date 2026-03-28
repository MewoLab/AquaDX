package icu.samnyan.aqua.sega.diva.model.db.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "DivaPlayerPvCustomize")
@Table(name = "diva_player_pv_customize", uniqueConstraints = [UniqueConstraint(columnNames = ["pd_id", "pv_id"])])
class PlayerPvCustomize : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    @JsonIgnore
    var pdId: PlayerProfile = PlayerProfile()

    @Column(name = "pv_id")
    var pvId = -1
    var module: String = "-999,-999,-999"
    var customize: String = "-999,-999,-999,-999,-999,-999,-999,-999,-999,-999,-999,-999"
    var customizeFlag: String = "-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1"
    var skin = -1
    var buttonSe = -1
    var slideSe = -1
    var chainSlideSe = -1
    var sliderTouchSe = -1

    constructor(pdId: PlayerProfile, pvId: Int) {
        this.pdId = pdId
        this.pvId = pvId
    }

    constructor()
}
