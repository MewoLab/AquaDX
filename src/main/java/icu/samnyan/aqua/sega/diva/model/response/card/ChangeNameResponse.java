package icu.samnyan.aqua.sega.diva.model.response.card;

import icu.samnyan.aqua.sega.diva.model.common.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class ChangeNameResponse {
    private Result cd_adm_result;
    private int accept_idx;
    private long pd_id;
    private String player_name;

    public ChangeNameResponse(Result cd_adm_result, int accept_idx, long pd_id, String player_name) {
        this.cd_adm_result = cd_adm_result;
        this.accept_idx = accept_idx;
        this.pd_id = pd_id;
        this.player_name = player_name;
    }

    public ChangeNameResponse(Result cd_adm_result) {
        this.cd_adm_result = cd_adm_result;
    }
}
