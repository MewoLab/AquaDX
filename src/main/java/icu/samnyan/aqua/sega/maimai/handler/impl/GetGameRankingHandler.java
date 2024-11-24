package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository;
import icu.samnyan.aqua.sega.general.model.PropertyEntry;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.data.GameRanking;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetGameRankingHandler")
public class GetGameRankingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameRankingHandler.class);

    private final BasicMapper mapper;

    private final PropertyEntryRepository propertyEntryRepository;

    @Autowired
    public GetGameRankingHandler(BasicMapper mapper, PropertyEntryRepository propertyEntryRepository) {
        this.mapper = mapper;
        this.propertyEntryRepository = propertyEntryRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        int type = ((Number) request.get("type")).intValue();

        String dataName = "maimai_game_ranking_";

        Optional<PropertyEntry> ranking = propertyEntryRepository.findByPropertyKey(dataName + type);

        List<GameRanking> gameRankingList = new ArrayList<>();
        if (ranking.isPresent()) {
            String rankingList = ranking.get().getPropertyValue();
            String[] r = rankingList.split(",");
            for (String i : r) {
                String[] v = i.split(":");
                if (v.length == 2) {
                    gameRankingList.add(new GameRanking(Integer.parseInt(v[0]), Integer.parseInt(v[1]), ""));
                } else if (v.length == 3) {
                    gameRankingList.add(new GameRanking(Integer.parseInt(v[0]), Integer.parseInt(v[1]), v[2]));

                }
            }
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);
        resultMap.put("gameRankingList", gameRankingList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
