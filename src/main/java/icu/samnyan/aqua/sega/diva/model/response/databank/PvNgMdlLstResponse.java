package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PvNgMdlLstResponse {
    private String pnml_lut;
    private String pnm_lst;

    public PvNgMdlLstResponse(String pnml_lut, String pnm_lst) {
        this.pnml_lut = pnml_lut;
        this.pnm_lst = pnm_lst;
    }
}
