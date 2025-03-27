package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserSkinRepository;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserSkin;
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
@Component("OngekiGetUserSkinHandler")
public class GetUserSkinHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserSkinHandler.class);

    private final BasicMapper mapper;

    private final UserSkinRepository userSkinRepository;

    @Autowired
    public GetUserSkinHandler(BasicMapper mapper, UserSkinRepository userSkinRepository) {
        this.mapper = mapper;
        this.userSkinRepository = userSkinRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        List<UserSkin> userSkinList = userSkinRepository.findByUser_Card_ExtId(userId);
        resultMap.put("length", userSkinList.size());
        resultMap.put("userSkinList", userSkinList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
