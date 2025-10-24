package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerPvCustomizeRepository
import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerPvRecordRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.request.ingame.GetPvPdRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.GetPvPdResponse
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvCustomize
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvRecord
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class GetPvPdHandler(
    private val pvRecordRepository: PlayerPvRecordRepository,
    private val pvCustomizeRepository: PlayerPvCustomizeRepository,
    private val playerProfileService: PlayerProfileService
) : BaseHandler() {
    fun handle(request: GetPvPdRequest): Any {
        val profileO = playerProfileService.findByPdId(request.pd_id)
        val pd = StringBuilder()

        for (pvId in request.pd_pv_id_lst) {
            if (pvId == -1) {
                pd.append("***").append(",")
            } else {
                if (profileO.isEmpty) {
                    pd.append("***").append(",")
                } else {
                    val profile = profileO.get()
                    val diff = request.difficulty
                    val difficulty = Difficulty.fromValue(diff)

                    // Myself
                    val edition0 = pvRecordRepository.findByPdIdAndPvIdAndEditionAndDifficulty(
                        profile,
                        pvId,
                        Edition.ORIGINAL,
                        difficulty
                    )
                        .orElseGet(Supplier { PlayerPvRecord(pvId, Edition.ORIGINAL) })

                    val edition1 = pvRecordRepository.findByPdIdAndPvIdAndEditionAndDifficulty(
                        profile,
                        pvId,
                        Edition.EXTRA,
                        difficulty
                    )
                        .orElseGet(Supplier { PlayerPvRecord(pvId, Edition.EXTRA) })

                    // Rival
                    val rivalEdition0: PlayerPvRecord?
                    val rivalEdition1: PlayerPvRecord?
                    if (profile.rivalPdId != -1L) {
                        rivalEdition0 = pvRecordRepository.findByPdId_PdIdAndPvIdAndEditionAndDifficulty(
                            profile.rivalPdId,
                            pvId,
                            Edition.ORIGINAL,
                            difficulty
                        )
                            .orElseGet(Supplier { PlayerPvRecord(pvId, Edition.ORIGINAL) })

                        rivalEdition1 = pvRecordRepository.findByPdId_PdIdAndPvIdAndEditionAndDifficulty(
                            profile.rivalPdId,
                            pvId,
                            Edition.EXTRA,
                            difficulty
                        )
                            .orElseGet(Supplier { PlayerPvRecord(pvId, Edition.EXTRA) })
                    } else {
                        rivalEdition0 = PlayerPvRecord(pvId, Edition.ORIGINAL)
                        rivalEdition1 = PlayerPvRecord(pvId, Edition.EXTRA)
                    }

                    val customize = pvCustomizeRepository.findByPdIdAndPvId(profile, pvId)
                        .orElseGet(Supplier { PlayerPvCustomize(profile, pvId) })

                    val str = getString(
                        edition0,
                        customize,
                        rivalEdition0,
                        profile.rivalPdId
                    ) + "," + getString(edition1, customize, rivalEdition1, profile.rivalPdId)
                    //                logger.info(str);
                    pd.append(encode(str)).append(",")
                }
            }
        }
        pd.deleteCharAt(pd.length - 1)


        val response = GetPvPdResponse(
            request.cmd,
            request.req_id,
            "ok",
            pd.toString(),
            false,
            DivaDateTimeUtil.getString(LocalDateTime.now())
        )

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
    }


    private fun getString(
        record: PlayerPvRecord,
        customize: PlayerPvCustomize,
        rivalRecord: PlayerPvRecord,
        rivalId: Long
    ): String {
        return record.pvId.toString() + "," +
            record.edition.value + "," +
            record.result.value + "," +
            record.maxScore + "," +
            record.maxAttain + "," +
            record.challengeKind.value + "," +
            customize.module + "," +
            customize.customize + "," +
            customize.customizeFlag + "," +
            customize.skin + "," +
            customize.buttonSe + "," +
            customize.slideSe + "," +
            customize.chainSlideSe + "," +
            customize.sliderTouchSe + "," +
            rivalId + "," +
            rivalRecord.maxScore + "," +
            rivalRecord.maxAttain + "," +
            "-1,-1," +
            pvRecordRepository.rankByPvIdAndPdIdAndEditionAndDifficulty(
                record.pvId,
                record.pdId,
                record.edition,
                record.difficulty
            ) + "," +
            record.rgoPurchased + "," +
            record.rgoPlayed
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GetPvPdHandler::class.java)
    }
}
