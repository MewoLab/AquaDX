package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserMusicDetailRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.UserMusicListItem;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserMusicDetail;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import icu.samnyan.aqua.spring.data.OffsetPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserMusicHandler")
public class GetUserMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMusicHandler.class);

    private final BasicMapper mapper;

    private final UserMusicDetailRepository userMusicDetailRepository;

    @Autowired
    public GetUserMusicHandler(BasicMapper mapper, UserMusicDetailRepository userMusicDetailRepository) {
        this.mapper = mapper;
        this.userMusicDetailRepository = userMusicDetailRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        Integer maxCount = (Integer) request.get("maxCount");
        Integer currentIndex = (Integer) request.get("nextIndex");
        if(currentIndex < 0) {
            currentIndex = 0;
        }

        Page<UserMusicDetail> dbPage = userMusicDetailRepository
                .findByUser_Card_ExtId(userId, OffsetPageRequest.of(currentIndex, maxCount, Sort.by("musicId")));

        Map<Integer, UserMusicListItem> userMusicMap = new LinkedHashMap<>();
        dbPage.getContent().forEach(userMusicDetail -> {
            UserMusicListItem list;
            if (userMusicMap.containsKey(userMusicDetail.getMusicId())) {
                list = userMusicMap.get(userMusicDetail.getMusicId());
            } else {
                list = new UserMusicListItem(0, new ArrayList<>());
                userMusicMap.put(userMusicDetail.getMusicId(), list);
            }
            list.getUserMusicDetailList().add(userMusicDetail);
            list.setLength(list.getUserMusicDetailList().size());
        });

        // Someone report that ongeki also has the score missing problem
        int lastListSize = 0;
        if(dbPage.getNumberOfElements() >= maxCount) {
            // Get last key
            int lastMusicId = userMusicMap.keySet().stream().reduce((a, b) -> b).orElseThrow();
            List<UserMusicDetail> lastList = userMusicMap.get(lastMusicId).getUserMusicDetailList();
            lastListSize = lastList.size();
            // Remove last one from map
            userMusicMap.remove(lastMusicId);
        }

        long nextIndex = currentIndex + dbPage.getNumberOfElements() - lastListSize;

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userMusicMap.size());
        resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : nextIndex);
        resultMap.put("userMusicList", userMusicMap.values());

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
