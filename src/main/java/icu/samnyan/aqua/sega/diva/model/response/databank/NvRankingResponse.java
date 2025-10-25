package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class NvRankingResponse {
    private String rnk_nv_tag_str;
    private LocalDateTime rnk_nv_ts;
    private String rnk_nv_data;
    private LocalDateTime rnk_nv_lut;

    public NvRankingResponse(String rnk_nv_tag_str, LocalDateTime rnk_nv_ts, String rnk_nv_data, LocalDateTime rnk_nv_lut) {
        this.rnk_nv_tag_str = rnk_nv_tag_str;
        this.rnk_nv_ts = rnk_nv_ts;
        this.rnk_nv_data = rnk_nv_data;
        this.rnk_nv_lut = rnk_nv_lut;
    }
}
