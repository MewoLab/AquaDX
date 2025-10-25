package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class BannerDataResponse {
    private LocalDateTime bd_ut;
    private String bd_ti;
    private String bd_hs;
    private int bd_id;

    public BannerDataResponse(LocalDateTime bd_ut, String bd_ti, String bd_hs, int bd_id) {
        this.bd_ut = bd_ut;
        this.bd_ti = bd_ti;
        this.bd_hs = bd_hs;
        this.bd_id = bd_id;
    }
}

