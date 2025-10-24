package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.databank.BannerDataRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.BannerDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class BannerDataHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(BannerDataHandler.class);

    public String handle(BannerDataRequest request) {
        BannerDataResponse response = new BannerDataResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                LocalDateTime.now(),
                "***",
                "***",
                request.getBd_id()
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
