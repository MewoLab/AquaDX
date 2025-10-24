package icu.samnyan.aqua.sega.diva

import ext.JDict
import ext.MutJDict
import ext.logger
import icu.samnyan.aqua.sega.diva.handler.AttendHandler
import icu.samnyan.aqua.sega.diva.handler.GameInitHandler
import icu.samnyan.aqua.sega.diva.handler.PingHandler
import icu.samnyan.aqua.sega.diva.handler.buildResultMap
import icu.samnyan.aqua.sega.diva.handler.card.*
import icu.samnyan.aqua.sega.diva.handler.databank.*
import icu.samnyan.aqua.sega.diva.handler.ingame.*
import icu.samnyan.aqua.sega.diva.handler.user.*
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.request.boot.GameInitRequest
import icu.samnyan.aqua.sega.diva.model.request.card.CardProcedureRequest
import icu.samnyan.aqua.sega.diva.model.request.card.ChangeNameRequest
import icu.samnyan.aqua.sega.diva.model.request.card.ChangePasswdRequest
import icu.samnyan.aqua.sega.diva.model.request.card.RegistrationRequest
import icu.samnyan.aqua.sega.diva.model.request.databank.BannerDataRequest
import icu.samnyan.aqua.sega.diva.model.request.databank.PsRankingRequest
import icu.samnyan.aqua.sega.diva.model.request.ingame.*
import icu.samnyan.aqua.sega.diva.model.request.user.PdUnlockRequest
import icu.samnyan.aqua.sega.diva.model.request.user.PreStartRequest
import icu.samnyan.aqua.sega.diva.model.request.user.SpendCreditRequest
import icu.samnyan.aqua.sega.diva.model.request.user.StartRequest
import icu.samnyan.aqua.sega.diva.util.DivaMapper
import jakarta.servlet.http.HttpServletRequest
import lombok.AllArgsConstructor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@RestController
@RequestMapping("/g/diva")
@AllArgsConstructor
class DivaController(
    val gameInitHandler: GameInitHandler,
    val attendHandler: AttendHandler,
    val cardProcedureHandler: CardProcedureHandler,
    val changeNameHandler: ChangeNameHandler,
    val changePasswdHandler: ChangePasswdHandler,
    val initPasswdHandler: InitPasswdHandler,
    val registrationHandler: RegistrationHandler,
    val bannerInfoHandler: BannerInfoHandler,
    val bannerDataHandler: BannerDataHandler,
    val cmPlyInfoHandler: CmPlyInfoHandler,
    val contestInfoHandler: ContestInfoHandler,
    val cstmzItmCtlgHandler: CstmzItmCtlgHandler,
    val cstmzItmNgMdlListHandler: CstmzItmNgMdlListHandler,
    val festaInfoHandler: FestaInfoHandler,
    val ngWordHandler: NgWordHandler,
    val nvRankingHandler: NvRankingHandler,
    val psRankingHandler: PsRankingHandler,
    val pstdHCtrlHandler: PstdHCtrlHandler,
    val pstdItemNgLstHandler: PstdItemNgLstHandler,
    val pvDefChrLstHandler: PvDefChrLstHandler,
    val pvListHandler: PvListHandler,
    val pvNgMdlLstHandler: PvNgMdlLstHandler,
    val qstInfHandler: QstInfHandler,
    val rmtWpLstHandler: RmtWpLstHandler,
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
            "game_init" -> gameInitHandler.handle(mapper.convert(body, GameInitRequest::class.java))
            "attend" -> attendHandler.handle(mapper.convert(body, GameInitRequest::class.java))
            "test" -> gameInitHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "nv_ranking" -> nvRankingHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "ps_ranking" -> psRankingHandler.handle(mapper.convert(body, PsRankingRequest::class.java))

            "pv_list" -> pvListHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "ng_word" -> ngWordHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "rmt_wp_list" -> rmtWpLstHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "festa_info" -> festaInfoHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "contest_info" -> contestInfoHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "pv_def_chr_list" -> pvDefChrLstHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "pv_ng_mdl_list" -> pvNgMdlLstHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "cstmz_itm_ng_mdl_list" -> cstmzItmNgMdlListHandler.handle(mapper.convert(body, BaseRequest::class.java))

            "banner_info" -> bannerInfoHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "banner_data" -> bannerDataHandler.handle(mapper.convert(body, BannerDataRequest::class.java))

            "cm_ply_info" -> cmPlyInfoHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "qst_inf" -> qstInfHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "pstd_h_ctrl" -> pstdHCtrlHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "pstd_item_ng_lst" -> pstdItemNgLstHandler.handle(mapper.convert(body, BaseRequest::class.java))

            "shop_catalog" -> shopCatalogHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "cstmz_itm_ctlg" -> cstmzItmCtlgHandler.handle(mapper.convert(body, BaseRequest::class.java))
            "card_procedure" -> cardProcedureHandler.handle(mapper.convert(body, CardProcedureRequest::class.java))

            "registration" -> registrationHandler.handle(mapper.convert(body, RegistrationRequest::class.java))

            "init_passwd" -> initPasswdHandler.handle(mapper.convert(body, GameInitRequest::class.java))

            "change_passwd" -> changePasswdHandler.handle(mapper.convert(body, ChangePasswdRequest::class.java))

            "change_name" -> changeNameHandler.handle(mapper.convert(body, ChangeNameRequest::class.java))

            "pre_start" -> preStartHandler.handle(mapper.convert(body, PreStartRequest::class.java))
            "start" -> startHandler.handle(mapper.convert(body, StartRequest::class.java))
            "pd_unlock" -> pdUnlockHandler.handle(mapper.convert(body, PdUnlockRequest::class.java))
            "spend_credit" -> spendCreditHandler.handle(mapper.convert(body, SpendCreditRequest::class.java))

            "no_card_end" -> gameInitHandler.handle(mapper.convert(body, GameInitRequest::class.java))
            "end" -> endHandler.handle(mapper.convert(body, StageResultRequest::class.java))
            "get_pv_pd" -> getPvPdHandler.handle(mapper.convert(body, GetPvPdRequest::class.java))
            "buy_module" -> buyModuleHandler.handle(mapper.convert(body, BuyModuleRequest::class.java))

            "buy_cstmz_itm" -> buyCstmzItmHandler.handle(mapper.convert(body, BuyCstmzItmRequest::class.java))

            "shop_exit" -> shopExitHandler.handle(mapper.convert(body, ShopExitRequest::class.java))
            "stage_start" -> stageStartHandler.handle(mapper.convert(body, StageStartRequest::class.java))

            "stage_result" -> stageResultHandler.handle(mapper.convert(body, StageResultRequest::class.java))

            "store_ss" -> gameInitHandler.handle(mapper.convert(body, GameInitRequest::class.java))
            else -> "stat=0"
        }
        val resp = respObj as? String ?: buildResultMap(mapper.toMap(respObj))
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
            "investigate" -> gameInitHandler.handle(mapper.convert(body, BaseRequest::class.java))
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