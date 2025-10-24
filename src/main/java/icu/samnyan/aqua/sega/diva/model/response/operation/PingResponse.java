package icu.samnyan.aqua.sega.diva.model.response.operation;

import icu.samnyan.aqua.sega.diva.model.response.BaseResponse;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class PingResponse extends BaseResponse {
    private String ping_b_msg = URIEncoder.encode("Server Running                  No other news");
    private String ping_m_msg = URIEncoder.encode("Network Service Running");
    private String atnd_lut;
    private String fi_lut;
    private String ci_lut;
    private String qi_lut;
    private String pvl_lut;
    private String pdcl_lut;
    private String pnml_lut;
    private String cinml_lut;
    private String rwl_lut;
    private String bdlol_lut;
    private String shp_ctlg_lut;
    private String cstmz_itm_ctlg_lut;
    private String ngwl_lut;
    private String rnk_nv_lut;
    private String rnk_ps_lut;
    private String bi_lut;
    private String cpi_lut;
    private String p_std_hc_lut;
    private String p_std_i_n_lut;

    private String req_inv_cmd_num;
    private String req_inv_cmd_prm1;
    private String req_inv_cmd_prm2;
    private String req_inv_cmd_prm3;
    private String req_inv_cmd_prm4;

    private boolean pow_save_flg = false;

    private Integer nblss_dnt_p = 0;
    private Integer nblss_ltt_rl_vp = 0;
    private Integer nblss_ex_ltt_flg = 0;

    private String nblss_dnt_st_tm;
    private String nblss_dnt_ed_tm;
    private String nblss_ltt_st_tm;
    private String nblss_ltt_ed_tm;

    public PingResponse(String cmd, String req_id, String stat) {
        super(cmd, req_id, stat);
    }

    public PingResponse(String cmd, String req_id, String stat, String ping_b_msg, String ping_m_msg) {
        super(cmd, req_id, stat);
        this.ping_b_msg = ping_b_msg;
        this.ping_m_msg = ping_m_msg;
    }
}
