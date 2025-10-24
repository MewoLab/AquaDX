package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.PstdItemNgLstResponse;
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class PstdItemNgLstHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PstdItemNgLstHandler.class);

    public String handle(BaseRequest request) {
        PstdItemNgLstResponse response = new PstdItemNgLstResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                DivaDateTimeUtil.getString(LocalDateTime.now()),
                "***",
                "***"
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
