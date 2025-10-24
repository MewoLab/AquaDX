package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.dao.gamedata.ContestRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.gamedata.Contest;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.ContestInfoResponse;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class ContestInfoHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(CmPlyInfoHandler.class);
    private final ContestRepository contestRepository;

    public String handle(BaseRequest request) {
        List<Contest> contestList = contestRepository.findTop8ByEnable(true);
        String ci_str = "***";
        if (!contestList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            contestList.forEach(x -> sb.append(URIEncoder.encode(x.getString())).append(","));
            sb.append("%2A%2A%2A,".repeat(Math.max(0, 8 - contestList.size())));
            sb.deleteCharAt(sb.length() - 1);
            ci_str = sb.toString();
        }
        ContestInfoResponse response = new ContestInfoResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                LocalDateTime.now(),
                ci_str
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
