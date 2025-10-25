package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class RmtWpLstResponse {
    private String rwl_lut;
    private String rw_lst;

    public RmtWpLstResponse(String rwl_lut, String rw_lst) {
        this.rwl_lut = rwl_lut;
        this.rw_lst = rw_lst;
    }
}
