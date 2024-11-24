package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserDataRepository;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserRivalDataRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.UserRivalData;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author GEEKiDoS (geek_ds@foxmail.com)
 */
@Component("OngekiGetUserRivalDataHandler")
public class GetUserRivalDataHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserRivalDataHandler.class);

    private final BasicMapper mapper;
    private final UserRivalDataRepository userRivalDataRepository;
    private final UserDataRepository userDataRepository;

    @Autowired
    public GetUserRivalDataHandler(BasicMapper mapper, UserRivalDataRepository userRivalDataRepository, UserDataRepository userDataRepository) {
        this.mapper = mapper;
        this.userRivalDataRepository = userRivalDataRepository;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        var userRivalId = ((Number) request.get("userId")).longValue();
        var userRivalList = ((Collection<HashMap<String,Object>>) request.get("userRivalList"))
                .stream()
                .map(x->((Number)x.get("rivalUserId")).longValue())
                .collect(Collectors.toList());

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userRivalId);

        var userInfos = userDataRepository
                .findByCard_ExtIdIn(userRivalList)
                .stream()
                .map(x -> new UserRivalData(x.getCard().getExtId(), x.getUserName()))
                .toArray();

        resultMap.put("length", userInfos.length);
        resultMap.put("userRivalDataList", userInfos);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
