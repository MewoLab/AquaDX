package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserMemoryChapterRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserMemoryChapter;
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
@Component("OngekiGetUserMemoryChapterHandler")
public class GetUserMemoryChapterHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMemoryChapterHandler.class);

    private final BasicMapper mapper;

    private final UserMemoryChapterRepository userMemoryChapterRepository;

    @Autowired
    public GetUserMemoryChapterHandler(BasicMapper mapper, UserMemoryChapterRepository userMemoryChapterRepository) {
        this.mapper = mapper;
        this.userMemoryChapterRepository = userMemoryChapterRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserMemoryChapter> MemoryChapterList = userMemoryChapterRepository.findByUser_Card_ExtId(userId);
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", MemoryChapterList.size());
        resultMap.put("userMemoryChapterList", MemoryChapterList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
