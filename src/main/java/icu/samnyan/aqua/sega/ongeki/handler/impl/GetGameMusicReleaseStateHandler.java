package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserKopRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetGameMusicReleaseStateHandler")
public class GetGameMusicReleaseStateHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameMusicReleaseStateHandler.class);

    private final BasicMapper mapper;

    @Autowired
    public GetGameMusicReleaseStateHandler(BasicMapper mapper, UserKopRepository userKopRepository) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        // TODO: Find out what this does

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("techScore", 0);
        resultMap.put("cardNum", 0);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
