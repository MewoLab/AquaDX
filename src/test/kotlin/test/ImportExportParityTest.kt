package test

import ext.JACKSON
import ext.parseJackson
import icu.samnyan.aqua.net.games.chu3.Chu3DataExport
import icu.samnyan.aqua.net.games.mai2.Maimai2DataExport
import icu.samnyan.aqua.net.games.ongeki.OngekiDataExport
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserLinkedVerse
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCharge
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCourse
import icu.samnyan.aqua.sega.chusan.model.userdata.UserDuel
import icu.samnyan.aqua.sega.chusan.model.userdata.UserGacha
import icu.samnyan.aqua.sega.chusan.model.userdata.UserLoginBonus
import icu.samnyan.aqua.sega.chusan.model.userdata.UserPlaylog
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserKaleidx
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ImportExportParityTest : StringSpec({
    val timestamp = LocalDateTime.of(2026, 8, 4, 1, 2, 3, 123_456_000)

    "CHUNITHM export JSON can be imported without changing data" {
        val data = Chu3DataExport().apply {
            userData.eventWatchedDate = timestamp
            userData.firstPlayDate = timestamp
            userData.lastPlayDate = timestamp
            userChargeList = listOf(UserCharge().apply {
                purchaseDate = timestamp
                validDate = timestamp
                paramDate = timestamp
            })
            userCourseList = listOf(UserCourse().apply { lastPlayDate = timestamp })
            userDuelList = listOf(UserDuel().apply { lastPlayDate = timestamp })
            userPlaylogList = listOf(UserPlaylog().apply {
                playDate = timestamp
                userPlayDate = timestamp
            })
            userGachaList = listOf(UserGacha().apply { dailyGachaDate = timestamp })
            userLinkedVerseList = listOf(Chu3UserLinkedVerse().apply { clearDate = timestamp })
            userLoginBonusList = listOf(UserLoginBonus(lastUpdateDate = timestamp))
        }

        assertJsonRoundTrip(data)
        val tree = JACKSON.readTree(JACKSON.writeValueAsString(data))
        tree.at("/userData/firstPlayDate").isTextual shouldBe true
        tree.at("/userChargeList/0/purchaseDate").isTextual shouldBe true
    }

    "maimai DX export JSON can be imported without changing data" {
        val data = Maimai2DataExport().apply {
            userKaleidxScopeList = listOf(Mai2UserKaleidx().apply {
                bestAchievementDate = timestamp
                bestDeluxscoreDate = timestamp
                clearDate = timestamp
                lastPlayDate = timestamp
            })
        }

        assertJsonRoundTrip(data)
        JACKSON.readTree(JACKSON.writeValueAsString(data))
            .at("/userKaleidxScopeList/0/lastPlayDate").isTextual shouldBe true
    }

    "ONGEKI export JSON can be imported without changing data" {
        assertJsonRoundTrip(OngekiDataExport())
    }
})

private inline fun <reified T : Any> assertJsonRoundTrip(value: T) {
    val exportedJson = JACKSON.writeValueAsString(value)
    val imported = exportedJson.parseJackson(T::class.java)
    JACKSON.readTree(JACKSON.writeValueAsString(imported)) shouldBe JACKSON.readTree(exportedJson)
}
