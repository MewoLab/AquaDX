package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PsRankingResponse {
    private LocalDateTime rnk_ps_lut;
    private LocalDateTime rnk_ps_ts;
    private int rnk_ps_dffclty;
    private String rnk_ps_pv_id;
    private String rnk_ps_edtn1;
    private String rnk_ps_edtn2;
    private String rnk_ps_edtn3;
    private String rnk_ps_scr1;
    private String rnk_ps_scr2;
    private String rnk_ps_scr3;
    private String rnk_ps_nm1;
    private String rnk_ps_nm2;
    private String rnk_ps_nm3;

    public PsRankingResponse(LocalDateTime rnk_ps_lut, LocalDateTime rnk_ps_ts, int rnk_ps_dffclty, String rnk_ps_pv_id, String rnk_ps_edtn1, String rnk_ps_edtn2, String rnk_ps_edtn3, String rnk_ps_scr1, String rnk_ps_scr2, String rnk_ps_scr3, String rnk_ps_nm1, String rnk_ps_nm2, String rnk_ps_nm3) {
        this.rnk_ps_lut = rnk_ps_lut;
        this.rnk_ps_ts = rnk_ps_ts;
        this.rnk_ps_dffclty = rnk_ps_dffclty;
        this.rnk_ps_pv_id = rnk_ps_pv_id;
        this.rnk_ps_edtn1 = rnk_ps_edtn1;
        this.rnk_ps_edtn2 = rnk_ps_edtn2;
        this.rnk_ps_edtn3 = rnk_ps_edtn3;
        this.rnk_ps_scr1 = rnk_ps_scr1;
        this.rnk_ps_scr2 = rnk_ps_scr2;
        this.rnk_ps_scr3 = rnk_ps_scr3;
        this.rnk_ps_nm1 = rnk_ps_nm1;
        this.rnk_ps_nm2 = rnk_ps_nm2;
        this.rnk_ps_nm3 = rnk_ps_nm3;
    }
}
