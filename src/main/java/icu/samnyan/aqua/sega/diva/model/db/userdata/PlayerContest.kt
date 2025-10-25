package icu.samnyan.aqua.sega.diva.model.db.userdata

import icu.samnyan.aqua.sega.diva.model.common.ContestBorder
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaPlayerContest")
@Table(name = "diva_player_contest", uniqueConstraints = [UniqueConstraint(columnNames = ["pd_id", "contest_id"])])
class PlayerContest : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    var pdId: PlayerProfile = PlayerProfile()

    @Column(name = "contest_id")
    var contestId = 0
    var startCount = 0

    @Enumerated(EnumType.STRING)
    var resultRank: ContestBorder = ContestBorder.NONE
    var bestValue = -1
    var flag = -1
    var lastUpdateTime: LocalDateTime = LocalDateTime.now()

    constructor(pdId: PlayerProfile, contestId: Int) {
        this.pdId = pdId
        this.contestId = contestId
    }

    constructor()
}
