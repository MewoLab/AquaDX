package icu.samnyan.aqua.sega.diva.model.common.collection;

import icu.samnyan.aqua.sega.diva.model.common.Edition;
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerPvRecord;
import lombok.Data;

import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
public class PsRankingCollection {
    private PlayerPvRecord first;
    private PlayerPvRecord second;
    private PlayerPvRecord third;

    public PsRankingCollection(int pvId, Edition edition, List<PlayerPvRecord> list) {
        this.first = new PlayerPvRecord(pvId, edition);
        this.second = new PlayerPvRecord(pvId, edition);
        this.third = new PlayerPvRecord(pvId, edition);
        if (list.size() >= 1) {
            this.first = list.get(0);
        }
        if (list.size() >= 2) {
            this.second = list.get(1);
        }
        if (list.size() >= 3) {
            this.third = list.get(2);
        }
    }
}
