package icu.samnyan.aqua.sega.diva.handler.ingame;

import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerPvCustomizeRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.request.ingame.ShopExitRequest;
import icu.samnyan.aqua.sega.diva.model.response.ingame.ShopExitResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvCustomize;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static icu.samnyan.aqua.sega.diva.util.DivaStringUtils.arrToCsv;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class ShopExitHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShopExitHandler.class);
    private final PlayerProfileService playerProfileService;
    private final PlayerPvCustomizeRepository pvCustomizeRepository;

    public String handle(ShopExitRequest request) {

        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
        PlayerPvCustomize customize = pvCustomizeRepository.findByPdIdAndPvId(profile, request.getPly_pv_id()).orElseGet(() -> new PlayerPvCustomize(profile, request.getPly_pv_id()));

        if (request.getUse_pv_mdl_eqp() == 1) {
            customize.setModule(arrToCsv(request.getMdl_eqp_pv_ary()));
            customize.setCustomize(arrToCsv(request.getC_itm_eqp_pv_ary()));
            customize.setCustomizeFlag(arrToCsv(request.getMs_itm_flg_pv_ary()));
        } else {
            customize.setModule("-1,-1,-1");
            customize.setCustomize("-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1");
            customize.setCustomizeFlag("1,1,1,1,1,1,1,1,1,1,1,1");
        }

        profile.setCommonModule(arrToCsv(request.getMdl_eqp_cmn_ary()));
        profile.setCommonCustomizeItems(arrToCsv(request.getC_itm_eqp_cmn_ary()));
        profile.setModuleSelectItemFlag(arrToCsv(request.getMs_itm_flg_cmn_ary()));

        playerProfileService.save(profile);
        pvCustomizeRepository.save(customize);
        ShopExitResponse response = new ShopExitResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                Result.SUCCESS
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
