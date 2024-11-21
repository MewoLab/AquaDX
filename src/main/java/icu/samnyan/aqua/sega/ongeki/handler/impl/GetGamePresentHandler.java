package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.gamedata.GamePresentRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.gamedata.GamePresent;
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
@Component("OngekiGetGamePresentHandler")
public class GetGamePresentHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGamePresentHandler.class);

    private final BasicMapper mapper;

    private final GamePresentRepository gamePresentRepository;

    @Autowired
    public GetGamePresentHandler(BasicMapper mapper, GamePresentRepository gamePresentRepository) {
        this.mapper = mapper;
        this.gamePresentRepository = gamePresentRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        Boolean isAllPresent = (Boolean) request.get("isAllPresent");

        List<GamePresent> presentList = gamePresentRepository.findAll();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("length", presentList.size());
        resultMap.put("gamePresentList", presentList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
