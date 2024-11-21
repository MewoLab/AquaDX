package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserEventMusicRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserEventMusic;
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
@Component("OngekiGetUserEventMusicHandler")
public class GetUserEventMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserEventMusicHandler.class);

    private final BasicMapper mapper;

    private final UserEventMusicRepository userEventMusicRepository;

    @Autowired
    public GetUserEventMusicHandler(BasicMapper mapper, UserEventMusicRepository userEventMusicRepository) {
        this.mapper = mapper;
        this.userEventMusicRepository = userEventMusicRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        List<UserEventMusic> eventMusicList = userEventMusicRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", eventMusicList.size());
        resultMap.put("userEventMusicList", eventMusicList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
