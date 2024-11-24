package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData;
import icu.samnyan.aqua.sega.chusan.service.UserDataService;
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
@Component("ChusanCMGetUserPreviewHandler")
public class CMGetUserPreviewHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CMGetUserPreviewHandler.class);

    private final BasicMapper mapper;

    private final UserDataService userDataService;

    @Autowired
    public CMGetUserPreviewHandler(BasicMapper mapper, UserDataService userDataService) {
        this.mapper = mapper;
        this.userDataService = userDataService;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = String.valueOf(request.get("userId"));
        String segaIdAuthKey = String.valueOf(request.get("segaIdAuthKey"));

        Optional<Chu3UserData> userDataOptional = userDataService.getUserByExtId(userId);

        if (userDataOptional.isPresent()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            Chu3UserData user = userDataOptional.get();

            resultMap.put("userName", user.getUserName());
            resultMap.put("level", user.getLevel());
            resultMap.put("medal", user.getMedal());
            resultMap.put("lastDataVersion", user.getLastDataVersion());
            resultMap.put("isLogin", false);

            String json = mapper.write(resultMap);
            logger.info("Response: " + json);
            return json;
        }

        return null;
    }
}
