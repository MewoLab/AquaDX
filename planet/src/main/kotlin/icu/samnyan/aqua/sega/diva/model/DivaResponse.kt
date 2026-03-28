@file:Suppress("unused")

package icu.samnyan.aqua.sega.diva.model

import icu.samnyan.aqua.sega.diva.model.common.*
import icu.samnyan.aqua.sega.diva.util.URIEncoder
import java.time.LocalDateTime

class CardProcedureResponse {
    var cd_adm_result: Result?
    var chg_name_price = 100
    var accept_idx = 0
    var pd_id: Long = 0
    var player_name: String? = null
    var lv_num: Int? = null
    var lv_pnt: Int? = null
    var lv_str: String? = null
    var lv_efct_id: Int? = null
    var lv_plt_id: Int? = null
    var vcld_pts: Int? = null
    var passwd_stat: PassStat? = null

    constructor(
        cd_adm_result: Result?,
        chg_name_price: Int,
        accept_idx: Int,
        pd_id: Long,
        player_name: String?,
        lv_num: Int?,
        lv_pnt: Int?,
        lv_str: String?,
        lv_efct_id: Int?,
        lv_plt_id: Int?,
        vcld_pts: Int?,
        passwd_stat: PassStat?
    ) {
        this.cd_adm_result = cd_adm_result
        this.chg_name_price = chg_name_price
        this.accept_idx = accept_idx
        this.pd_id = pd_id
        this.player_name = player_name
        this.lv_num = lv_num
        this.lv_pnt = lv_pnt
        this.lv_str = lv_str
        this.lv_efct_id = lv_efct_id
        this.lv_plt_id = lv_plt_id
        this.vcld_pts = vcld_pts
        this.passwd_stat = passwd_stat
    }

    constructor(cd_adm_result: Result?) {
        this.cd_adm_result = cd_adm_result
    }
}

class ChangeNameResponse(
    var cd_adm_result: Result?,
    var accept_idx: Int,
    var pd_id: Long,
    var player_name: String?
)

class ChangePasswdResponse(var cd_adm_result: Result?, var accept_idx: Int, var pd_id: Long)
class RegistrationResponse(var cd_adm_result: Result?, var pd_id: Long)
class ContestInfoResponse(var ci_lut: LocalDateTime?, var ci_str: String?)
class FestaInfoResponse(
    var fi_id: String?,
    var fi_name: String?,
    var fi_kind: String?,
    var fi_difficulty: String?,
    var fi_pv_id_lst: String?,
    var fi_attr: String?,
    var fi_add_vp: String?,
    var fi_mul_vp: String?,
    var fi_st: String?,
    var fi_et: String?,
    var fi_lut: String?
)

class NvRankingResponse(
    var rnk_nv_tag_str: String?,
    var rnk_nv_ts: LocalDateTime?,
    var rnk_nv_data: String?,
    var rnk_nv_lut: LocalDateTime?
)

class PsRankingResponse(
    var rnk_ps_lut: LocalDateTime?,
    var rnk_ps_ts: LocalDateTime?,
    var rnk_ps_dffclty: Int,
    var rnk_ps_pv_id: String?,
    var rnk_ps_edtn1: String?,
    var rnk_ps_edtn2: String?,
    var rnk_ps_edtn3: String?,
    var rnk_ps_scr1: String?,
    var rnk_ps_scr2: String?,
    var rnk_ps_scr3: String?,
    var rnk_ps_nm1: String?,
    var rnk_ps_nm2: String?,
    var rnk_ps_nm3: String?
)

class PvListResponse(var pvl_lut: LocalDateTime?, var pv_lst: String?)
class ShopCatalogResponse(var shp_ctlg_lut: LocalDateTime?, var shp_ctlg: String?)
class BuyCstmzItmResponse {
    var shp_rslt: Result?
    var cstmz_itm_id = 0
    var cstmz_itm_have: String? = null
    var vcld_pts = 0

    constructor(shp_rslt: Result?, cstmz_itm_id: Int, cstmz_itm_have: String?, vcld_pts: Int) {
        this.shp_rslt = shp_rslt
        this.cstmz_itm_id = cstmz_itm_id
        this.cstmz_itm_have = cstmz_itm_have
        this.vcld_pts = vcld_pts
    }

    constructor(shp_rslt: Result?) {
        this.shp_rslt = shp_rslt
    }
}

class BuyModuleResponse {
    var shp_rslt: Result?
    var mdl_id = 0
    var mdl_have: String? = null
    var vcld_pts = 0

