package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class CstmzItmNgMdlListResponse {
    private String cinml_lut;
    private String cinm_lst;

    public CstmzItmNgMdlListResponse(String cinml_lut, String cinm_lst) {
        this.cinml_lut = cinml_lut;
        this.cinm_lst = cinm_lst;
    }
}
