package test

import ext.JACKSON
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserCard
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserPrintDetail
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Mai2UserPrintDetailJsonTest : StringSpec({
    "userCard is accepted on input but omitted from output" {
        val detail = Mai2UserPrintDetail().apply {
            userCard = Mai2UserCard().apply { cardId = 42 }
        }

        JACKSON.writeValueAsString(detail).contains("\"userCard\"") shouldBe false

        val imported = JACKSON.readValue(
            """{"userCard":{"cardId":42}}""",
            Mai2UserPrintDetail::class.java,
        )
        imported.userCard?.cardId shouldBe 42
    }
})
