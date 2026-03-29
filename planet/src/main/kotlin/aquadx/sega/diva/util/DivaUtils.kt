package aquadx.sega.diva.util

import aquadx.sega.diva.PlayerPvRecordRepository
import aquadx.sega.diva.model.common.Edition
import aquadx.sega.diva.model.common.LevelInfo
import aquadx.sega.diva.model.db.userdata.PlayerProfile
import org.springframework.stereotype.Component
import kotlin.math.roundToInt

@Component
class DivaCalculator(private val playerPvRecordRepository: PlayerPvRecordRepository) {
    fun getLevelInfo(profile: PlayerProfile): LevelInfo {
        val recordList = playerPvRecordRepository.findByPdIdAndEdition(profile, Edition.ORIGINAL)
        var totalAttain = 0
        for (record in recordList) {
            totalAttain += record.maxAttain
        }

        val level = totalAttain / 13979
        val exp = ((totalAttain % 13979) / 13979.0f * 100.0f).roundToInt()

        return LevelInfo(level + 1, exp)
    }
}
