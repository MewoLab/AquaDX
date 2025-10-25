package icu.samnyan.aqua.sega.diva.model.db.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import icu.samnyan.aqua.sega.diva.model.common.ChallengeKind
import icu.samnyan.aqua.sega.diva.model.common.ClearResult
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaPlayLog")
@Table(name = "diva_play_log")
class PlayLog : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @ManyToOne
    @JoinColumn(name = "pd_id")
    @JsonIgnore
    var pdId: PlayerProfile = PlayerProfile()
    var pvId = 0

    @Enumerated(EnumType.STRING)
    var difficulty: Difficulty = Difficulty.NORMAL

    @Enumerated(EnumType.STRING)
    var edition: Edition = Edition.ORIGINAL
    var scriptVer = 0
    var score = 0

    @Enumerated(EnumType.STRING)
    var challengeKind: ChallengeKind = ChallengeKind.UNDEFINED
    var challengeResult = 0

    @Enumerated(EnumType.STRING)
    var clearResult: ClearResult = ClearResult.NO_CLEAR
    var vp = 0
    var coolCount = 0
    var coolPercent = 0
    var fineCount = 0
    var finePercent = 0
    var safeCount = 0
    var safePercent = 0
    var sadCount = 0
    var sadPercent = 0
    var wrongCount = 0
    var wrongPercent = 0
    var maxCombo = 0
    var chanceTime = 0
    var holdScore = 0
    var attainPoint = 0
    var skinId = 0
    var buttonSe = 0
    var buttonSeVol = 0
    var sliderSe = 0
    var ChainSlideSe = 0
    var SliderTouchSe = 0
    var modules: String = ""
    var stageCompletion = 0
    var slideScore = 0
    var isVocalChange = 0
    var customizeItems: String = ""

    //    String customizeItemFlags;
    var rhythmGameOptions: String = ""
    var screenShotCount = -1
    var dateTime: LocalDateTime = LocalDateTime.now()

    constructor(
        pdId: PlayerProfile,
        pvId: Int,
        difficulty: Difficulty,
        edition: Edition,
        scriptVer: Int,
        score: Int,
        challengeKind: ChallengeKind,
        challengeResult: Int,
        clearResult: ClearResult,
        vp: Int,
        coolCount: Int,
        coolPercent: Int,
        fineCount: Int,
        finePercent: Int,
        safeCount: Int,
        safePercent: Int,
        sadCount: Int,
        sadPercent: Int,
        wrongCount: Int,
        wrongPercent: Int,
        maxCombo: Int,
        chanceTime: Int,
        holdScore: Int,
        attainPoint: Int,
        skinId: Int,
        buttonSe: Int,
        buttonSeVol: Int,
        sliderSe: Int,
        chainSlideSe: Int,
        sliderTouchSe: Int,
        modules: String,
        stageCompletion: Int,
        slideScore: Int,
        isVocalChange: Int,
        customizeItems: String,
        rhythmGameOptions: String,
        screenShotCount: Int,
        dateTime: LocalDateTime
    ) {
        this.pdId = pdId
        this.pvId = pvId
        this.difficulty = difficulty
        this.edition = edition
        this.scriptVer = scriptVer
        this.score = score
        this.challengeKind = challengeKind
        this.challengeResult = challengeResult
        this.clearResult = clearResult
        this.vp = vp
        this.coolCount = coolCount
        this.coolPercent = coolPercent
        this.fineCount = fineCount
        this.finePercent = finePercent
        this.safeCount = safeCount
        this.safePercent = safePercent
        this.sadCount = sadCount
        this.sadPercent = sadPercent
        this.wrongCount = wrongCount
        this.wrongPercent = wrongPercent
        this.maxCombo = maxCombo
        this.chanceTime = chanceTime
        this.holdScore = holdScore
        this.attainPoint = attainPoint
        this.skinId = skinId
        this.buttonSe = buttonSe
        this.buttonSeVol = buttonSeVol
        this.sliderSe = sliderSe
        ChainSlideSe = chainSlideSe
        SliderTouchSe = sliderTouchSe
        this.modules = modules
        this.stageCompletion = stageCompletion
        this.slideScore = slideScore
        this.isVocalChange = isVocalChange
        this.customizeItems = customizeItems
        this.rhythmGameOptions = rhythmGameOptions
        this.screenShotCount = screenShotCount
        this.dateTime = dateTime
    }

    constructor()
}
