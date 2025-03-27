package icu.samnyan.aqua.sega.ongeki.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserPreviewResp {

    private long userId = 0;
    @JsonProperty("isLogin")
    private boolean isLogin = false;
    private String lastLoginDate = null;
    private String userName = "";
    private int reincarnationNum = 0;
    private int level = 0;
    private long exp = 0;
    private long playerRating = 0;
    private long newPlayerRating = 0;
    private String lastGameId = "";
    private String lastRomVersion = "";
    private String lastDataVersion = "";
    private String lastPlayDate = null;
    private int nameplateId = 0;
    private int trophyId = 0;
    private int cardId = 0;
    private int dispPlayerLv = 0;
    private int dispRating = 0;
    private int dispBP = 0;
    private int headphone = 0;
    private int banStatus = 0;
    @JsonProperty("isWarningConfirmed")
    private boolean isWarningConfirmed = false;
    private int lastEmoneyBrand = 0;
    private int lastEmoneyCredit = 0;
}
