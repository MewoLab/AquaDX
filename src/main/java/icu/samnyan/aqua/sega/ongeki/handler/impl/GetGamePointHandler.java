package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.gamedata.GamePointRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.gamedata.GamePoint;
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
@Component("OngekiGetGamePointHandler")
public class GetGamePointHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGamePointHandler.class);

    private final BasicMapper mapper;

    private final GamePointRepository gamePointRepository;

    @Autowired
    public GetGamePointHandler(BasicMapper mapper, GamePointRepository gamePointRepository) {
        this.mapper = mapper;
        this.gamePointRepository = gamePointRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        // This value is always false
        Boolean isAllGP = (Boolean) request.get("isAllGP");

        Map<String, Object> resultMap = new LinkedHashMap<>();

        List<GamePoint> gpList = gamePointRepository.findAll();

        resultMap.put("length", gpList.size());
        resultMap.put("gamePointList", gpList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
