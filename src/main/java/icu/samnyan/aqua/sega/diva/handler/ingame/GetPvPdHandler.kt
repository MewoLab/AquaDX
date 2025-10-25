package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.GetPvPdRequest
import icu.samnyan.aqua.sega.diva.model.GetPvPdResponse
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerPvCustomize
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerPvRecord
import icu.samnyan.aqua.sega.diva.util.DivaTime
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class GetPvPdHandler(val db: DivaRepos) {
    fun handle(request: GetPvPdRequest): Any {
        val profileO = db.profile.findByPdId(request.pd_id)
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
                    val edition0 = db.pvRecord.findByPdIdAndPvIdAndEditionAndDifficulty(
                        profile,
                        pvId,
                        Edition.ORIGINAL,
                        difficulty
                    )
                        .orElseGet(Supplier { PlayerPvRecord(pvId, Edition.ORIGINAL) })

                    val edition1 = db.pvRecord.findByPdIdAndPvIdAndEditionAndDifficulty(
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
                        rivalEdition0 = db.pvRecord.findByPdId_PdIdAndPvIdAndEditionAndDifficulty(
                            profile.rivalPdId,
                            pvId,
                            Edition.ORIGINAL,
                            difficulty
                        )
                            .orElseGet(Supplier { PlayerPvRecord(pvId, Edition.ORIGINAL) })

                        rivalEdition1 = db.pvRecord.findByPdId_PdIdAndPvIdAndEditionAndDifficulty(
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

                    val customize = db.pvCustomize.findByPdIdAndPvId(profile, pvId)
                        .orElseGet(Supplier { PlayerPvCustomize(profile, pvId) })

                    val str = getString(
                        edition0,
                        customize,
                        rivalEdition0,
                        profile.rivalPdId
                    ) + "," + getString(edition1, customize, rivalEdition1, profile.rivalPdId)
                    pd.append(encode(str)).append(",")
                }
            }
        }
        pd.deleteCharAt(pd.length - 1)


        return GetPvPdResponse(
            pd.toString(),
            false,
            DivaTime.now
        )
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
            db.pvRecord.rankByPvIdAndPdIdAndEditionAndDifficulty(
                record.pvId,
                record.pdId,
                record.edition,
                record.difficulty
            ) + "," +
            record.rgoPurchased + "," +
            record.rgoPlayed
    }
}
