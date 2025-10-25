package icu.samnyan.aqua.sega.diva.model.response.ingame;

import icu.samnyan.aqua.sega.diva.model.common.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class ShopExitResponse {
    private Result shp_rslt;

    public ShopExitResponse(Result shp_rslt) {
        this.shp_rslt = shp_rslt;
    }
}
