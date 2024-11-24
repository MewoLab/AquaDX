package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository;
import icu.samnyan.aqua.sega.general.model.PropertyEntry;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.GameRankingItem;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Get game music hot ranking list.
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetGameRankingHandler")
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
        // 1 is current, 2 is old
        // See ADT_Ranking.cs
        Integer type = (Integer) request.get("type");
        Optional<PropertyEntry> propertyEntryOptional;
        if(type == 1) {
            propertyEntryOptional = propertyEntryRepository.findByPropertyKey("ongeki_music_ranking_current");
        } else if(type == 2) {
            propertyEntryOptional = propertyEntryRepository.findByPropertyKey("ongeki_music_ranking_old");
        } else {
            propertyEntryOptional = Optional.empty();
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);

        if (propertyEntryOptional.isPresent()) {
            String value = propertyEntryOptional.get().getPropertyValue();

            if(StringUtils.isNotBlank(value) && value.contains(",")) {
                String[] ids = value.split(",");
                List<GameRankingItem> list = new LinkedList<>();

                for (String id : ids) {
                    try {
                        list.add(new GameRankingItem(Integer.parseInt(id), 0, ""));
                    } catch (NumberFormatException ignored) {
                    }
                    // in ADT_Ranking.cs, the game read this array and expected it has 10 value here.
                    while (list.size() < 10) {
                        list.add(new GameRankingItem(0, 0, ""));
                    }
                }

                resultMap.put("gameRankingList", list);

            } else {
                resultMap.put("gameRankingList", new List[]{});
            }
        } else {
            resultMap.put("gameRankingList", new List[]{});
        }

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
