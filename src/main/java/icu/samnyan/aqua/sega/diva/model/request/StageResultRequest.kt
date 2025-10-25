package icu.samnyan.aqua.sega.diva.model.request

import java.time.ZonedDateTime

/**
 * Data format from <url>https://dev.s-ul.eu/mikumiku/minime/wikis/home</url>
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
open class BaseRequest {
    // var cmd: String = "" // Command
    // var req_id: String = "" // Request Id
    var game_id: String = "" // Game Id
    // var r_ver: String = "" // Game Version
    // var kc_serial: String = "" // KeyChip Serial
    // var b_serial: String = "" // Board Serial
    var place_id: String = "" // Place Id
    var time_stamp: ZonedDateTime = ZonedDateTime.now() // Timestamp
    // var start_up_mode: String = "" // Satellite mode, as on dipsw#1, 0 is SUB and 1 is MAIN
    // var cmm_dly_mod: String = ""
    // var cmm_dly_sec: String = ""
    // var cmm_err_mod: String = ""
    // var country_code: String = ""
    // var region_code: String = ""
}

class StageResultRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx: Int = 0
    // var start_idx: Int = 0
    var hp_vol: Int = 0
    var btn_se_vol: Boolean = false
    var btn_se_vol2: Int = 0
    var sldr_se_vol2: Int = 0
    // var use_pv_mdl_eqp: Boolean = false
    // var vcld_pts: Int = 0
    var nxt_pv_id: Int = 0
    var nxt_dffclty: Int = 0
    var nxt_edtn: Int = 0
    var sort_kind: Int = 0
    // var nblss_ltt_stts: Int = 0
    // var nblss_ltt_tckt: Int = 0
    // var my_qst_id: IntArray = intArrayOf()
    // var my_qst_sts: IntArray = intArrayOf()
    // var free_play: Boolean = false
    var game_type: Int = 0
    var stg_difficulty: IntArray = intArrayOf()
    var stg_edtn: IntArray = intArrayOf()
    var stg_ply_pv_id: IntArray = intArrayOf()
    // var stg_sel_pv_id: IntArray = intArrayOf()
    var stg_scrpt_ver: IntArray = intArrayOf()
    var stg_score: IntArray = intArrayOf()
    var stg_chllng_kind: IntArray = intArrayOf()
    var stg_chllng_result: IntArray = intArrayOf()
    var stg_clr_kind: IntArray = intArrayOf()
    var stg_vcld_pts: IntArray = intArrayOf()
    var stg_cool_cnt: IntArray = intArrayOf()
    var stg_cool_pct: IntArray = intArrayOf()
    var stg_fine_cnt: IntArray = intArrayOf()
    var stg_fine_pct: IntArray = intArrayOf()
    var stg_safe_cnt: IntArray = intArrayOf()
    var stg_safe_pct: IntArray = intArrayOf()
    var stg_sad_cnt: IntArray = intArrayOf()
    var stg_sad_pct: IntArray = intArrayOf()
    var stg_wt_wg_cnt: IntArray = intArrayOf()
    var stg_wt_wg_pct: IntArray = intArrayOf()

    var stg_max_cmb: IntArray = intArrayOf()
    var stg_chance_tm: IntArray = intArrayOf()
    var stg_sm_hl: IntArray = intArrayOf()
    var stg_atn_pnt: IntArray = intArrayOf()
    var stg_skin_id: IntArray = intArrayOf()
    var stg_btn_se: IntArray = intArrayOf()
    var stg_btn_se_vol: IntArray = intArrayOf()
    var stg_sld_se: IntArray = intArrayOf()
    var stg_chn_sld_se: IntArray = intArrayOf()
    var stg_sldr_tch_se: IntArray = intArrayOf()
    var stg_mdl_id: IntArray = intArrayOf()
    // var stg_sel_mdl_id: IntArray = intArrayOf()
    // var stg_rvl_pd_id: IntArray = intArrayOf()
    // var stg_rvl_wl: IntArray = intArrayOf()
    var stg_cpt_rslt: IntArray = intArrayOf()
    var stg_sld_scr: IntArray = intArrayOf()
    // var stg_is_sr_gm: IntArray = intArrayOf()
    // var stg_pv_brnch_rslt: IntArray = intArrayOf()
    var stg_vcl_chg: IntArray = intArrayOf()
    var stg_c_itm_id: IntArray = intArrayOf()
    // var stg_ms_itm_flg: IntArray = intArrayOf()
    var stg_rgo: IntArray = intArrayOf()
    var stg_ss_num: IntArray = intArrayOf()
    // var stg_is_cs_scs: IntArray = intArrayOf()
    // var stg_is_nppg_use: IntArray = intArrayOf()
    // var stg_p_std_lo_id: IntArray = intArrayOf()
    // var stg_p_std_is_to: IntArray = intArrayOf()
    // var stg_p_std_is_ccu: IntArray = intArrayOf()
    // var stg_p_std_is_tiu: IntArray = intArrayOf()
    // var stg_p_std_is_iu: IntArray = intArrayOf()
    // var stg_p_std_is_npu: IntArray = intArrayOf()
    // var stg_p_std_is_du: IntArray = intArrayOf()
    // var ply_pv_id: Int = 0
    // var ttl_vp_add: Int = 0
    // var ttl_vp_sub: Int = 0
    // var continue_cnt: Int = 0
    // var gu_cmd: IntArray = intArrayOf()
    // var mdl_eqp_cmn_ary: IntArray = intArrayOf()
    // var c_itm_eqp_cmn_ary: IntArray = intArrayOf()
    // var ms_itm_flg_cmn_ary: IntArray = intArrayOf()
    // var mdl_eqp_pv_ary: IntArray = intArrayOf()
    // var c_itm_eqp_pv_ary: IntArray = intArrayOf()
    // var ms_itm_flg_pv_ary: IntArray = intArrayOf()
    // var stg_mdl_s_sts: IntArray = intArrayOf()

    var cr_cid: Int = 0
    // var cr_sc: Int = 0
    var cr_tv: Int = 0
    var cr_if: Int = 0
    var cr_sp: Array<String?> = arrayOf()
}

class StageStartRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx: Int = 0
    // var start_idx: Int = 0
    // var free_play: Boolean = false
    // var game_type: Int = 0
    // var stg_difficulty: IntArray = intArrayOf()
    // var stg_edtn: IntArray = intArrayOf()
    var stg_ply_pv_id: IntArray = intArrayOf()
    // var stg_sel_pv_id: IntArray = intArrayOf()
    // var stg_scrpt_ver: IntArray = intArrayOf()
    // var stg_skin_id: IntArray = intArrayOf()
    // var stg_btn_se: IntArray = intArrayOf()
    // var stg_btn_se_vol: IntArray = intArrayOf()
    // var stg_sld_se: IntArray = intArrayOf()
    // var stg_chn_sld_se: IntArray = intArrayOf()
    // var stg_sldr_tch_se: IntArray = intArrayOf()
    // var stg_mdl_id: IntArray = intArrayOf()
    // var stg_sel_mdl_id: IntArray = intArrayOf()
    // var stg_rvl_pd_id: IntArray = intArrayOf()
    // var stg_c_itm_id: IntArray = intArrayOf()
    // var stg_ms_itm_flg: IntArray = intArrayOf()
    // var stg_rgo: IntArray = intArrayOf()
    // var stg_ss_num: IntArray = intArrayOf()
    // var stg_is_cs_scs: IntArray = intArrayOf()
    // var continue_cnt: Int = 0
}

class StartRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx: Int = 0
}

class StoreSsRequest : BaseRequest() {
    var pd_id: Long = 0
    // var ss_dat_id: String = ""
    // var ss_pv_id = 0
    // var ss_sel_pv_id = 0
    var ss_mdl_id: IntArray = intArrayOf()
    // var ss_sel_mdl_id: IntArray = intArrayOf()
    var ss_c_itm_id: IntArray = intArrayOf()
    // var ss_pxl_sz: IntArray = intArrayOf()
}

class SpendCreditRequest : BaseRequest() {
    var pd_id: Long = 0
    // var my_qst_id: IntArray = intArrayOf()
    // var my_qst_sts: IntArray = intArrayOf()
    // var crdt_typ = 0
    // var cmpgn_id: IntArray = intArrayOf()
    // var cmpgn_pb: IntArray = intArrayOf()
}

class ShopExitRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx = 0
    // var start_idx = 0
    var use_pv_mdl_eqp = 0
    var ply_pv_id = 0
    var mdl_eqp_cmn_ary: IntArray = intArrayOf()
    var c_itm_eqp_cmn_ary: IntArray = intArrayOf()
    var ms_itm_flg_cmn_ary: IntArray = intArrayOf()
    var mdl_eqp_pv_ary: IntArray = intArrayOf()
    var c_itm_eqp_pv_ary: IntArray = intArrayOf()
    var ms_itm_flg_pv_ary: IntArray = intArrayOf()
}

class RegistrationRequest : BaseRequest() {
    // var pmm: String = ""
    var idm: String = ""
    // var mmgameid: String = ""
    // var mmuid: String = ""
    // var a_code: String = ""
    var aime_id: Long = 0
    // var aime_a_code: String = ""
    // var key_obj_type: String = ""
    var player_name: String = ""
    // var passwd: String = ""
}

class PsRankingRequest : BaseRequest() {
    var rnk_ps_pv_id_lst: IntArray = intArrayOf()
    var rnk_ps_idx = 0
}

class PreStartRequest : BaseRequest() {
    // var pmm: String = ""
    var idm: String = ""
    // var mmgameid: String = ""
    // var mmuid: String = ""
    // var a_code: String = ""
    var aime_id: Long = 0
    // var aime_a_code: String = ""
    // var key_obj_type: String = ""
    // var exec_vu = false
}

class PdUnlockRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx = 0
}

class GetPvPdRequest : BaseRequest() {
    var pd_id: Long = 0
    var difficulty = 0
    var pd_pv_id_lst: IntArray = intArrayOf()
}

class ChangePasswdRequest : BaseRequest() {
    // var a_code: String = ""
    // var aime_id: Long = 0
    // var aime_a_code: String = ""
    var pd_id: Long = 0
    // var accept_idx = 0
    var new_passwd: String = ""
}

class ChangeNameRequest : BaseRequest() {
    // var a_code: String = ""
    // var aime_id: Long = 0
    // var aime_a_code: String = ""
    var pd_id: Long = 0
    // var accept_idx = 0
    var player_name: String = ""
    // var chg_name_price = 0
}

class CardProcedureRequest : BaseRequest() {
    // var cd_adm_cmd = 0
    // var a_code: String = ""
    var aime_id: Long = 0
    // var aime_a_code: String = ""
}

class BuyModuleRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx = 0
    // var start_idx = 0
    var mdl_id = 0
    // var mdl_price = 0
}

class BuyCstmzItmRequest : BaseRequest() {
    var pd_id: Long = 0
    // var accept_idx = 0
    // var start_idx = 0
    var cstmz_itm_id = 0
    // var cstmz_itm_price = 0
}