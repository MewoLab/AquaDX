package icu.samnyan.aqua.sega.diva.handler.boot;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.attend.DispersalParameter;
import icu.samnyan.aqua.sega.diva.model.common.attend.EtcParameter;
import icu.samnyan.aqua.sega.diva.model.common.attend.GameBalanceParameter;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.boot.AttendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class AttendHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(AttendHandler.class);

    public String handle(BaseRequest request) {
        AttendResponse response = new AttendResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                new EtcParameter().toInternal(),
                new DispersalParameter().toInternal(),
                new GameBalanceParameter().toInternal(),
                LocalDateTime.now()
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
