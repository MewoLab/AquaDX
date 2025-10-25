package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class FestaInfoResponse {
    private String fi_id;
    private String fi_name;
    private String fi_kind;
    private String fi_difficulty;
    private String fi_pv_id_lst;
    private String fi_attr;
    private String fi_add_vp;
    private String fi_mul_vp;
    private String fi_st;
    private String fi_et;
    private String fi_lut;

    public FestaInfoResponse(String fi_id, String fi_name, String fi_kind, String fi_difficulty, String fi_pv_id_lst, String fi_attr, String fi_add_vp, String fi_mul_vp, String fi_st, String fi_et, String fi_lut) {
        this.fi_id = fi_id;
        this.fi_name = fi_name;
        this.fi_kind = fi_kind;
        this.fi_difficulty = fi_difficulty;
        this.fi_pv_id_lst = fi_pv_id_lst;
        this.fi_attr = fi_attr;
        this.fi_add_vp = fi_add_vp;
        this.fi_mul_vp = fi_mul_vp;
        this.fi_st = fi_st;
        this.fi_et = fi_et;
        this.fi_lut = fi_lut;
    }
}
