package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserGeneralDataRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.data.UserGradeStatus;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserGeneralData;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserGradeHandler")
public class GetUserGradeHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserGradeHandler.class);

    private final BasicMapper mapper;

    private final UserGeneralDataRepository userGeneralDataRepository;

    public GetUserGradeHandler(BasicMapper mapper, UserGeneralDataRepository userGeneralDataRepository) {
        this.mapper = mapper;
        this.userGeneralDataRepository = userGeneralDataRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        UserGeneralData userData = userGeneralDataRepository.findByUser_Card_ExtIdAndPropertyKey(userId, "user_grade_status")
                .orElseGet(() -> new UserGeneralData(-1, null, "user_grade_status", "1,1,1,1"));

        String[] val = userData.getPropertyValue().split(",");
        UserGradeStatus userGradeStatus = new UserGradeStatus(
                Integer.parseInt(val[0]),
                Integer.parseInt(val[1]),
                Integer.parseInt(val[2]),
                Integer.parseInt(val[3])
        );

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("userGradeStatus", userGradeStatus);
        resultMap.put("length", 0);
        resultMap.put("userGradeList", null);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
