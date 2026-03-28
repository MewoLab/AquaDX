package icu.samnyan.aqua.sega.diva.model.db.userdata

import icu.samnyan.aqua.sega.diva.model.common.StartMode
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaGameSession")
@Table(name = "diva_game_session")
class GameSession : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    var acceptId = 0

    @OneToOne
    @JoinColumn(name = "pd_id", unique = true)
    var pdId: PlayerProfile = PlayerProfile()

    @Enumerated(EnumType.STRING)
    var startMode: StartMode = StartMode.START
    var startTime: LocalDateTime = LocalDateTime.now()
    var lastUpdateTime: LocalDateTime = LocalDateTime.now()
    var stageIndex: Int = 0
    var stageResultIndex: Int = 0
    var lastPvId: Int = 0
    var levelNumber: Int = 0
    var levelExp: Int = 0
    var oldLevelNumber: Int = 0
    var oldLevelExp: Int = 0
    var vp: Int = 0

    constructor(
        acceptId: Int,
        pdId: PlayerProfile,
        startMode: StartMode,
        startTime: LocalDateTime,
        lastUpdateTime: LocalDateTime,
        stageIndex: Int,
        stageResultIndex: Int,
        lastPvId: Int,
        levelNumber: Int,
        levelExp: Int,
        oldLevelNumber: Int,
        oldLevelExp: Int,
        vp: Int
    ) {
        this.acceptId = acceptId
        this.pdId = pdId
        this.startMode = startMode
        this.startTime = startTime
        this.lastUpdateTime = lastUpdateTime
        this.stageIndex = stageIndex
        this.stageResultIndex = stageResultIndex
        this.lastPvId = lastPvId
        this.levelNumber = levelNumber
        this.levelExp = levelExp
        this.oldLevelNumber = oldLevelNumber
        this.oldLevelExp = oldLevelExp
        this.vp = vp
    }

    constructor()
}
