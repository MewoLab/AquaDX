package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.dao.gamedata.FestaRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.collection.FestaCollection;
import icu.samnyan.aqua.sega.diva.model.gamedata.Festa;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.FestaInfoResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class FestaInfoHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(FestaInfoHandler.class);
    private final FestaRepository festaRepository;

    public String handle(BaseRequest request) {
        List<Festa> festaList = festaRepository.findTop2ByEnableOrderByCreateDateDesc(true);
        FestaCollection collection = new FestaCollection(festaList);

        FestaInfoResponse response = new FestaInfoResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                collection.getIds(),
                collection.getNames(),
                collection.getKinds(),
                collection.getDiffs(),
                collection.getPvIds(),
                collection.getAttr(),
                collection.getAddVps(),
                collection.getVpMultipliers(),
                collection.getStarts(),
                collection.getEnds(),
                collection.getLastUpdateTime()
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
