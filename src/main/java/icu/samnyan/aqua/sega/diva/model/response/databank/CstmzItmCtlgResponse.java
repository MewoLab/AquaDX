package icu.samnyan.aqua.sega.diva.model.response.databank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class CstmzItmCtlgResponse {
    private String cstmz_itm_ctlg_lut;
    private String cstmz_itm_ctlg;

    public CstmzItmCtlgResponse(String cstmz_itm_ctlg_lut, String cstmz_itm_ctlg) {
        this.cstmz_itm_ctlg_lut = cstmz_itm_ctlg_lut;
        this.cstmz_itm_ctlg = cstmz_itm_ctlg;
    }
}
