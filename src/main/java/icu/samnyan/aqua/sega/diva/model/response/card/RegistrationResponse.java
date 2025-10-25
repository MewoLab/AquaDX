package icu.samnyan.aqua.sega.diva.model.response.card;

import icu.samnyan.aqua.sega.diva.model.common.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Getter
@Setter
public class RegistrationResponse {
    private Result cd_adm_result;
    private long pd_id;

    public RegistrationResponse(Result cd_adm_result, long pd_id) {
        this.cd_adm_result = cd_adm_result;
        this.pd_id = pd_id;
    }
}
