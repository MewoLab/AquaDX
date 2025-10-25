package icu.samnyan.aqua.sega.diva.model.db.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import icu.samnyan.aqua.sega.diva.model.common.*
import icu.samnyan.aqua.sega.diva.util.DivaStringUtils.getDummyString
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "DivaPlayerProfile")
@Table(name = "diva_player_profile")
class PlayerProfile : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(unique = true)
    var pdId: Long = 0
    var playerName: String = "xxx"
    var vocaloidPoints = 300
    var level = 1
    var levelExp = 0
    var levelTitle: String = "xxx"
    var plateId = -1
    var plateEffectId = -1

    @Enumerated(EnumType.STRING)
    var passwordStatus: PassStat = PassStat.MISS

    @JsonIgnore
    var password: String = "**********"

    /**
     * Game play customize
     */
    var preferPerPvModule = true
    var preferCommonModule = false
    var usePerPvSkin = false
    var usePerPvButtonSe = false
    var usePerPvSliderSe = false
    var usePerPvChainSliderSe = false
    var usePerPvTouchSliderSe = false
    var commonModule: String = "-999,-999,-999"
    var commonCustomizeItems: String = "-999,-999,-999,-999,-999,-999,-999,-999,-999,-999,-999,-999"
    var commonModuleSetTime: LocalDateTime = LocalDateTime.now()
    var moduleSelectItemFlag: String = "-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1"

    // -1 is disable
    var commonSkin = -1

    /**
     * Sound Setting
     */
    var headphoneVolume = 100
    var buttonSeOn = true
    var buttonSeVolume = 100
    var sliderSeVolume = 100
    var buttonSe = -1
    var chainSlideSe = -1
    var slideSe = -1
    var sliderTouchSe = -1

    /**
     * View Setting
     */
    @Enumerated(EnumType.STRING)
    var sortMode: SortMode = SortMode.RELEASE_DATE

    @JsonIgnore
    var nextPvId = -1

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    var nextDifficulty: Difficulty = Difficulty.NORMAL

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    var nextEdition: Edition = Edition.ORIGINAL
    var showInterimRanking = true
    var showClearStatus = true
    var showGreatBorder = true
    var showExcellentBorder = true
    var showRivalBorder = true
    var showRgoSetting = true

    @JsonIgnore
    var contestNowPlayingEnable = false

    @JsonIgnore
    var contestNowPlayingId = -1

    // Contest now playing progress
    @JsonIgnore
    var contestNowPlayingValue = -1

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    var contestNowPlayingResultRank: ContestBorder = ContestBorder.NONE

    // This store the current progress of contest
    @JsonIgnore
    var contestNowPlayingSpecifier: String = ""


    /**
     * MyList, comma separate string
     */
    var myList0: String = getDummyString("-1", 40)
    var myList1: String = getDummyString("-1", 40)
    var myList2: String = getDummyString("-1", 40)

    @JsonIgnore
    var rivalPdId: Long = -1

    constructor(pdId: Long, playerName: String) {
        this.pdId = pdId
        this.playerName = playerName
    }

    constructor(
        id: Long,
        pdId: Long,
        playerName: String,
        vocaloidPoints: Int,
        level: Int,
        levelExp: Int,
        levelTitle: String,
        plateId: Int,
        plateEffectId: Int,
        passwordStatus: PassStat,
        password: String,
        preferPerPvModule: Boolean,
        preferCommonModule: Boolean,
        usePerPvSkin: Boolean,
        usePerPvButtonSe: Boolean,
        usePerPvSliderSe: Boolean,
        usePerPvChainSliderSe: Boolean,
        usePerPvTouchSliderSe: Boolean,
        commonModule: String,
        commonCustomizeItems: String,
        commonModuleSetTime: LocalDateTime,
        moduleSelectItemFlag: String,
        commonSkin: Int,
        headphoneVolume: Int,
        buttonSeOn: Boolean,
        buttonSeVolume: Int,
        sliderSeVolume: Int,
        buttonSe: Int,
        chainSlideSe: Int,
        slideSe: Int,
        sliderTouchSe: Int,
        sortMode: SortMode,
        nextPvId: Int,
        nextDifficulty: Difficulty,
        nextEdition: Edition,
        showInterimRanking: Boolean,
        showClearStatus: Boolean,
        showGreatBorder: Boolean,
        showExcellentBorder: Boolean,
        showRivalBorder: Boolean,
        showRgoSetting: Boolean,
        contestNowPlayingEnable: Boolean,
        contestNowPlayingId: Int,
        contestNowPlayingValue: Int,
        contestNowPlayingResultRank: ContestBorder,
        contestNowPlayingSpecifier: String,
        myList0: String,
        myList1: String,
        myList2: String,
        rivalPdId: Long
    ) {
        this.id = id
        this.pdId = pdId
        this.playerName = playerName
        this.vocaloidPoints = vocaloidPoints
        this.level = level
        this.levelExp = levelExp
        this.levelTitle = levelTitle
        this.plateId = plateId
        this.plateEffectId = plateEffectId
        this.passwordStatus = passwordStatus
        this.password = password
        this.preferPerPvModule = preferPerPvModule
        this.preferCommonModule = preferCommonModule
        this.usePerPvSkin = usePerPvSkin
        this.usePerPvButtonSe = usePerPvButtonSe
        this.usePerPvSliderSe = usePerPvSliderSe
        this.usePerPvChainSliderSe = usePerPvChainSliderSe
        this.usePerPvTouchSliderSe = usePerPvTouchSliderSe
        this.commonModule = commonModule
        this.commonCustomizeItems = commonCustomizeItems
        this.commonModuleSetTime = commonModuleSetTime
        this.moduleSelectItemFlag = moduleSelectItemFlag
        this.commonSkin = commonSkin
        this.headphoneVolume = headphoneVolume
        this.buttonSeOn = buttonSeOn
        this.buttonSeVolume = buttonSeVolume
        this.sliderSeVolume = sliderSeVolume
        this.buttonSe = buttonSe
        this.chainSlideSe = chainSlideSe
        this.slideSe = slideSe
        this.sliderTouchSe = sliderTouchSe
        this.sortMode = sortMode
        this.nextPvId = nextPvId
        this.nextDifficulty = nextDifficulty
        this.nextEdition = nextEdition
        this.showInterimRanking = showInterimRanking
        this.showClearStatus = showClearStatus
        this.showGreatBorder = showGreatBorder
        this.showExcellentBorder = showExcellentBorder
        this.showRivalBorder = showRivalBorder
        this.showRgoSetting = showRgoSetting
        this.contestNowPlayingEnable = contestNowPlayingEnable
        this.contestNowPlayingId = contestNowPlayingId
        this.contestNowPlayingValue = contestNowPlayingValue
        this.contestNowPlayingResultRank = contestNowPlayingResultRank
        this.contestNowPlayingSpecifier = contestNowPlayingSpecifier
        this.myList0 = myList0
        this.myList1 = myList1
        this.myList2 = myList2
        this.rivalPdId = rivalPdId
    }

    constructor()
}
