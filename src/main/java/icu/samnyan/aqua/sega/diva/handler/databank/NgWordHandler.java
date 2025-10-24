package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class NgWordHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(NgWordHandler.class);

    public String handle(BaseRequest request) {
        BaseResponse response = new BaseResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok");

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
