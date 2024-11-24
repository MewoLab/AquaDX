package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserData;
import icu.samnyan.aqua.sega.chunithm.service.UserDataService;
import icu.samnyan.aqua.sega.general.service.ClientSettingService;
import icu.samnyan.aqua.sega.util.VersionUtil;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static icu.samnyan.aqua.sega.util.AquaConst.SERIAL_KEY;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserDataHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserDataHandler.class);

    private final StringMapper mapper;

    private final ClientSettingService clientSettingService;

    private final UserDataService userDataService;

    @Autowired
    public GetUserDataHandler(StringMapper mapper,
                              ClientSettingService clientSettingService, UserDataService userDataService
    ) {
        this.mapper = mapper;
        this.clientSettingService = clientSettingService;
        this.userDataService = userDataService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        Optional<UserData> userDataOptional = userDataService.getUserByExtId(userId);

        if (userDataOptional.isPresent()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("userId", userId);
            UserData user = userDataOptional.get();

            var vo = clientSettingService.getSetting((String) request.get(SERIAL_KEY));
            if (vo.isPresent()) {
                var version = vo.get();
                user.setLastRomVersion(VersionUtil.getTargetVersion(user.getLastRomVersion(), version.getRomVersion()));
                user.setLastDataVersion(VersionUtil.getTargetVersion(user.getLastDataVersion(), version.getDataVersion()));
            }

            resultMap.put("userData", user);
            String json = mapper.write(resultMap);
            logger.info("Response: " + json);
            return json;
        }

        return null;
    }
}
