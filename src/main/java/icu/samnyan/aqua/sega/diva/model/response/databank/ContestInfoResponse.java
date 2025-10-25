package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class ContestInfoResponse {
    private LocalDateTime ci_lut;
    private String ci_str;

    public ContestInfoResponse(LocalDateTime ci_lut, String ci_str) {
        this.ci_lut = ci_lut;
        this.ci_str = ci_str;
    }
}
