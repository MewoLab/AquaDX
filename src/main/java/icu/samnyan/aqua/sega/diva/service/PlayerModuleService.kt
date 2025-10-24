package icu.samnyan.aqua.sega.diva.service

import icu.samnyan.aqua.sega.diva.PlayerModuleRepository
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerModule
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.*

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
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
