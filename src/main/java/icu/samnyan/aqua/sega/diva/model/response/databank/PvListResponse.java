package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PvListResponse {
    private LocalDateTime pvl_lut;
    private String pv_lst;

    public PvListResponse(LocalDateTime pvl_lut, String pv_lst) {
        this.pvl_lut = pvl_lut;
        this.pv_lst = pv_lst;
    }
}
