package icu.samnyan.aqua.sega.diva.handler.boot;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.boot.GameInitResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GameInitHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameInitHandler.class);

    public String handle(BaseRequest request) {
        GameInitResponse response = new GameInitResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                "0,0",
                "FFFF"
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
