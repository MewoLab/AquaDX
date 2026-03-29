package icu.samnyan.aqua.sega.diva.model.db.userdata

import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "DivaPlayerCustomize")
@Table(name = "diva_player_customize", uniqueConstraints = [UniqueConstraint(columnNames = ["pd_id", "customize_id"])])
class PlayerCustomize : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    var pdId: PlayerProfile = PlayerProfile()

    @Column(name = "customize_id")
    var customizeId = 0

    constructor(profile: PlayerProfile, customizeId: Int) {
        this.pdId = profile
        this.customizeId = customizeId
    }

    constructor()
}
