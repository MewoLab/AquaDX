package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserRivalDataRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GEEKiDoS (geek_ds@foxmail.com)
 */
@Component("OngekiGetUserRivalHandler")
public class GetUserRivalHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserRivalHandler.class);

    private final BasicMapper mapper;
    private final UserRivalDataRepository userRivalDataRepository;

    @Autowired
    public GetUserRivalHandler(BasicMapper mapper, UserRivalDataRepository userRivalDataRepository) {
        this.mapper = mapper;
        this.userRivalDataRepository = userRivalDataRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        var userRivalId = ((Number) request.get("userId")).longValue();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userRivalId);

        var result = userRivalDataRepository.findByUser_Card_ExtId(userRivalId);//.stream().map(x->x.getUser().getCard().getExtId()).toArray();
        resultMap.put("length", result.size());
        resultMap.put("userRivalList", result);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
