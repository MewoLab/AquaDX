package icu.samnyan.aqua.sega.diva.service

import icu.samnyan.aqua.sega.diva.PlayerProfileRepository
import icu.samnyan.aqua.sega.diva.model.request.card.RegistrationRequest
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
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
