package icu.samnyan.aqua.sega.ongeki.model.gamedata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OngekiFumenScore {
    private int musicId;
    private int difficultId;
    private String romVersionCode;
    private int score;
    public int platinumScoreMax;
    public int platinumScoreStar;
}
