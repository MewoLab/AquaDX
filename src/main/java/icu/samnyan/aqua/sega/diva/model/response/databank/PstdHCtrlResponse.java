package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PstdHCtrlResponse {
    private String p_std_hc_lut;
    private String p_std_hc_str;

    public PstdHCtrlResponse(String p_std_hc_lut, String p_std_hc_str) {
        this.p_std_hc_lut = p_std_hc_lut;
        this.p_std_hc_str = p_std_hc_str;
    }
}
