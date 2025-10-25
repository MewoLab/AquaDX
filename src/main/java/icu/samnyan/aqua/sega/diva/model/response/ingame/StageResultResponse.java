package icu.samnyan.aqua.sega.diva.model.response.ingame;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class StageResultResponse {

    private int chllng_kind;
    private int lv_num_old;
    private int lv_pnt_old;
    private int lv_num;
    private int lv_pnt;
    private String lv_str;
    private int lv_efct_id;
    private int lv_plt_id;
    private int vcld_pts;
    private int prsnt_vcld_pts;
    private int cnp_cid;
    private int cnp_val;
    private String cnp_sp;
    private String crwd_kind = "-1,-1,-1";
    private String crwd_value = "-1,-1,-1";
    private String crwd_str_0 = "***,***,***";
    private String crwd_str_1 = "***,***,***";
    private int cerwd_kind = -1;
    private int cerwd_value = -1;
    private String cerwd_str_0 = "***";
    private String cerwd_str_1 = "***";
    private String ttl_str_ary = "xxx";
    private String ttl_plt_id_ary = "-1,-1,-1,-1,-1";
    private String ttl_desc_ary = "xxx";
    private String skin_id_ary = "xxx";
    private String skin_name_ary = "xxx";
    private String skin_illust_ary = "xxx";
    private String skin_desc_ary = "xxx";
    private int pdddt_flg = 0;
    private LocalDateTime pdddt_tm;
    private int nblss_ltt_stts = -1;
    private int nblss_ltt_tckt = -1;
    private int nblss_ltt_is_opn = 0;
    private int nblss_ltt_prz = 0;
    private int nblss_ltt_nxt_stts = 0;
    private int nblss_ltt_nxt_tckt = -1;
    private String my_qst_id;
    private String my_qst_r_qid;
    private String my_qst_r_knd;
    private String my_qst_r_vl;
    private String my_qst_r_nflg;
    private String my_ccd_r_qid;
    private String my_ccd_r_hnd;
    private String my_ccd_r_vp;

    public StageResultResponse() {}

    public StageResultResponse(int chllng_kind, int lv_num_old, int lv_pnt_old, int lv_num, int lv_pnt, String lv_str, int lv_efct_id, int lv_plt_id, int vcld_pts, int prsnt_vcld_pts, int cnp_cid, int cnp_val, String cnp_sp, String crwd_kind, String crwd_value, String crwd_str_0, String crwd_str_1, int cerwd_kind, int cerwd_value, String cerwd_str_0, String cerwd_str_1, String ttl_str_ary, String ttl_plt_id_ary, String ttl_desc_ary, String skin_id_ary, String skin_name_ary, String skin_illust_ary, String skin_desc_ary, int pdddt_flg, LocalDateTime pdddt_tm, int nblss_ltt_stts, int nblss_ltt_tckt, int nblss_ltt_is_opn, int nblss_ltt_prz, int nblss_ltt_nxt_stts, int nblss_ltt_nxt_tckt, String my_qst_id, String my_qst_r_qid, String my_qst_r_knd, String my_qst_r_vl, String my_qst_r_nflg, String my_ccd_r_qid, String my_ccd_r_hnd, String my_ccd_r_vp) {
        this.chllng_kind = chllng_kind;
        this.lv_num_old = lv_num_old;
        this.lv_pnt_old = lv_pnt_old;
        this.lv_num = lv_num;
        this.lv_pnt = lv_pnt;
        this.lv_str = lv_str;
        this.lv_efct_id = lv_efct_id;
        this.lv_plt_id = lv_plt_id;
        this.vcld_pts = vcld_pts;
        this.prsnt_vcld_pts = prsnt_vcld_pts;
        this.cnp_cid = cnp_cid;
        this.cnp_val = cnp_val;
        this.cnp_sp = cnp_sp;
        this.crwd_kind = crwd_kind;
        this.crwd_value = crwd_value;
        this.crwd_str_0 = crwd_str_0;
        this.crwd_str_1 = crwd_str_1;
        this.cerwd_kind = cerwd_kind;
        this.cerwd_value = cerwd_value;
        this.cerwd_str_0 = cerwd_str_0;
        this.cerwd_str_1 = cerwd_str_1;
        this.ttl_str_ary = ttl_str_ary;
        this.ttl_plt_id_ary = ttl_plt_id_ary;
        this.ttl_desc_ary = ttl_desc_ary;
        this.skin_id_ary = skin_id_ary;
        this.skin_name_ary = skin_name_ary;
        this.skin_illust_ary = skin_illust_ary;
        this.skin_desc_ary = skin_desc_ary;
        this.pdddt_flg = pdddt_flg;
        this.pdddt_tm = pdddt_tm;
        this.nblss_ltt_stts = nblss_ltt_stts;
        this.nblss_ltt_tckt = nblss_ltt_tckt;
        this.nblss_ltt_is_opn = nblss_ltt_is_opn;
        this.nblss_ltt_prz = nblss_ltt_prz;
        this.nblss_ltt_nxt_stts = nblss_ltt_nxt_stts;
        this.nblss_ltt_nxt_tckt = nblss_ltt_nxt_tckt;
        this.my_qst_id = my_qst_id;
        this.my_qst_r_qid = my_qst_r_qid;
        this.my_qst_r_knd = my_qst_r_knd;
        this.my_qst_r_vl = my_qst_r_vl;
        this.my_qst_r_nflg = my_qst_r_nflg;
        this.my_ccd_r_qid = my_ccd_r_qid;
        this.my_ccd_r_hnd = my_ccd_r_hnd;
        this.my_ccd_r_vp = my_ccd_r_vp;
    }
}
