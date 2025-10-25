package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class ShopCatalogResponse {
    private LocalDateTime shp_ctlg_lut;
    private String shp_ctlg;

    public ShopCatalogResponse(LocalDateTime shp_ctlg_lut, String shp_ctlg) {
        this.shp_ctlg_lut = shp_ctlg_lut;
        this.shp_ctlg = shp_ctlg;
    }
}
