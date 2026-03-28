package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.db.AquaGameOptions
import icu.samnyan.aqua.net.db.AquaGameOptionsRepo
import icu.samnyan.aqua.net.db.AquaNetUserRepo
import icu.samnyan.aqua.net.db.AquaUserServices
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

@RestController
@API("/api/v2/settings")
class SettingsApi(
    val us: AquaUserServices,
    val userRepo: AquaNetUserRepo,
    val goRepo: AquaGameOptionsRepo,
    val fedy: Fedy
) {
    // Get all params with SettingField annotation
    val fields = AquaGameOptions::class.vars()
        .mapNotNull { it.findAnnotation<SettingField>()?.let { an -> it to an } }
    val fieldMap = fields.associate { (f, _) -> f.name to f }
    val fieldDesc = fields.map { (f, _) -> mapOf(
        "key" to f.name, "type" to f.returnType.jvmErasure.simpleName,
        "game" to f.findAnnotation<SettingField>()!!.game,
    ) }

    @API("get")
    @Doc("Get the game options of the logged in user")
    fun getSettings(@RP token: String) = us.jwt.auth(token) { u ->
        val go = u.gameOptions ?: AquaGameOptions()
        fieldDesc.map { it + ("value" to fieldMap[it["key"]]!!.get(go)) }
    }

    @API("set")
    @Doc("Set a field in the game options")
    fun setField(@RP token: String, @RP key: String, @RP value: String) = us.jwt.auth(token) { u ->
        val field = fieldMap[key] ?: (400 - "Invalid field $key")
        val options = u.gameOptions ?: AquaGameOptions().also {
            userRepo.save(u.apply { gameOptions = it })
        }
        // Check field type
        field.setCast(options, value)
        goRepo.save(options).also { fedy.onUserUpdated(u) }
    }
}
