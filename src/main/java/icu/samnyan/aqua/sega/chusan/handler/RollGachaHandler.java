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
@Component("ChusanRollGachaHandler")
public class RollGachaHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(RollGachaHandler.class);
    private final GameGachaCardService gameGachaCardService;
    private final BasicMapper mapper;

    @Autowired
    public RollGachaHandler(GameGachaCardService gameGachaCardService, BasicMapper mapper) {
        this.gameGachaCardService = gameGachaCardService;
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        int gachaId = ((Number) request.get("gachaId")).intValue();
        int times = ((Number) request.get("times")).intValue();

        List<GameGachaCard> gameGachaCardList = gameGachaCardService.getRandomCards(gachaId, times);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("length", gameGachaCardList.size());
        resultMap.put("gameGachaCardList", gameGachaCardList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
