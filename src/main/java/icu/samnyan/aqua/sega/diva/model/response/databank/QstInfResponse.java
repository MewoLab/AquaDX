package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class QstInfResponse {
    private String qi_lut;
    private String qhi_str;
    private String qrai_str;

    public QstInfResponse(String qi_lut, String qhi_str, String qrai_str) {
        this.qi_lut = qi_lut;
        this.qhi_str = qhi_str;
        this.qrai_str = qrai_str;
    }
}
