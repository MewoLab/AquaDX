package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserGeneralDataRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.data.UserRecentRating;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserGeneralData;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserRecentRatingHandler")
public class GetUserRecentRatingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserRecentRatingHandler.class);

    private final BasicMapper mapper;

    private final UserGeneralDataRepository userGeneralDataRepository;

    public GetUserRecentRatingHandler(BasicMapper mapper, UserGeneralDataRepository userGeneralDataRepository) {
        this.mapper = mapper;
        this.userGeneralDataRepository = userGeneralDataRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        UserGeneralData data = userGeneralDataRepository.findByUser_Card_ExtIdAndPropertyKey(userId, "user_recent_rating")
                .orElse(new UserGeneralData(-1, null, "user_recent_rating", ""));

        String[] ratings = data.getPropertyValue().split(",");
        List<UserRecentRating> recentRatingList = new LinkedList<>();
        for (String rating : ratings) {
            String[] v = rating.split(":");
            recentRatingList.add(new UserRecentRating(Integer.parseInt(v[0]), Integer.parseInt(v[1]), Integer.parseInt(v[2])));
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", recentRatingList.size());
        resultMap.put("userRecentRatingList", recentRatingList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
