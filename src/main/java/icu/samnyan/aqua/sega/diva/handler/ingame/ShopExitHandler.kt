package icu.samnyan.aqua.sega.diva.handler.ingame

import ext.csv
import icu.samnyan.aqua.sega.diva.PlayerPvCustomizeRepository
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.ShopExitRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.ShopExitResponse
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvCustomize
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ShopExitHandler(
    private val playerProfileService: PlayerProfileService,
    private val pvCustomizeRepository: PlayerPvCustomizeRepository
) {
    fun handle(request: ShopExitRequest): Any {
        val profile = playerProfileService.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })
        val customize = pvCustomizeRepository.findByPdIdAndPvId(profile, request.ply_pv_id)
            .orElseGet(Supplier { PlayerPvCustomize(profile, request.ply_pv_id) })

        if (request.use_pv_mdl_eqp == 1) {
            customize.module = request.mdl_eqp_pv_ary.csv
            customize.customize = request.c_itm_eqp_pv_ary.csv
            customize.customizeFlag = request.ms_itm_flg_pv_ary.csv
        } else {
            customize.module = "-1,-1,-1"
            customize.customize = "-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1"
            customize.customizeFlag = "1,1,1,1,1,1,1,1,1,1,1,1"
        }

        profile.commonModule = request.mdl_eqp_cmn_ary.csv
        profile.commonCustomizeItems = request.c_itm_eqp_cmn_ary.csv
        profile.moduleSelectItemFlag = request.ms_itm_flg_cmn_ary.csv

        playerProfileService.save(profile)
        pvCustomizeRepository.save<PlayerPvCustomize?>(customize)
        return ShopExitResponse(
            request.cmd,
            request.req_id,
            "ok",
            Result.SUCCESS
        )
    }
}
