package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PstdItemNgLstResponse {
    private String p_std_i_n_lut;
    private String p_std_i_ie_n_lst;
    private String p_std_i_se_n_lst;

    public PstdItemNgLstResponse(String p_std_i_n_lut, String p_std_i_ie_n_lst, String p_std_i_se_n_lst) {
        this.p_std_i_n_lut = p_std_i_n_lut;
        this.p_std_i_ie_n_lst = p_std_i_ie_n_lst;
        this.p_std_i_se_n_lst = p_std_i_se_n_lst;
    }
}
