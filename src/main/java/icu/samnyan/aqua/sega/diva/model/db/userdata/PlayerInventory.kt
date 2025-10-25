package icu.samnyan.aqua.sega.diva.model.db.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "DivaPlayerInventory")
@Table(name = "diva_player_inventory", uniqueConstraints = [UniqueConstraint(columnNames = ["pd_id", "value", "type"])])
class PlayerInventory : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    @JsonIgnore
    var pdId: PlayerProfile = PlayerProfile()
    var value: String = ""

    // Type: (1: Skin, 2: Call sign plate, 3: Call sign)
    var type: String = ""

    constructor(pdId: PlayerProfile, value: String, type: String) {
        this.pdId = pdId
        this.value = value
        this.type = type
    }

    constructor()
}
