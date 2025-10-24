package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaModuleRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.gamedata.DivaModule;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.ShopCatalogResponse;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class ShopCatalogHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShopCatalogHandler.class);
    private final DivaModuleRepository moduleRepository;

    public String handle(BaseRequest request) {
        List<DivaModule> moduleList = moduleRepository.findAll();

        ShopCatalogResponse response = new ShopCatalogResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                LocalDateTime.now(),
                URIEncoder.encode(moduleList.stream().map(DivaModule::toInternal).map(URIEncoder::encode)
                        .collect(Collectors.joining(",")))
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
