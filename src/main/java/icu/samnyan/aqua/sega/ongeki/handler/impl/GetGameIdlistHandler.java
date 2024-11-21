package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository;
import icu.samnyan.aqua.sega.general.model.PropertyEntry;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.GameIdListItem;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetGameIdlistHandler")
public class GetGameIdlistHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameIdlistHandler.class);

    private final BasicMapper mapper;

    private final PropertyEntryRepository propertyEntryRepository;

    @Autowired
    public GetGameIdlistHandler(BasicMapper mapper, PropertyEntryRepository propertyEntryRepository) {
        this.mapper = mapper;
        this.propertyEntryRepository = propertyEntryRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        Integer type = (Integer) request.get("type");

        Optional<PropertyEntry> propertyEntryOptional;
        if(type == 1) {
            propertyEntryOptional = propertyEntryRepository.findByPropertyKey("ongeki_music_ng_list");
        } else if(type == 2) {
            propertyEntryOptional = propertyEntryRepository.findByPropertyKey("ongeki_music_recommend_list");
        } else {
            propertyEntryOptional = Optional.empty();
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);

        if (propertyEntryOptional.isPresent()) {
            String value = propertyEntryOptional.get().getPropertyValue();

            if(StringUtils.isNotBlank(value) && value.contains(",")) {
                String[] ids = value.split(",");
                List<GameIdListItem> idList = new LinkedList<>();

                for (String id : ids) {
                    try {
                        idList.add(new GameIdListItem(Integer.parseInt(id), type));
                    } catch (NumberFormatException ignored) {
                    }
                }
                resultMap.put("length", idList.size());
                resultMap.put("gameIdlistList", idList);

            } else {
                resultMap.put("length", 0);
                resultMap.put("gameIdlistList", new List[]{});
            }
        } else {
            resultMap.put("length", 0);
            resultMap.put("gameIdlistList", new List[]{});
        }

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
