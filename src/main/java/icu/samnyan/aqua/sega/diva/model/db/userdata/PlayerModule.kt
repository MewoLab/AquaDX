package icu.samnyan.aqua.sega.diva.model.db.userdata

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable

@Entity(name = "DivaPlayerModule")
@Table(name = "diva_player_module", uniqueConstraints = [UniqueConstraint(columnNames = ["pd_id", "module_id"])])
class PlayerModule : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pd_id")
    var pdId: PlayerProfile = PlayerProfile()

    @Column(name = "module_id")
    var moduleId = 0

    constructor(profile: PlayerProfile, moduleId: Int) {
        this.pdId = profile
        this.moduleId = moduleId
    }

    constructor()
}
