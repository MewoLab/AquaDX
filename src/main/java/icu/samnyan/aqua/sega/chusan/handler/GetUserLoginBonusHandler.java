package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserLoginBonus;
import icu.samnyan.aqua.sega.chusan.service.UserDataService;
import icu.samnyan.aqua.sega.chusan.service.UserLoginBonusService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("ChusanGetUserLoginBonusHandler")
public class GetUserLoginBonusHandler  implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserLoginBonusHandler.class);

    private final StringMapper mapper;

    private boolean enableLoginBonus = false;

    private final UserDataService userDataService;

    private final UserLoginBonusService userLoginBonusService;

    @Autowired
    public GetUserLoginBonusHandler(StringMapper mapper,
                                    @Value("${game.chusan.loginbonus-enable:}") boolean enableLoginBonus,
                                    UserDataService userDataService,
                                    UserLoginBonusService userLoginBonusService) {
        this.mapper = mapper;
        this.enableLoginBonus = enableLoginBonus;
        this.userLoginBonusService = userLoginBonusService;
        this.userDataService = userDataService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        if(!this.enableLoginBonus){
            resultMap.put("length", 0);
            resultMap.put("userLoginBonusList", List.of());
        }else{
            List<UserLoginBonus> userLoginBonusList = userLoginBonusService.getAllUserLoginBonus(Integer.parseInt(userId));
            List<Map<String, String>> retList = new ArrayList<>();
            resultMap.put("length", userLoginBonusList.size());
            for (UserLoginBonus u: userLoginBonusList) {
                Map<String, String> appendInfo = new HashMap<>();
                appendInfo.put("presetId", String.valueOf(u.getPresetId()));
                appendInfo.put("bonusCount", String.valueOf(u.getBonusCount()));
                appendInfo.put("lastUpdateDate", u.getLastUpdateDate().toString());
                appendInfo.put("isWatched", String.valueOf(u.isWatched()));
                retList.add(appendInfo);
            }
            resultMap.put("userLoginBonusList", retList);
        }

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}