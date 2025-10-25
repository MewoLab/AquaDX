package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PvDefChrLstResponse {
    private String pdcl_lut;
    private String pdc_lst;

    public PvDefChrLstResponse(String pdcl_lut, String pdc_lst) {
        this.pdcl_lut = pdcl_lut;
        this.pdc_lst = pdc_lst;
    }
}