    constructor(shp_rslt: Result?, mdl_id: Int, mdl_have: String?, vcld_pts: Int) {
        this.shp_rslt = shp_rslt
        this.mdl_id = mdl_id
        this.mdl_have = mdl_have
        this.vcld_pts = vcld_pts
    }

    constructor(shp_rslt: Result?) {
        this.shp_rslt = shp_rslt
    }
}

class GetPvPdResponse(var pd_by_pv_id: String?, var pdddt_flg: Boolean?, var pdddt_tm: String?)
class ShopExitResponse(var shp_rslt: Result?)
class StageResultResponse {
    var chllng_kind = 0
    var lv_num_old = 0
    var lv_pnt_old = 0
    var lv_num = 0
    var lv_pnt = 0
    var lv_str: String? = null
    var lv_efct_id = 0
    var lv_plt_id = 0
    var vcld_pts = 0
    var prsnt_vcld_pts = 0
    var cnp_cid = 0
    var cnp_var = 0
    var cnp_sp: String? = null
    var crwd_kind: String? = "-1,-1,-1"
    var crwd_varue: String? = "-1,-1,-1"
    var crwd_str_0: String? = "***,***,***"
    var crwd_str_1: String? = "***,***,***"
    var cerwd_kind = -1
    var cerwd_varue = -1
    var cerwd_str_0: String? = "***"
    var cerwd_str_1: String? = "***"
    var ttl_str_ary: String? = "xxx"
    var ttl_plt_id_ary: String? = "-1,-1,-1,-1,-1"
    var ttl_desc_ary: String? = "xxx"
    var skin_id_ary: String? = "xxx"
    var skin_name_ary: String? = "xxx"
    var skin_illust_ary: String? = "xxx"
    var skin_desc_ary: String? = "xxx"
    var pdddt_flg = 0
    var pdddt_tm: LocalDateTime? = null
    var nblss_ltt_stts = -1
    var nblss_ltt_tckt = -1
    var nblss_ltt_is_opn = 0
    var nblss_ltt_prz = 0
    var nblss_ltt_nxt_stts = 0
    var nblss_ltt_nxt_tckt = -1
    var my_qst_id: String? = null
    var my_qst_r_qid: String? = null
    var my_qst_r_knd: String? = null
    var my_qst_r_vl: String? = null
    var my_qst_r_nflg: String? = null
    var my_ccd_r_qid: String? = null
    var my_ccd_r_hnd: String? = null
    var my_ccd_r_vp: String? = null

    constructor()

    constructor(
        chllng_kind: Int,
        lv_num_old: Int,
        lv_pnt_old: Int,
        lv_num: Int,
        lv_pnt: Int,
        lv_str: String?,
        lv_efct_id: Int,
        lv_plt_id: Int,
        vcld_pts: Int,
        prsnt_vcld_pts: Int,
        cnp_cid: Int,
        cnp_var: Int,
        cnp_sp: String?,
        crwd_kind: String?,
        crwd_varue: String?,
        crwd_str_0: String?,
        crwd_str_1: String?,
        cerwd_kind: Int,
        cerwd_varue: Int,
        cerwd_str_0: String?,
        cerwd_str_1: String?,
        ttl_str_ary: String?,
        ttl_plt_id_ary: String?,
        ttl_desc_ary: String?,
        skin_id_ary: String?,
        skin_name_ary: String?,
        skin_illust_ary: String?,
        skin_desc_ary: String?,
        pdddt_flg: Int,
        pdddt_tm: LocalDateTime?,
        nblss_ltt_stts: Int,
        nblss_ltt_tckt: Int,
        nblss_ltt_is_opn: Int,
        nblss_ltt_prz: Int,
        nblss_ltt_nxt_stts: Int,
        nblss_ltt_nxt_tckt: Int,
        my_qst_id: String?,
        my_qst_r_qid: String?,
        my_qst_r_knd: String?,
        my_qst_r_vl: String?,
        my_qst_r_nflg: String?,
        my_ccd_r_qid: String?,
        my_ccd_r_hnd: String?,
        my_ccd_r_vp: String?
    ) {
        this.chllng_kind = chllng_kind
        this.lv_num_old = lv_num_old
        this.lv_pnt_old = lv_pnt_old
        this.lv_num = lv_num
        this.lv_pnt = lv_pnt
        this.lv_str = lv_str
        this.lv_efct_id = lv_efct_id
        this.lv_plt_id = lv_plt_id
        this.vcld_pts = vcld_pts
        this.prsnt_vcld_pts = prsnt_vcld_pts
        this.cnp_cid = cnp_cid
        this.cnp_var = cnp_var
        this.cnp_sp = cnp_sp
        this.crwd_kind = crwd_kind
        this.crwd_varue = crwd_varue
        this.crwd_str_0 = crwd_str_0
        this.crwd_str_1 = crwd_str_1
        this.cerwd_kind = cerwd_kind
        this.cerwd_varue = cerwd_varue
        this.cerwd_str_0 = cerwd_str_0
        this.cerwd_str_1 = cerwd_str_1
        this.ttl_str_ary = ttl_str_ary
        this.ttl_plt_id_ary = ttl_plt_id_ary
        this.ttl_desc_ary = ttl_desc_ary
        this.skin_id_ary = skin_id_ary
        this.skin_name_ary = skin_name_ary
        this.skin_illust_ary = skin_illust_ary
        this.skin_desc_ary = skin_desc_ary
        this.pdddt_flg = pdddt_flg
        this.pdddt_tm = pdddt_tm
        this.nblss_ltt_stts = nblss_ltt_stts
        this.nblss_ltt_tckt = nblss_ltt_tckt
        this.nblss_ltt_is_opn = nblss_ltt_is_opn
        this.nblss_ltt_prz = nblss_ltt_prz
        this.nblss_ltt_nxt_stts = nblss_ltt_nxt_stts
        this.nblss_ltt_nxt_tckt = nblss_ltt_nxt_tckt
        this.my_qst_id = my_qst_id
        this.my_qst_r_qid = my_qst_r_qid
        this.my_qst_r_knd = my_qst_r_knd
        this.my_qst_r_vl = my_qst_r_vl
        this.my_qst_r_nflg = my_qst_r_nflg
        this.my_ccd_r_qid = my_ccd_r_qid
        this.my_ccd_r_hnd = my_ccd_r_hnd
        this.my_ccd_r_vp = my_ccd_r_vp
    }
}

