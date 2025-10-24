package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.NvRankingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class NvRankingHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(NvRankingHandler.class);

    public String handle(BaseRequest request) {
        NvRankingResponse response = new NvRankingResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                null,
                null,
                null,
                null
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
