package icu.samnyan.aqua.sega.diva.model.response.ingame;

import icu.samnyan.aqua.sega.diva.model.common.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class BuyModuleResponse {
    private Result shp_rslt;
    private int mdl_id;
    private String mdl_have;
    private int vcld_pts;

    public BuyModuleResponse(Result shp_rslt, int mdl_id, String mdl_have, int vcld_pts) {
        this.shp_rslt = shp_rslt;
        this.mdl_id = mdl_id;
        this.mdl_have = mdl_have;
        this.vcld_pts = vcld_pts;
    }

    public BuyModuleResponse(Result shp_rslt) {
        this.shp_rslt = shp_rslt;
    }
}
