package icu.samnyan.aqua.sega.diva

import icu.samnyan.aqua.sega.diva.model.RegistrationRequest
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerCustomize
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerModule
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerProfile
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.*


@Component
class DivaServices(
    val profile: PlayerProfileService,
    val module: PlayerModuleService,
    val customize: PlayerCustomizeService
)

@Service
class PlayerModuleService(val repo: PlayerModuleRepository) {
    fun buy(profile: PlayerProfile, moduleId: Int) = repo.save(PlayerModule(profile, moduleId))

    fun getModuleHaveString(profile: PlayerProfile): String {
        val moduleList = repo.findByPdId(profile)
        var module_have = BigInteger("0")
        for (module in moduleList) {
            module_have = module_have.or(BigInteger.valueOf(1).shiftLeft(module.moduleId))
        }
        println(module_have.toString(2))
        return StringUtils.leftPad(module_have.toString(16), 250, "0").uppercase(Locale.getDefault())
    }
}

@Service
class PlayerProfileService(val repo: PlayerProfileRepository) {
    fun findByPdId(pdId: Long): Optional<PlayerProfile> = repo.findByPdId(pdId)

    fun register(request: RegistrationRequest): PlayerProfile {
        val profile = PlayerProfile()
        profile.pdId = request.aime_id
        profile.playerName = request.player_name

        return repo.save(profile)
    }

    fun save(profile: PlayerProfile) = repo.save(profile)
}

@Service
class PlayerCustomizeService(val repo: PlayerCustomizeRepository) {
    fun buy(profile: PlayerProfile, customizeId: Int) = repo.save(PlayerCustomize(profile, customizeId))

    fun getModuleHaveString(profile: PlayerProfile): String {
        val customizeList = repo.findByPdId(profile)
        var customize_have = BigInteger("0")
        for (customize in customizeList) {
            customize_have = customize_have.or(BigInteger.valueOf(1).shiftLeft(customize.customizeId))
        }
        return StringUtils.leftPad(customize_have.toString(16), 250, "0")
    }
}