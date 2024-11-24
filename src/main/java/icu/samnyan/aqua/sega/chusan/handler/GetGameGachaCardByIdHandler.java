package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.gamedata.GameGachaCard;
import icu.samnyan.aqua.sega.chusan.service.GameGachaCardService;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanGetGameGachaCardByIdHandler")
public class GetGameGachaCardByIdHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameGachaCardByIdHandler.class);
    private final GameGachaCardService gameGachaCardService;
    private final BasicMapper mapper;

    @Autowired
    public GetGameGachaCardByIdHandler(GameGachaCardService gameGachaCardService, BasicMapper mapper) {
        this.gameGachaCardService = gameGachaCardService;
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        int gachaId = ((Number) request.get("gachaId")).intValue();

        List<GameGachaCard> gameGachaCardList = gameGachaCardService.getByGachaId(gachaId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("gachaId", gachaId);
        resultMap.put("length", gameGachaCardList.size());
        resultMap.put("isPickup", false);
        resultMap.put("gameGachaCardList", gameGachaCardList);
        resultMap.put("emissionList", List.of());
        resultMap.put("afterCalcList", List.of());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
