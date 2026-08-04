package test

import com.fasterxml.jackson.databind.JsonMappingException
import ext.JACKSON
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCMissionProgress
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class Chu3ImportJsonTest : StringSpec({
    "mission progress preserves mission IDs with the same order" {
        val progress = listOf(
            UserCMissionProgress().apply {
                missionId = 100
                order = 0
            },
            UserCMissionProgress().apply {
                missionId = 200
                order = 0
            },
        )

        val json = JACKSON.writeValueAsString(progress)
        val imported = JACKSON.readerForListOf(UserCMissionProgress::class.java).readValue<List<UserCMissionProgress>>(json)

        imported.map { it.missionId to it.order } shouldContainExactly listOf(100 to 0, 200 to 0)
    }

    "CHUNITHM dates accept ISO and database formats" {
        val imported = JACKSON.readValue(
            """{
                "eventWatchedDate":"2026-08-04T01:02:03",
                "firstPlayDate":"2026-08-04 01:02:03.123456",
                "lastPlayDate":"2026-08-04 01:02:03.0"
            }""",
            Chu3UserData::class.java,
        )

        imported.eventWatchedDate shouldBe LocalDateTime.of(2026, 8, 4, 1, 2, 3)
        imported.firstPlayDate shouldBe LocalDateTime.of(2026, 8, 4, 1, 2, 3, 123456000)
        imported.lastPlayDate shouldBe LocalDateTime.of(2026, 8, 4, 1, 2, 3)
    }

    "invalid CHUNITHM dates are rejected with a useful message" {
        val error = shouldThrow<JsonMappingException> {
            JACKSON.readValue(
                """{"firstPlayDate":"573-573-573T22:49:23"}""",
                Chu3UserData::class.java,
            )
        }

        error.originalMessage shouldBe
            "Cannot deserialize value of type `java.time.LocalDateTime` from String \"573-573-573T22:49:23\": Invalid date-time; expected yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd HH:mm:ss with an optional fractional second"
    }
})