class PreStartResponse {
    var ps_result: PreStartResult?
    var accept_idx: Int? = null
    var nblss_ltt_stts: Int? = null
    var nblss_ltt_tckt: Int? = null
    var nblss_ltt_is_opn: Int? = null

    var pd_id: Long = 0
    var player_name: String? = null
    var sort_kind: SortMode? = null
    var lv_num: Int? = null
    var lv_pnt: Int? = null
    var lv_str: String? = null
    var lv_efct_id: Int? = null
    var lv_plt_id: Int? = null
    var mdl_eqp_ary: String? = null
    var mdl_eqp_tm: LocalDateTime? = null
    var skn_eqp: Int? = null
    var btn_se_eqp: Int? = null
    var sld_se_eqp: Int? = null
    var chn_sld_se_eqp: Int? = null
    var sldr_tch_se_eqp: Int? = null
    var vcld_pts: Int? = null
    var passwd_stat: PassStat? = null

    constructor(ps_result: PreStartResult?) {
        this.ps_result = ps_result
    }

    constructor(
        ps_result: PreStartResult?,
        accept_idx: Int?,
        pd_id: Long,
        player_name: String?,
        sort_kind: SortMode?,
        lv_num: Int?,
        lv_pnt: Int?,
        lv_str: String?,
        lv_efct_id: Int?,
        lv_plt_id: Int?,
        mdl_eqp_ary: String?,
        mdl_eqp_tm: LocalDateTime?,
        skn_eqp: Int?,
        btn_se_eqp: Int?,
        sld_se_eqp: Int?,
        chn_sld_se_eqp: Int?,
        sldr_tch_se_eqp: Int?,
        vcld_pts: Int?,
        passwd_stat: PassStat?
    ) {
        this.ps_result = ps_result
        this.accept_idx = accept_idx
        this.nblss_ltt_stts = -1
        this.nblss_ltt_tckt = -1
        this.nblss_ltt_is_opn = -1
        this.pd_id = pd_id
        this.player_name = player_name
        this.sort_kind = sort_kind
        this.lv_num = lv_num
        this.lv_pnt = lv_pnt
        this.lv_str = lv_str
        this.lv_efct_id = lv_efct_id
        this.lv_plt_id = lv_plt_id
        this.mdl_eqp_ary = mdl_eqp_ary
        this.mdl_eqp_tm = mdl_eqp_tm
        this.skn_eqp = skn_eqp
        this.btn_se_eqp = btn_se_eqp
        this.sld_se_eqp = sld_se_eqp
        this.chn_sld_se_eqp = chn_sld_se_eqp
        this.sldr_tch_se_eqp = sldr_tch_se_eqp
        this.vcld_pts = vcld_pts
        this.passwd_stat = passwd_stat
    }
}

