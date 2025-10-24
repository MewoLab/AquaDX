package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.CstmzItmNgMdlListResponse;
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class CstmzItmNgMdlListHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(CstmzItmCtlgHandler.class);

    public String handle(BaseRequest request) {
        CstmzItmNgMdlListResponse response = new CstmzItmNgMdlListResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                DivaDateTimeUtil.getString(LocalDateTime.now()),
                "***"
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
