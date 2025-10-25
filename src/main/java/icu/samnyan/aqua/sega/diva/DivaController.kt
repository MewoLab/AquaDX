package icu.samnyan.aqua.sega.diva

import ext.JDict
import ext.MutJDict
import ext.emptyMap
import ext.logger
import icu.samnyan.aqua.sega.diva.handler.AttendHandler
import icu.samnyan.aqua.sega.diva.handler.PingHandler
import icu.samnyan.aqua.sega.diva.handler.card.CardProcedureHandler
import icu.samnyan.aqua.sega.diva.handler.card.ChangeNameHandler
import icu.samnyan.aqua.sega.diva.handler.card.ChangePasswdHandler
import icu.samnyan.aqua.sega.diva.handler.card.RegistrationHandler
import icu.samnyan.aqua.sega.diva.handler.databank.*
import icu.samnyan.aqua.sega.diva.handler.ingame.*
import icu.samnyan.aqua.sega.diva.handler.user.*
import icu.samnyan.aqua.sega.diva.model.*
import icu.samnyan.aqua.sega.diva.util.DivaMapper
import icu.samnyan.aqua.sega.diva.util.DivaTime
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

val DIVA_BAD = mapOf("stat" to "0")
val DIVA_OK = emptyMap
val DIVA_INIT = mapOf("db_close" to "0,0", "retry_time" to "FFFF")

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@RestController
@RequestMapping("/g/diva")
class DivaController(
    val attendHandler: AttendHandler,
    val cardProcedureHandler: CardProcedureHandler,
    val changeNameHandler: ChangeNameHandler,
    val changePasswdHandler: ChangePasswdHandler,
    val registrationHandler: RegistrationHandler,
    val contestInfoHandler: ContestInfoHandler,
    val festaInfoHandler: FestaInfoHandler,
    val nvRankingHandler: NvRankingHandler,
    val psRankingHandler: PsRankingHandler,
    val pvListHandler: PvListHandler,
    val shopCatalogHandler: ShopCatalogHandler,
    val buyCstmzItmHandler: BuyCstmzItmHandler,
    val buyModuleHandler: BuyModuleHandler,
    val getPvPdHandler: GetPvPdHandler,
    val shopExitHandler: ShopExitHandler,
    val stageResultHandler: StageResultHandler,
    val stageStartHandler: StageStartHandler,
    val storeSsHandler: StoreSsHandler,
    val pingHandler: PingHandler,
    val endHandler: EndHandler,
    val pdUnlockHandler: PdUnlockHandler,
    val preStartHandler: PreStartHandler,
    val spendCreditHandler: SpendCreditHandler,
    val startHandler: StartHandler,
    val db: DivaRepos
) {
    val logger = logger()
    val mapper = DivaMapper()

    fun buildResultMap(map: JDict) =
        map.filterValues { it != null && !(it is String && it == "") }
            .map { (k, v) -> "$k=$v" }.joinToString("&")

    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun formRequest(request: HttpServletRequest): String? {
        val bodyStr = String(request.inputStream.readAllBytes())
        val body = parse(bodyStr)

        val command = body.getOrDefault("cmd", "") as String?

        logger.info("{}: {}", command, body)
        val respObj = when (command) {
            "game_init" -> DIVA_INIT
            "attend" -> attendHandler.handle()
            "test" -> DIVA_INIT
            "nv_ranking" -> nvRankingHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "ps_ranking" -> psRankingHandler.handle(mapper.convert(body, PsRankingRequest::class.java))

            "pv_list" -> pvListHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "ng_word" -> DIVA_OK
            "rmt_wp_list" -> mapOf("rwl_lut" to DivaTime.now, "rw_lst" to "***")
            "festa_info" -> festaInfoHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "contest_info" -> contestInfoHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "pv_def_chr_list" -> mapOf("pdcl_lut" to DivaTime.now, "pdc_lst" to "***")
            "pv_ng_mdl_list" ->  mapOf("pnml_lut" to DivaTime.now, "pnm_lst" to "***")
            "cstmz_itm_ng_mdl_list" -> mapOf("cinml_lut" to DivaTime.now, "cinm_lst" to "***")

            "banner_info" -> listOf("bi_lut", "bi_id", "bi_st", "bi_et", "bi_ut").associateWith { null }
            "banner_data" -> mapOf("bd_ut" to DivaTime.now, "bd_ti" to "***", "bd_hs" to "***", "bd_id" to body["bd_id"])

            "cm_ply_info" -> emptyMap
            "qst_inf" -> mapOf("qi_lut" to DivaTime.now, "qhi_str" to null, "qrai_str" to null)
            "pstd_h_ctrl" -> mapOf("p_std_hc_lut" to DivaTime.now, "p_std_hc_str" to "***,***")
            "pstd_item_ng_lst" -> mapOf("p_std_i_n_lut" to DivaTime.now, "p_std_i_ie_n_lst" to "***", "p_std_i_se_n_lst" to "***")

            "shop_catalog" -> shopCatalogHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "cstmz_itm_ctlg" -> mapOf(
                "cstmz_itm_ctlg_lut" to DivaTime.now,
                "cstmz_itm_ctlg" to encode(db.g.customize.findAll().map { it.toInternal() }.joinToString(",") { encode(it) })
            )
            "card_procedure" -> cardProcedureHandler.handle(mapper.convert(body, CardProcedureRequest::class.java))

            "registration" -> registrationHandler.handle(mapper.convert(body, RegistrationRequest::class.java))

            "init_passwd" -> DIVA_BAD

            "change_passwd" -> changePasswdHandler.handle(mapper.convert(body, ChangePasswdRequest::class.java))

            "change_name" -> changeNameHandler.handle(mapper.convert(body, ChangeNameRequest::class.java))

            "pre_start" -> preStartHandler.handle(mapper.convert(body, PreStartRequest::class.java))
            "start" -> startHandler.handle(mapper.convert(body, StartRequest::class.java))
            "pd_unlock" -> pdUnlockHandler.handle(mapper.convert(body, PdUnlockRequest::class.java))
            "spend_credit" -> spendCreditHandler.handle(mapper.convert(body, SpendCreditRequest::class.java))

            "no_card_end" -> DIVA_INIT
            "end" -> endHandler.handle(mapper.convert(body, StageResultRequest::class.java))
            "get_pv_pd" -> getPvPdHandler.handle(mapper.convert(body, GetPvPdRequest::class.java))
            "buy_module" -> buyModuleHandler.handle(mapper.convert(body, BuyModuleRequest::class.java))

            "buy_cstmz_itm" -> buyCstmzItmHandler.handle(mapper.convert(body, BuyCstmzItmRequest::class.java))

            "shop_exit" -> shopExitHandler.handle(mapper.convert(body, ShopExitRequest::class.java))
            "stage_start" -> stageStartHandler.handle(mapper.convert(body, StageStartRequest::class.java))

            "stage_result" -> stageResultHandler.handle(mapper.convert(body, StageResultRequest::class.java))

            "store_ss" -> DIVA_INIT
            else -> DIVA_BAD
        }
        val resp = respObj as? String
            ?: buildResultMap(mapOf("cmd" to command, "req_id" to body["req_id"], "stat" to "ok") + mapper.toMap(respObj))
        logger.info("Response: {}", resp)
        return resp
    }

    @PostMapping(value = ["/"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun fileRequest(@RequestParam query: String, @RequestParam(required = false) bin: MultipartFile): String? {
        val body = parse(query)
        val command = body.getOrDefault("cmd", "") as String?

        logger.info("{}: {}", command, body)

        val respObj = when (command) {
            "ping" -> pingHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "investigate" -> DIVA_INIT
            "store_ss" -> storeSsHandler.handle(mapper.convert(body, StoreSsRequest::class.java), bin)
            else -> "stat=1"
        }
        val resp = respObj as? String ?: buildResultMap(mapper.toMap(respObj))
        logger.info("Response: {}", resp)
        return resp
    }

    fun parse(form: String): MutJDict {
        val kvps = form.split('&').dropLastWhile { it.isEmpty() }
        val body: MutJDict = LinkedHashMap()
        for (kvp in kvps) {
            var (k, v) = kvp.split('=').dropLastWhile { it.isEmpty() }
            v = URLDecoder.decode(v, StandardCharsets.UTF_8)
            body[k] =  if (v.contains(",")) { v.deArray() } else { v }
        }
        return body
    }

    fun String.deArray(): Any {
        if (!contains(',')) return this
        return split(',').dropLastWhile { it.isEmpty() }
            .map { URLDecoder.decode(it, StandardCharsets.UTF_8) }
            .map { it.deArray() }
    }
}