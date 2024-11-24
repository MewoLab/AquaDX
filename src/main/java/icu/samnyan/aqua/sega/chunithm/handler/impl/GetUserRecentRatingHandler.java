package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserGeneralData;
import icu.samnyan.aqua.sega.chunithm.service.UserGeneralDataService;
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserPlaylog;
import icu.samnyan.aqua.sega.chunithm.service.UserPlaylogService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Return the recent play to calculate rating. Rating base on top 30 songs plus top 10 in recent 30 plays.
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserRecentRatingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserRecentRatingHandler.class);

    private final StringMapper mapper;

    private final UserPlaylogService userPlaylogService;
    private final UserGeneralDataService userGeneralDataService;

    @Autowired
    public GetUserRecentRatingHandler(StringMapper mapper, UserPlaylogService userPlaylogService, UserGeneralDataService userGeneralDataService) {
        this.mapper = mapper;
        this.userPlaylogService = userPlaylogService;
        this.userGeneralDataService = userGeneralDataService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        Optional<UserGeneralData> recentOptional = userGeneralDataService.getByUserIdAndKey(userId, "recent_rating_list");

        List<UserRecentRating> ratingList;
        if(recentOptional.isPresent()) {
            ratingList = new LinkedList<>();
            String val = recentOptional.get().getPropertyValue();
            if(StringUtils.isNotBlank(val) && val.contains(",")) {
                String[] records = val.split(",");
                for (String record :
                        records) {
                    String[] value = record.split(":");
                    ratingList.add(new UserRecentRating(
                            Integer.parseInt(value[0]),
                            Integer.parseInt(value[1]),
                            "1030000",
                            Integer.parseInt(value[2])
                    ));
                }
            }
        } else {
            List<UserPlaylog> top = userPlaylogService.getRecent30Plays(userId);
            ratingList = top.stream().map(log -> new UserRecentRating(log.getMusicId(), log.getLevel(), "1030000", log.getScore()))
                    .collect(Collectors.toList());
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", ratingList.size());
        resultMap.put("userRecentRatingList", ratingList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
