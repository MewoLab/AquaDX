package icu.samnyan.aqua.sega.chusan

import ext.*
import icu.samnyan.aqua.sega.chunithm.handler.impl.GetGameIdlistHandler
import icu.samnyan.aqua.sega.chusan.handler.*
import icu.samnyan.aqua.sega.general.BaseHandler
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import kotlin.reflect.full.declaredMemberProperties


/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Suppress("unused")
@RestController
@API(value = ["/g/chu3/{version}/ChuniServlet", "/g/chu3/{version}"])
class ChusanServletController(
    val gameLogin: GameLoginHandler,
    val gameLogout: GameLogoutHandler,
    val getGameCharge: GetGameChargeHandler,
    val getGameEvent: GetGameEventHandler,
    val getGameIdlist: GetGameIdlistHandler,
    val getGameRanking: GetGameRankingHandler,
    val getGameSetting: GetGameSettingHandler,
    val getTeamCourseRule: GetTeamCourseRuleHandler,
    val getTeamCourseSetting: GetTeamCourseSettingHandler,
    val getUserActivity: GetUserActivityHandler,
    val getUserCharacter: GetUserCharacterHandler,
    val getUserCharge: GetUserChargeHandler,
    val getUserCourse: GetUserCourseHandler,
    val getUserData: GetUserDataHandler,
    val getUserDuel: GetUserDuelHandler,
    val getUserFavoriteItem: GetUserFavoriteItemHandler,
    val getUserItem: GetUserItemHandler,
    val getUserLoginBonus: GetUserLoginBonusHandler,
    val getUserMapArea: GetUserMapAreaHandler,
    val getUserMusic: GetUserMusicHandler,
    val getUserOption: GetUserOptionHandler,
    val getUserPreview: GetUserPreviewHandler,
    val getUserRecentRating: GetUserRecentRatingHandler,
    val getUserRegion: GetUserRegionHandler,
    val getUserRivalData: GetUserRivalDataHandler,
    val getUserRivalMusic: GetUserRivalMusicHandler,
    val getUserSymbolChatSetting: GetUserSymbolChatSettingHandler,
    val getUserNetBattleData: GetUserNetBattleDataHandler,
    val getUserTeam: GetUserTeamHandler,
    val upsertUserAll: UpsertUserAllHandler,
    val upsertUserChargelog: UpsertUserChargelogHandler,
    val getGameGacha: GetGameGachaHandler,
    val getGameGachaCardById: GetGameGachaCardByIdHandler,
    val getUserCardPrintError: GetUserCardPrintErrorHandler,
    val cmGetUserPreview: CMGetUserPreviewHandler,
    val cmGetUserData: CMGetUserDataHandler,
    val cmGetUserCharacter: CMGetUserCharacterHandler,
    val getUserGacha: GetUserGachaHandler,
    val getUserPrintedCard: GetUserPrintedCardHandler,
    val cmGetUserItem: CMGetUserItemHandler,
    val rollGacha: RollGachaHandler,
    val cmUpsertUserGacha: CMUpsertUserGachaHandler,
    val cmUpsertUserPrintSubtract: CMUpsertUserPrintSubtractHandler,
    val cmUpsertUserPrintCancel: CMUpsertUserPrintCancelHandler,
    val beginMatching: BeginMatchingHandler,
    val endMatching: EndMatchingHandler,
    val getMatchingState: GetMatchingStateHandler,
    val removeMatchingMember: RemoveMatchingMemberHandler,

    // Luminous
    val getUserCMission: GetUserCMissionHandler,
    val getUserNetBattleRankingInfo: GetUserNetBattleRankingInfoHandler,
    val getGameMapAreaCondition: GetGameMapAreaConditionHandler
) {
    val logger = LoggerFactory.getLogger(ChusanServletController::class.java)

    val getUserCtoCPlay = BaseHandler { """{"userId":"${it["userId"]}","orderBy":"0","count":"0","userCtoCPlayList":[]}""" }
    val cmUpsertUserPrint = BaseHandler { """{"returnCode":1,"orderId":"0","serialId":"FAKECARDIMAG12345678","apiName":"CMUpsertUserPrintApi"}""" }
    val cmUpsertUserPrintlog = BaseHandler { """{"returnCode":1,"orderId":"0","serialId":"FAKECARDIMAG12345678","apiName":"CMUpsertUserPrintlogApi"}""" }

    val endpointList = mutableListOf(
        "GameLoginApi", "GameLogoutApi", "GetGameChargeApi", "GetGameEventApi", "GetGameIdlistApi",
        "GetGameRankingApi", "GetGameSettingApi", "GetTeamCourseRuleApi", "GetTeamCourseSettingApi", "GetUserActivityApi",
        "GetUserCharacterApi", "GetUserChargeApi", "GetUserCourseApi", "GetUserDataApi", "GetUserDuelApi",
        "GetUserFavoriteItemApi", "GetUserItemApi", "GetUserLoginBonusApi", "GetUserMapAreaApi", "GetUserMusicApi",
        "GetUserOptionApi", "GetUserPreviewApi", "GetUserRecentRatingApi", "GetUserRegionApi", "GetUserRivalDataApi",
        "GetUserRivalMusicApi", "GetUserTeamApi", "GetUserSymbolChatSettingApi", "GetUserNetBattleDataApi",
        "UpsertClientBookkeepingApi", "UpsertClientDevelopApi", "UpsertClientErrorApi", "UpsertClientSettingApi",
        "UpsertClientTestmodeApi", "UpsertUserAllApi", "UpsertUserChargelogApi", "CreateTokenApi", "RemoveTokenApi",
        "UpsertClientUploadApi", "MatchingServer/Ping", "MatchingServer/BeginMatchingApi", "MatchingServer/EndMatchingApi",
        "MatchingServer/RemoveMatchingMemberApi", "MatchingServer/GetMatchingStateApi", "GetGameGachaApi",
        "GetGameGachaCardByIdApi", "GetUserCardPrintErrorApi", "CMGetUserCharacterApi", "CMGetUserDataApi",
        "GetUserGachaApi", "CMGetUserItemApi", "CMGetUserPreviewApi", "GetUserPrintedCardApi", "PrinterLoginApi",
        "PrinterLogoutApi", "RollGachaApi", "CMUpsertUserGachaApi", "CMUpsertUserPrintApi", "CMUpsertUserPrintCancelApi",
        "CMUpsertUserPrintlogApi", "CMUpsertUserPrintSubtractApi",

        // SDGS Exclusive
        "GetUserCtoCPlayApi", "GetUserCMissionApi", "GetUserNetBattleRankingInfoApi", "GetGameMapAreaConditionApi")

    val noopEndpoint = endpointList.popAll("UpsertClientBookkeepingApi", "UpsertClientDevelopApi", "UpsertClientErrorApi",
        "UpsertClientSettingApi", "UpsertClientTestmodeApi", "CreateTokenApi", "RemoveTokenApi", "UpsertClientUploadApi",
        "MatchingServer/Ping", "PrinterLoginApi", "PrinterLogoutApi", "Ping")

    val members = this::class.declaredMemberProperties
    val handlers: Map<String, BaseHandler> = endpointList.associateWith { api ->
        val name = api.replace("Api", "").replace("MatchingServer/", "").lowercase()
        (members.find { it.name.lowercase() == name } ?: members.find { it.name.lowercase() == name.replace("cm", "") })
            ?.let { it.call(this) as BaseHandler }
            ?: throw IllegalArgumentException("Chu3: No handler found for $api")
    }

    @API("/{endpoint}")
    fun handle(@PV endpoint: Str, @RB request: MutableMap<Str, Any>, @PV version: Str): Any {
        var api = endpoint
        request["version"] = version

        // Export version
        if (api.endsWith("C3Exp")) {
            api = api.removeSuffix("C3Exp")
            request["c3exp"] = true
        }

        logger.info("Chu3 $api : $request")

        if (api in noopEndpoint) {
            return """{"returnCode":"1"}"""
        }

        return handlers[api]?.handle(request) ?: {
            logger.warn("Chu3 $api not found")
            """{"returnCode":"1","apiName":"$api"}"""
        }
    }
}
