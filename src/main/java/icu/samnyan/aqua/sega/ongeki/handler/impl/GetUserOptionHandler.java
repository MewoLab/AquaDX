package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserOptionRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserOption;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserOptionHandler")
public class GetUserOptionHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserOptionHandler.class);

    private final BasicMapper mapper;

    private final UserOptionRepository userOptionRepository;

    @Autowired
    public GetUserOptionHandler(BasicMapper mapper, UserOptionRepository userOptionRepository) {
        this.mapper = mapper;
        this.userOptionRepository = userOptionRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Optional<UserOption> userOptionOptional = userOptionRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        if(userOptionOptional.isPresent()) {
            resultMap.put("userOption", userOptionOptional.get());
        } else {
            resultMap.put("userOption", null);
        }


        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
