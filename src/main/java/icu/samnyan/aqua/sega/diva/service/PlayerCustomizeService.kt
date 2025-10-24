package icu.samnyan.aqua.sega.diva.service

import icu.samnyan.aqua.sega.diva.PlayerCustomizeRepository
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerCustomize
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import java.math.BigInteger

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
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
