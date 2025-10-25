package icu.samnyan.aqua.sega.diva.model.response.user;

import icu.samnyan.aqua.sega.diva.model.common.PassStat;
import icu.samnyan.aqua.sega.diva.model.common.PreStartResult;
import icu.samnyan.aqua.sega.diva.model.common.SortMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PreStartResponse {

    private PreStartResult ps_result;
    private Integer accept_idx;
    private Integer nblss_ltt_stts;
    private Integer nblss_ltt_tckt;
    private Integer nblss_ltt_is_opn;

    private long pd_id;
    private String player_name;
    private SortMode sort_kind;
    private Integer lv_num;
    private Integer lv_pnt;
    private String lv_str;
    private Integer lv_efct_id;
    private Integer lv_plt_id;
    private String mdl_eqp_ary;
    private LocalDateTime mdl_eqp_tm;
    private Integer skn_eqp;
    private Integer btn_se_eqp;
    private Integer sld_se_eqp;
    private Integer chn_sld_se_eqp;
    private Integer sldr_tch_se_eqp;
    private Integer vcld_pts;
    private PassStat passwd_stat;

    public PreStartResponse(PreStartResult ps_result) {
        this.ps_result = ps_result;
    }

    public PreStartResponse(PreStartResult ps_result, Integer accept_idx, long pd_id, String player_name, SortMode sort_kind, Integer lv_num, Integer lv_pnt, String lv_str, Integer lv_efct_id, Integer lv_plt_id, String mdl_eqp_ary, LocalDateTime mdl_eqp_tm, Integer skn_eqp, Integer btn_se_eqp, Integer sld_se_eqp, Integer chn_sld_se_eqp, Integer sldr_tch_se_eqp, Integer vcld_pts, PassStat passwd_stat) {
        this.ps_result = ps_result;
        this.accept_idx = accept_idx;
        this.nblss_ltt_stts = -1;
        this.nblss_ltt_tckt = -1;
        this.nblss_ltt_is_opn = -1;
        this.pd_id = pd_id;
        this.player_name = player_name;
        this.sort_kind = sort_kind;
        this.lv_num = lv_num;
        this.lv_pnt = lv_pnt;
        this.lv_str = lv_str;
        this.lv_efct_id = lv_efct_id;
        this.lv_plt_id = lv_plt_id;
        this.mdl_eqp_ary = mdl_eqp_ary;
        this.mdl_eqp_tm = mdl_eqp_tm;
        this.skn_eqp = skn_eqp;
        this.btn_se_eqp = btn_se_eqp;
        this.sld_se_eqp = sld_se_eqp;
        this.chn_sld_se_eqp = chn_sld_se_eqp;
        this.sldr_tch_se_eqp = sldr_tch_se_eqp;
        this.vcld_pts = vcld_pts;
        this.passwd_stat = passwd_stat;
    }
}