class PingResponse(ping_b_msg: String?, ping_m_msg: String?) {
    var ping_b_msg: String? = URIEncoder.encode("Server Running                  No other news")
    var ping_m_msg: String? = URIEncoder.encode("Network Service Running")
    var atnd_lut: String? = null
    var fi_lut: String? = null
    var ci_lut: String? = null
    var qi_lut: String? = null
    var pvl_lut: String? = null
    var pdcl_lut: String? = null
    var pnml_lut: String? = null
    var cinml_lut: String? = null
    var rwl_lut: String? = null
    var bdlol_lut: String? = null
    var shp_ctlg_lut: String? = null
    var cstmz_itm_ctlg_lut: String? = null
    var ngwl_lut: String? = null
    var rnk_nv_lut: String? = null
    var rnk_ps_lut: String? = null
    var bi_lut: String? = null
    var cpi_lut: String? = null
    var p_std_hc_lut: String? = null
    var p_std_i_n_lut: String? = null

    var req_inv_cmd_num: String? = null
    var req_inv_cmd_prm1: String? = null
    var req_inv_cmd_prm2: String? = null
    var req_inv_cmd_prm3: String? = null
    var req_inv_cmd_prm4: String? = null

    var pow_save_flg = false

    var nblss_dnt_p = 0
    var nblss_ltt_rl_vp = 0
    var nblss_ex_ltt_flg = 0

    var nblss_dnt_st_tm: String? = null
    var nblss_dnt_ed_tm: String? = null
    var nblss_ltt_st_tm: String? = null
    var nblss_ltt_ed_tm: String? = null


    init {
        this.ping_b_msg = ping_b_msg
        this.ping_m_msg = ping_m_msg
    }
}

class SpendCreditResponse(
    var cmpgn_rslt: String?,
    var cmpgn_rslt_num: Int,
    var vcld_pts: Int,
    var lv_str: String?,
    var lv_efct_id: Int,
    var lv_plt_id: Int
)

