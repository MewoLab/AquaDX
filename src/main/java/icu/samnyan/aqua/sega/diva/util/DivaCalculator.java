package icu.samnyan.aqua.sega.diva.util;

import icu.samnyan.aqua.sega.diva.PlayerPvRecordRepository;
import icu.samnyan.aqua.sega.diva.model.common.Edition;
import icu.samnyan.aqua.sega.diva.model.common.LevelInfo;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class DivaCalculator {

    private final PlayerPvRecordRepository playerPvRecordRepository;

    public DivaCalculator(PlayerPvRecordRepository playerPvRecordRepository) {
        this.playerPvRecordRepository = playerPvRecordRepository;
    }

    public LevelInfo getLevelInfo(PlayerProfile profile) {
        List<PlayerPvRecord> recordList = playerPvRecordRepository.findByPdIdAndEdition(profile, Edition.ORIGINAL);
        int totalAttain = 0;
        for (PlayerPvRecord record :
                recordList) {
            totalAttain += record.getMaxAttain();
        }

        int level = totalAttain / 13979;
        int exp = Math.round((totalAttain % 13979) / 13979.0f * 100.0f);

        return new LevelInfo(level + 1, exp);
    }
}
