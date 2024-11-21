package icu.samnyan.aqua.sega.wacca.model.db

import ext.isoDate
import ext.ls
import ext.sec
import ext.toDate
import icu.samnyan.aqua.net.games.BaseEntity
import icu.samnyan.aqua.net.games.IUserData
import icu.samnyan.aqua.sega.general.IntegerListConverter
import icu.samnyan.aqua.sega.general.model.Card
import jakarta.persistence.*
import java.util.*

/**
 * General user information
 */
@Entity @Table(name = "wacca_user")
class WaccaUser : BaseEntity(), IUserData {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aime_card_id", unique = true)
    override var card: Card? = Card()

    @Column(length = 8)
    override var userName = ""

    var xp = 0
    var wp = 500
    var wpTotal = 500
    var wpSpent = 0
    var danType = 0
    var danLevel = 0
    @Convert(converter = IntegerListConverter::class)
    var titles: MutableList<Int> = mutableListOf(0, 0, 0)
    override var playerRating = 0
    override var highestRating = 0
    @Temporal(TemporalType.TIMESTAMP)
    var vipExpireTime: Date = Date(0)
    var alwaysVip = false
    var loginCount = 0
    var loginCountDays = 0
    var loginCountDaysConsec = 0
    var loginCountToday = 0
    @Convert(converter = IntegerListConverter::class)
    var playCounts: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0)
    @Convert(converter = IntegerListConverter::class)
    var friendViews: MutableList<Int> = mutableListOf(0, 0, 0)
    @Column(length = 50)
    override var lastClientId = ""
    @Column(length = 50)
    override var lastRomVersion = "1.0.0"
    @Convert(converter = IntegerListConverter::class)
    var lastSongInfo: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0)
    @Temporal(TemporalType.TIMESTAMP)
    var lastConsecDate: Date = Date(0)
    @Temporal(TemporalType.TIMESTAMP)
    override var lastPlayDate: Date = Date()
    @Temporal(TemporalType.TIMESTAMP)
    override var firstPlayDate: Date = Date()
    var gateTutorialFlags: String = "[[1, 0], [2, 0], [3, 0], [4, 0], [5, 0]]"
    @Convert(converter = IntegerListConverter::class)
    var favoriteSongs: MutableList<Int> = mutableListOf()
    override var totalScore = 0L

    fun lStatus() = ls(card?.extId,
        userName, 1, xp, danLevel, danType, moddedWp, ls(0, 0, 0), loginCount, loginCountDays,
        (loginCount - 1).coerceAtLeast(0), loginCountDaysConsec, moddedVipExpire.sec, loginCountToday, this.playerRating
    )

    val moddedVipExpire get() =
        if (card?.aquaUser?.gameOptions?.waccaAlwaysVip == true) "2077-01-01".isoDate().toDate()
        else vipExpireTime

    val moddedWp get() = if (card?.aquaUser?.gameOptions?.waccaInfiniteWp == true) 999999 else wp
}