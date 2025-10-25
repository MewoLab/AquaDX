package icu.samnyan.aqua.sega.diva.model.response.boot;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class AttendResponse {
    private String atnd_prm1;
    private String atnd_prm2;
    private String atnd_prm3;
    private LocalDateTime atnd_lut;

    public AttendResponse(String atnd_prm1, String atnd_prm2, String atnd_prm3, LocalDateTime atnd_lut) {
        this.atnd_prm1 = atnd_prm1;
        this.atnd_prm2 = atnd_prm2;
        this.atnd_prm3 = atnd_prm3;
        this.atnd_lut = atnd_lut;
    }
}
