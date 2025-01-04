package icu.samnyan.aqua.sega.chusan.handler

import ext.int
import ext.invoke
import ext.long
import icu.samnyan.aqua.sega.chusan.ChusanProps
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import icu.samnyan.aqua.sega.chusan.model.userdata.UserItem
import icu.samnyan.aqua.sega.chusan.model.userdata.UserLoginBonus
import icu.samnyan.aqua.sega.general.BaseHandler
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@AllArgsConstructor
@Component("ChusanGameLoginHandler")
class GameLoginHandler(
    val props: ChusanProps,
    val db: Chu3Repos
) : BaseHandler {

    override fun handle(request: Map<String, Any>): Any? {
        val uid = request["userId"]!!.long
        fun process() {
            val u = db.userData.findByCard_ExtId(uid)() ?: return
            db.userData.save(u.apply { lastLoginDate = LocalDateTime.now() })

            if (!props.loginBonusEnable) return
            val bonusList = db.gameLoginBonusPresets.findLoginBonusPresets(1, 1)

            bonusList.forEach { preset ->
                // Check if a user already has some progress and if not, add the login bonus entry
                val bonus = db.userLoginBonus.findLoginBonus(uid.int, 1, preset.id)()
                    ?: UserLoginBonus(1, uid.int, preset.id).let { db.userLoginBonus.save(it) }
                if (bonus.isFinished) return@forEach

                // last login is 24 hours+ ago
                if (bonus.lastUpdateDate.toEpochSecond(ZoneOffset.ofHours(0)) <
                    (LocalDateTime.now().minusHours(24).toEpochSecond(ZoneOffset.ofHours(0)))
                ) {
                    var bCount = bonus.bonusCount + 1
                    val lastUpdate = LocalDateTime.now()
                    val allLoginBonus = db.gameLoginBonus.findGameLoginBonus(1, preset.id)
                        .ifEmpty { return@forEach }
                    val maxNeededDays = allLoginBonus[0].needLoginDayCount

                    // if all items are redeemed, then don't show the login bonuses.
                    var finished = false
                    if (bCount > maxNeededDays) {
                        if (preset.id < 3000) bCount = 1
                        else finished = true
                    }
                    db.gameLoginBonus.findByRequiredDays(1, preset.id, bCount)()?.let {
                        db.userItem.save(UserItem(6, it.presentId, it.itemNum).apply { user = u })
                    }
                    val toSave = db.userLoginBonus.findLoginBonus(uid.int, 1, preset.id)()
                        ?: UserLoginBonus().apply { user = uid.int; presetId = preset.id; version = 1 }

                    db.userLoginBonus.save(toSave.apply {
                        bonusCount = bCount
                        lastUpdateDate = lastUpdate
                        isWatched = false
                        isFinished = finished
                    })
                }
            }
        }
        process()

        return """{"returnCode":"1"}"""
    }
}
