package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.response.GetGameSettingResp;
import icu.samnyan.aqua.sega.chunithm.model.response.data.GameSetting;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetGameSettingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameSettingHandler.class);

    private final StringMapper mapper;

    @Autowired
    public GetGameSettingHandler(StringMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        // Fixed reboot time triggers chunithm maintenance lockout, so let's try minime method which sets it dynamically
        // Special thanks to skogaby

        // Hardcode so that the reboot time always started 3 hours ago and ended 2 hours ago
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime rebootStartTime = LocalDateTime.now().minusHours(3);
        LocalDateTime rebootEndTime = LocalDateTime.now().minusHours(2);

        GameSetting gameSetting = new GameSetting(
                1,
                false,
                10,
                rebootStartTime.format(formatter),
                rebootEndTime.format(formatter),
                false,
                300,
                300,
                300);

        GetGameSettingResp resp = new GetGameSettingResp(
                gameSetting,
                false,
                true
        );

        String json = mapper.write(resp);

        logger.info("Response: " + json);
        return json;
    }
}
