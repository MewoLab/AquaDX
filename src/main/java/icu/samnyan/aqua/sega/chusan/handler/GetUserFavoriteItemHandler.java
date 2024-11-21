package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.response.data.UserFavorite;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserGeneralData;
import icu.samnyan.aqua.sega.chusan.service.UserGeneralDataService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("ChusanGetUserFavoriteItemHandler")
public class GetUserFavoriteItemHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserFavoriteItemHandler.class);

    private final StringMapper mapper;

    private final UserGeneralDataService userGeneralDataService;

    @Autowired
    public GetUserFavoriteItemHandler(StringMapper mapper, UserGeneralDataService userGeneralDataService) {
        this.mapper = mapper;
        this.userGeneralDataService = userGeneralDataService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        int kind = Integer.parseInt((String) request.get("kind"));
        Long nextIndexVal = Long.parseLong((String) request.get("nextIndex"));
        int maxCount = Integer.parseInt((String) request.get("maxCount")); // Fixed to 50?

        List<UserFavorite> userFavorites = new LinkedList<>();
        Optional<UserGeneralData> favOptional;

        switch (kind) {
            case 1: // Music
            favOptional = userGeneralDataService.getByUserIdAndKey(userId, "favorite_music");
            break;

            case 3: // Chara
            favOptional = userGeneralDataService.getByUserIdAndKey(userId, "favorite_chara");
            break;

            default:
            favOptional = Optional.empty();
            break;
        }

        // Let's assume data is in following format:
        // 1111,2222,3333,4444 ...
        if(favOptional.isPresent()) {
            String val = favOptional.get().getPropertyValue();
            if(StringUtils.isNotBlank(val) && val.contains(",")) {
                String[] records = val.split(",");
                for (String record : records) {
                    userFavorites.add(new UserFavorite(Integer.parseInt(record)));
                }
            }
        }

        // While client seems capable to handle more than 50 items, let's limit it to 50
        // Reasons: 1. VARCHAR is limited to 255 chars (theoretically <= 51 entries), 2. Multiple pagination is troublesome
        if (userFavorites.size() > 50) {
            userFavorites = userFavorites.subList(0, 50);
        }
        
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("kind", kind);
        resultMap.put("length", userFavorites.size());
        resultMap.put("nextIndex", -1);
        resultMap.put("userFavoriteItemList", userFavorites);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
