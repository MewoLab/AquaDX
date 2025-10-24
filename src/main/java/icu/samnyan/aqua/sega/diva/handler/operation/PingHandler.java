package icu.samnyan.aqua.sega.diva.handler.operation;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.operation.PingResponse;
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository;
import icu.samnyan.aqua.sega.general.model.PropertyEntry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class PingHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PingHandler.class);
    private final PropertyEntryRepository propertyEntryRepository;

    public String handle(BaseRequest request) {

        PropertyEntry news = propertyEntryRepository.findByPropertyKey("diva_news").orElseGet(() -> new PropertyEntry("diva_news", "xxx"));
        PropertyEntry warning = propertyEntryRepository.findByPropertyKey("diva_warning").orElseGet(() -> new PropertyEntry("diva_warning", "xxx"));

        PingResponse response = new PingResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                news.getPropertyValue(),
                warning.getPropertyValue()
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
