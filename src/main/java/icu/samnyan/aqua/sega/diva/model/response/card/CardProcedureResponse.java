package icu.samnyan.aqua.sega.diva.model.response.card;

import icu.samnyan.aqua.sega.diva.model.common.PassStat;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class CardProcedureResponse {
    private Result cd_adm_result;
    private int chg_name_price = 100;
    private int accept_idx;
    private long pd_id;
    private String player_name;
    private Integer lv_num;
    private Integer lv_pnt;
    private String lv_str;
    private Integer lv_efct_id;
    private Integer lv_plt_id;
    private Integer vcld_pts;
    private PassStat passwd_stat;

    public CardProcedureResponse(Result cd_adm_result, int chg_name_price, int accept_idx, long pd_id, String player_name, Integer lv_num, Integer lv_pnt, String lv_str, Integer lv_efct_id, Integer lv_plt_id, Integer vcld_pts, PassStat passwd_stat) {
        this.cd_adm_result = cd_adm_result;
        this.chg_name_price = chg_name_price;
        this.accept_idx = accept_idx;
        this.pd_id = pd_id;
        this.player_name = player_name;
        this.lv_num = lv_num;
        this.lv_pnt = lv_pnt;
        this.lv_str = lv_str;
        this.lv_efct_id = lv_efct_id;
        this.lv_plt_id = lv_plt_id;
        this.vcld_pts = vcld_pts;
        this.passwd_stat = passwd_stat;
    }

    public CardProcedureResponse(Result cd_adm_result) {
        this.cd_adm_result = cd_adm_result;
    }
}
