package icu.samnyan.aqua.sega.diva.model

import java.time.ZonedDateTime

/**
 * Data format from <url>https://dev.s-ul.eu/mikumiku/minime/wikis/home</url>
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
open class BaseRequest {
    var game_id: String = "" // Game Id
    var place_id: String = "" // Place Id
    var time_stamp: ZonedDateTime = ZonedDateTime.now() // Timestamp
}

open class PdRequest : BaseRequest() {
    var pd_id: Long = 0
}

class StageResultRequest : PdRequest() {
    var hp_vol: Int = 0
    var btn_se_vol: Boolean = false
    var btn_se_vol2: Int = 0
    var sldr_se_vol2: Int = 0
    var nxt_pv_id: Int = 0
    var nxt_dffclty: Int = 0
    var nxt_edtn: Int = 0
    var sort_kind: Int = 0
    var game_type: Int = 0
    var stg_difficulty: IntArray = intArrayOf()
    var stg_edtn: IntArray = intArrayOf()
    var stg_ply_pv_id: IntArray = intArrayOf()
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
    var stg_cpt_rslt: IntArray = intArrayOf()
    var stg_sld_scr: IntArray = intArrayOf()
    var stg_vcl_chg: IntArray = intArrayOf()
    var stg_c_itm_id: IntArray = intArrayOf()
    var stg_rgo: IntArray = intArrayOf()
    var stg_ss_num: IntArray = intArrayOf()

    var cr_cid: Int = 0
    var cr_tv: Int = 0
    var cr_if: Int = 0
    var cr_sp: Array<String?> = arrayOf()
}

class StageStartRequest : PdRequest() {
    var stg_ply_pv_id: IntArray = intArrayOf()
}

class StartRequest : PdRequest() {
}

class StoreSsRequest : PdRequest() {
    var ss_mdl_id: IntArray = intArrayOf()
    var ss_c_itm_id: IntArray = intArrayOf()
}

class SpendCreditRequest : PdRequest() {
}

class ShopExitRequest : PdRequest() {
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
    var idm: String = ""
    var aime_id: Long = 0
    var player_name: String = ""
}

class PsRankingRequest : BaseRequest() {
    var rnk_ps_pv_id_lst: IntArray = intArrayOf()
    var rnk_ps_idx = 0
}

class PreStartRequest : BaseRequest() {
    var idm: String = ""
    var aime_id: Long = 0
}

class PdUnlockRequest : PdRequest() {
}

class GetPvPdRequest : PdRequest() {
    var difficulty = 0
    var pd_pv_id_lst: IntArray = intArrayOf()
}

class ChangePasswdRequest : PdRequest() {
    var new_passwd: String = ""
}

class ChangeNameRequest : PdRequest() {
    var player_name: String = ""
}

class CardProcedureRequest : BaseRequest() {
    var aime_id: Long = 0
}

class BuyModuleRequest : PdRequest() {
    var mdl_id = 0
}

class BuyCstmzItmRequest : PdRequest() {
    var cstmz_itm_id = 0
}