package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserGeneralDataRepository;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserPlaylogRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserGeneralData;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserPlaylog;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserRecentRatingHandler")
public class GetUserRecentRatingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserRecentRatingHandler.class);

    private final BasicMapper mapper;

    private final UserPlaylogRepository userPlaylogRepository;
    private final UserGeneralDataRepository userGeneralDataRepository;

    @Autowired
    public GetUserRecentRatingHandler(BasicMapper mapper, UserPlaylogRepository userPlaylogRepository, UserGeneralDataRepository userGeneralDataRepository) {
        this.mapper = mapper;
        this.userPlaylogRepository = userPlaylogRepository;
        this.userGeneralDataRepository = userGeneralDataRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Optional<UserGeneralData> recentOptional = userGeneralDataRepository.findByUser_Card_ExtIdAndPropertyKey(userId, "recent_rating_list");

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
                            "1000000",
                            Integer.parseInt(value[2])
                    ));
                }
            }
        } else {
            Pageable page = PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "id"));
            List<UserPlaylog> playlogList = userPlaylogRepository.findByUser_Card_ExtId(userId, page).getContent();
            ratingList = playlogList.stream().map(log -> new UserRecentRating(log.getMusicId(), log.getLevel(), "1000000", log.getTechScore()))
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
