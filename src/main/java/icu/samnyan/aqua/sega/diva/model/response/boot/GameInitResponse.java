package icu.samnyan.aqua.sega.diva.model.response.boot;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class GameInitResponse {
    private String db_close;
    private String retry_time;

    public GameInitResponse(String db_close, String retry_time) {
        this.db_close = db_close;
        this.retry_time = retry_time;
    }
}
