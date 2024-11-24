package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserMusicItemRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserMusicItem;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserMusicItemHandler")
public class GetUserMusicItemHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMusicItemHandler.class);

    private final BasicMapper mapper;

    private final UserMusicItemRepository userMusicItemRepository;

    @Autowired
    public GetUserMusicItemHandler(BasicMapper mapper, UserMusicItemRepository userMusicItemRepository) {
        this.mapper = mapper;
        this.userMusicItemRepository = userMusicItemRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        Integer maxCount = (Integer) request.get("maxCount");
        Integer nextIndex = (Integer) request.get("nextIndex");

        int pageNum = nextIndex / maxCount;

        Page<UserMusicItem> dbPage = userMusicItemRepository.findByUser_Card_ExtId(userId, PageRequest.of(pageNum, maxCount));

        long currentIndex = maxCount * pageNum + dbPage.getNumberOfElements();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", dbPage.getNumberOfElements());
        resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : currentIndex);
        resultMap.put("userMusicItemList", dbPage.getContent());

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
