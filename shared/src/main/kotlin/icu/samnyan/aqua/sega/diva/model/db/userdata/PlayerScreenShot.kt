package icu.samnyan.aqua.sega.diva.model.db.userdata

import jakarta.persistence.*

@Entity(name = "DivaPlayerScreenShot")
@Table(name = "diva_player_screen_shot")
class PlayerScreenShot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    var pdId: PlayerProfile = PlayerProfile()
    var pvId: Long = 0
    var fileName: String = ""
    var moduleList: String = ""
    var customizeList: String = ""

    constructor(pdId: PlayerProfile, fileName: String, pvId: Long, moduleList: String, customizeList: String) {
        this.pdId = pdId
        this.fileName = fileName
        this.pvId = pvId
        this.moduleList = moduleList
        this.customizeList = customizeList
    }

    constructor()
}