class StartResponse(
    var pd_id: Long,
    start_result: Result?,
    accept_idx: Int,
    start_idx: Int,
    player_name: String?,
    hp_vol: Int,
    btn_se_vol: Boolean,
    btn_se_vol2: Int,
    sldr_se_vol2: Int,
    sort_kind: SortMode?,
    lv_num: Int,
    lv_pnt: Int,
    lv_str: String?,
    lv_efct_id: Int,
    lv_plt_id: Int,
    mdl_eqp_ary: String?,
    c_itm_eqp_ary: String?,
    ms_itm_flg_ary: String?,
    mdl_eqp_tm: LocalDateTime?,
    mdl_have: String?,
    cstmz_itm_have: String?,
    use_pv_mdl_eqp: Boolean,
    use_mdl_pri: Boolean,
    use_pv_skn_eqp: Boolean,
    use_pv_btn_se_eqp: Boolean,
    use_pv_sld_se_eqp: Boolean,
    use_pv_chn_sld_se_eqp: Boolean,
    use_pv_sldr_tch_se_eqp: Boolean,
    vcld_pts: Int,
    nxt_pv_id: Int,
    nxt_dffclty: Difficulty?,
    nxt_edtn: Edition?,
    cv_cid: String?,
    cv_sc: String?,
    cv_rr: String?,
    cv_bv: String?,
    cv_bf: String?,
    cnp_cid: Int,
    cnp_var: Int,
    cnp_rr: ContestBorder?,
    cnp_sp: String?,
    my_lst_0: String?,
    my_lst_1: String?,
    my_lst_2: String?,
    my_lst_3: String?,
    my_lst_4: String?,
    dsp_clr_brdr: String?,
    dsp_intrm_rnk: Boolean,
    dsp_clr_sts: Boolean,
    clr_sts: String?,
    rgo_sts: Boolean,
    my_qst_id: String?,
    my_qst_sts: String?,
    my_qst_prgrs: String?,
    my_qst_et: String?,
    p_std_ie_have: String?,
    p_std_se_have: String?
) {
    var start_result: Result? = Result.SUCCESS
    var accept_idx: Int
    var start_idx: Int
    var player_name: String?
    var hp_vol = 100
    var btn_se_vol = true
    var btn_se_vol2 = 100
    var sldr_se_vol2 = 10
    var sort_kind: SortMode? = SortMode.RELEASE_DATE
    var lv_num: Int
    var lv_pnt: Int
    var lv_str: String?
    var lv_efct_id: Int
    var lv_plt_id: Int
    var mdl_eqp_ary: String?
    var c_itm_eqp_ary: String?
    var ms_itm_flg_ary: String?
    var mdl_eqp_tm: LocalDateTime? = LocalDateTime.now()
    var mdl_have: String? = Const.ALL_HAVE
    var cstmz_itm_have: String? = Const.ALL_HAVE
    var use_pv_mdl_eqp = false
    var use_mdl_pri = false
    var use_pv_skn_eqp = false
    var use_pv_btn_se_eqp = false
    var use_pv_sld_se_eqp = false
    var use_pv_chn_sld_se_eqp = false
    var use_pv_sldr_tch_se_eqp = false
    var vcld_pts = 300
    var nxt_pv_id = -1
    var nxt_dffclty: Difficulty? = Difficulty.NORMAL
    var nxt_edtn: Edition? = Edition.ORIGINAL

    // Contest play history, array of 4
    var cv_cid: String?
    var cv_sc: String?
    var cv_rr: String?
    var cv_bv: String?
    var cv_bf: String?

    // Contest now playing id, return -1 if no current playing contest
    var cnp_cid = -1
    var cnp_var = 0
    var cnp_rr: ContestBorder? = ContestBorder.NONE
    var cnp_sp: String? = ""

    var my_lst_0: String?
    var my_lst_1: String?
    var my_lst_2: String?
    var my_lst_3: String? // Unused
    var my_lst_4: String? // Unused

    var dsp_clr_brdr: String?
    var dsp_intrm_rnk: Boolean
    var dsp_clr_sts: Boolean

    var clr_sts: String?

    var rgo_sts: Boolean

    var my_qst_id: String?
    var my_qst_sts: String?
    var my_qst_prgrs: String?
    var my_qst_et: String?

    var p_std_ie_have: String? = Const.ALL_NOT_HAVE
    var p_std_se_have: String? = Const.ALL_NOT_HAVE

    init {
        this.start_result = start_result
        this.accept_idx = accept_idx
        this.start_idx = start_idx
        this.player_name = player_name
        this.hp_vol = hp_vol
        this.btn_se_vol = btn_se_vol
        this.btn_se_vol2 = btn_se_vol2
        this.sldr_se_vol2 = sldr_se_vol2
        this.sort_kind = sort_kind
        this.lv_num = lv_num
        this.lv_pnt = lv_pnt
        this.lv_str = lv_str
        this.lv_efct_id = lv_efct_id
        this.lv_plt_id = lv_plt_id
        this.mdl_eqp_ary = mdl_eqp_ary
        this.c_itm_eqp_ary = c_itm_eqp_ary
        this.ms_itm_flg_ary = ms_itm_flg_ary
        this.mdl_eqp_tm = mdl_eqp_tm
        this.mdl_have = mdl_have
        this.cstmz_itm_have = cstmz_itm_have
        this.use_pv_mdl_eqp = use_pv_mdl_eqp
        this.use_mdl_pri = use_mdl_pri
        this.use_pv_skn_eqp = use_pv_skn_eqp
        this.use_pv_btn_se_eqp = use_pv_btn_se_eqp
        this.use_pv_sld_se_eqp = use_pv_sld_se_eqp
        this.use_pv_chn_sld_se_eqp = use_pv_chn_sld_se_eqp
        this.use_pv_sldr_tch_se_eqp = use_pv_sldr_tch_se_eqp
        this.vcld_pts = vcld_pts
        this.nxt_pv_id = nxt_pv_id
        this.nxt_dffclty = nxt_dffclty
        this.nxt_edtn = nxt_edtn
        this.cv_cid = cv_cid
        this.cv_sc = cv_sc
        this.cv_rr = cv_rr
        this.cv_bv = cv_bv
        this.cv_bf = cv_bf
        this.cnp_cid = cnp_cid
        this.cnp_var = cnp_var
        this.cnp_rr = cnp_rr
        this.cnp_sp = cnp_sp
        this.my_lst_0 = my_lst_0
        this.my_lst_1 = my_lst_1
        this.my_lst_2 = my_lst_2
        this.my_lst_3 = my_lst_3
        this.my_lst_4 = my_lst_4
        this.dsp_clr_brdr = dsp_clr_brdr
        this.dsp_intrm_rnk = dsp_intrm_rnk
        this.dsp_clr_sts = dsp_clr_sts
        this.clr_sts = clr_sts
        this.rgo_sts = rgo_sts
        this.my_qst_id = my_qst_id
        this.my_qst_sts = my_qst_sts
        this.my_qst_prgrs = my_qst_prgrs
        this.my_qst_et = my_qst_et
        this.p_std_ie_have = p_std_ie_have
        this.p_std_se_have = p_std_se_have
    }
}