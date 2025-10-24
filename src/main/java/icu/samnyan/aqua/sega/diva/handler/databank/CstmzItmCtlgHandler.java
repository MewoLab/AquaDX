package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaCustomizeRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.gamedata.DivaCustomize;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.CstmzItmCtlgResponse;
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil;
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
public class CstmzItmCtlgHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(CstmzItmCtlgHandler.class);

    private final DivaCustomizeRepository customizeRepository;

    public String handle(BaseRequest request) {
        List<DivaCustomize> customizeList = customizeRepository.findAll();

        CstmzItmCtlgResponse response = new CstmzItmCtlgResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                DivaDateTimeUtil.getString(LocalDateTime.now()),
                URIEncoder.encode(customizeList.stream().map(DivaCustomize::toInternal).map(URIEncoder::encode)
                        .collect(Collectors.joining(",")))
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
