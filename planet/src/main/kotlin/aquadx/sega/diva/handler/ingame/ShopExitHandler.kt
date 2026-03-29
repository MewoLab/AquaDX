package aquadx.sega.diva.handler.ingame

import ext.csv
import aquadx.sega.diva.DivaRepos
import aquadx.sega.diva.model.ShopExitRequest
import aquadx.sega.diva.model.ShopExitResponse
import aquadx.sega.diva.model.common.Result
import aquadx.sega.diva.model.db.userdata.PlayerPvCustomize
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ShopExitHandler(val db: DivaRepos) {
    fun handle(request: ShopExitRequest): Any {
        val profile = db.profile(request.pd_id)
        val customize = db.pvCustomize.findByPdIdAndPvId(profile, request.ply_pv_id)
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

        db.profile.save(profile)
        db.pvCustomize.save(customize)
        return ShopExitResponse(
            Result.SUCCESS
        )
    }
}
