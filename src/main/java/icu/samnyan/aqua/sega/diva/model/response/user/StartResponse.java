package icu.samnyan.aqua.sega.diva.model.response.user;

import icu.samnyan.aqua.sega.diva.model.common.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static icu.samnyan.aqua.sega.diva.model.common.Const.ALL_HAVE;
import static icu.samnyan.aqua.sega.diva.model.common.Const.ALL_NOT_HAVE;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class StartResponse {

    private long pd_id;
    private Result start_result = Result.SUCCESS;
    private int accept_idx;
    private int start_idx;
    private String player_name;
    private int hp_vol = 100;
    private boolean btn_se_vol = true;
    private int btn_se_vol2 = 100;
    private int sldr_se_vol2 = 10;
    private SortMode sort_kind = SortMode.RELEASE_DATE;
    private int lv_num;
    private int lv_pnt;
    private String lv_str;
    private int lv_efct_id;
    private int lv_plt_id;
    private String mdl_eqp_ary;
    private String c_itm_eqp_ary;
    private String ms_itm_flg_ary;
    private LocalDateTime mdl_eqp_tm = LocalDateTime.now();
    private String mdl_have = ALL_HAVE;
    private String cstmz_itm_have = ALL_HAVE;
    private boolean use_pv_mdl_eqp = false;
    private boolean use_mdl_pri = false;
    private boolean use_pv_skn_eqp = false;
    private boolean use_pv_btn_se_eqp = false;
    private boolean use_pv_sld_se_eqp = false;
    private boolean use_pv_chn_sld_se_eqp = false;
    private boolean use_pv_sldr_tch_se_eqp = false;
    private int vcld_pts = 300;
    private int nxt_pv_id = -1;
    private Difficulty nxt_dffclty = Difficulty.NORMAL;
    private Edition nxt_edtn = Edition.ORIGINAL;

    // Contest play history, array of 4
    private String cv_cid;
    private String cv_sc;
    private String cv_rr;
    private String cv_bv;
    private String cv_bf;

    // Contest now playing id, return -1 if no current playing contest
    private int cnp_cid = -1;
    private int cnp_val = 0;
    private ContestBorder cnp_rr = ContestBorder.NONE;
    private String cnp_sp = "";

    private String my_lst_0;
    private String my_lst_1;
    private String my_lst_2;
    private String my_lst_3; // Unused
    private String my_lst_4; // Unused

    private String dsp_clr_brdr;
    private boolean dsp_intrm_rnk;
    private boolean dsp_clr_sts;

    private String clr_sts;

    private boolean rgo_sts;

    private String my_qst_id;
    private String my_qst_sts;
    private String my_qst_prgrs;
    private String my_qst_et;

    private String p_std_ie_have = ALL_NOT_HAVE;
    private String p_std_se_have = ALL_NOT_HAVE;

    public StartResponse(long pd_id, Result start_result, int accept_idx, int start_idx, String player_name, int hp_vol, boolean btn_se_vol, int btn_se_vol2, int sldr_se_vol2, SortMode sort_kind, int lv_num, int lv_pnt, String lv_str, int lv_efct_id, int lv_plt_id, String mdl_eqp_ary, String c_itm_eqp_ary, String ms_itm_flg_ary, LocalDateTime mdl_eqp_tm, String mdl_have, String cstmz_itm_have, boolean use_pv_mdl_eqp, boolean use_mdl_pri, boolean use_pv_skn_eqp, boolean use_pv_btn_se_eqp, boolean use_pv_sld_se_eqp, boolean use_pv_chn_sld_se_eqp, boolean use_pv_sldr_tch_se_eqp, int vcld_pts, int nxt_pv_id, Difficulty nxt_dffclty, Edition nxt_edtn, String cv_cid, String cv_sc, String cv_rr, String cv_bv, String cv_bf, int cnp_cid, int cnp_val, ContestBorder cnp_rr, String cnp_sp, String my_lst_0, String my_lst_1, String my_lst_2, String my_lst_3, String my_lst_4, String dsp_clr_brdr, boolean dsp_intrm_rnk, boolean dsp_clr_sts, String clr_sts, boolean rgo_sts, String my_qst_id, String my_qst_sts, String my_qst_prgrs, String my_qst_et, String p_std_ie_have, String p_std_se_have) {
        this.pd_id = pd_id;
        this.start_result = start_result;
        this.accept_idx = accept_idx;
        this.start_idx = start_idx;
        this.player_name = player_name;
        this.hp_vol = hp_vol;
        this.btn_se_vol = btn_se_vol;
        this.btn_se_vol2 = btn_se_vol2;
        this.sldr_se_vol2 = sldr_se_vol2;
        this.sort_kind = sort_kind;
        this.lv_num = lv_num;
        this.lv_pnt = lv_pnt;
        this.lv_str = lv_str;
        this.lv_efct_id = lv_efct_id;
        this.lv_plt_id = lv_plt_id;
        this.mdl_eqp_ary = mdl_eqp_ary;
        this.c_itm_eqp_ary = c_itm_eqp_ary;
        this.ms_itm_flg_ary = ms_itm_flg_ary;
        this.mdl_eqp_tm = mdl_eqp_tm;
        this.mdl_have = mdl_have;
        this.cstmz_itm_have = cstmz_itm_have;
        this.use_pv_mdl_eqp = use_pv_mdl_eqp;
        this.use_mdl_pri = use_mdl_pri;
        this.use_pv_skn_eqp = use_pv_skn_eqp;
        this.use_pv_btn_se_eqp = use_pv_btn_se_eqp;
        this.use_pv_sld_se_eqp = use_pv_sld_se_eqp;
        this.use_pv_chn_sld_se_eqp = use_pv_chn_sld_se_eqp;
        this.use_pv_sldr_tch_se_eqp = use_pv_sldr_tch_se_eqp;
        this.vcld_pts = vcld_pts;
        this.nxt_pv_id = nxt_pv_id;
        this.nxt_dffclty = nxt_dffclty;
        this.nxt_edtn = nxt_edtn;
        this.cv_cid = cv_cid;
        this.cv_sc = cv_sc;
        this.cv_rr = cv_rr;
        this.cv_bv = cv_bv;
        this.cv_bf = cv_bf;
        this.cnp_cid = cnp_cid;
        this.cnp_val = cnp_val;
        this.cnp_rr = cnp_rr;
        this.cnp_sp = cnp_sp;
        this.my_lst_0 = my_lst_0;
        this.my_lst_1 = my_lst_1;
        this.my_lst_2 = my_lst_2;
        this.my_lst_3 = my_lst_3;
        this.my_lst_4 = my_lst_4;
        this.dsp_clr_brdr = dsp_clr_brdr;
        this.dsp_intrm_rnk = dsp_intrm_rnk;
        this.dsp_clr_sts = dsp_clr_sts;
        this.clr_sts = clr_sts;
        this.rgo_sts = rgo_sts;
        this.my_qst_id = my_qst_id;
        this.my_qst_sts = my_qst_sts;
        this.my_qst_prgrs = my_qst_prgrs;
        this.my_qst_et = my_qst_et;
        this.p_std_ie_have = p_std_ie_have;
        this.p_std_se_have = p_std_se_have;
    }
}
