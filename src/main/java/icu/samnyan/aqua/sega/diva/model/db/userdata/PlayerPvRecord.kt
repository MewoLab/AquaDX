package icu.samnyan.aqua.sega.diva.model.db.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import icu.samnyan.aqua.sega.diva.model.common.ChallengeKind
import icu.samnyan.aqua.sega.diva.model.common.ClearResult
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "DivaPlayerPvRecord")
@Table(
    name = "diva_player_pv_record",
    uniqueConstraints = [UniqueConstraint(columnNames = ["pd_id", "pv_id", "edition", "difficulty"])]
)
class PlayerPvRecord : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    @JsonIgnore
    var pdId: PlayerProfile = PlayerProfile()

    @Column(name = "pv_id")
    var pvId = -1

    @Enumerated(EnumType.STRING)
    var edition: Edition = Edition.ORIGINAL

    @Enumerated(EnumType.STRING)
    var difficulty: Difficulty = Difficulty.NORMAL

    @Enumerated(EnumType.STRING)
    var result: ClearResult = ClearResult.NO_CLEAR
    var maxScore = -1
    var maxAttain = -1

    @Enumerated(EnumType.STRING)
    var challengeKind: ChallengeKind = ChallengeKind.UNDEFINED
    var rgoPurchased: String = "0,0,0"
    var rgoPlayed: String = "0,0,0"

    constructor(pvId: Int, edition: Edition) {
        this.pvId = pvId
        this.edition = edition
    }

    constructor(pdId: PlayerProfile, pvId: Int, edition: Edition, difficulty: Difficulty) {
        this.pdId = pdId
        this.pvId = pvId
        this.edition = edition
        this.difficulty = difficulty
    }

    constructor()
}
