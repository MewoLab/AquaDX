package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.UserLoginResp;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiUserLoginHandler")
public class UserLoginHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginHandler.class);

    private final BasicMapper mapper;

    public UserLoginHandler(BasicMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        UserLoginResp resp = new UserLoginResp();

        String json = mapper.write(resp);
        logger.info("Response: " + json);
        return json;
    }
}
