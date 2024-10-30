package icu.samnyan.aqua.sega.cardmaker.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.cardmaker.model.response.GetGameSettingResp;
import icu.samnyan.aqua.sega.cardmaker.model.response.data.GameSetting;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
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
@Component("CardMakerGetGameSettingHandler")
public class GetGameSettingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameSettingHandler.class);

    private final BasicMapper mapper;

    @Autowired
    public GetGameSettingHandler(BasicMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public String handle(Map<String, Object> request) throws JsonProcessingException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime rebootStartTime = LocalDateTime.now().minusHours(3);
        LocalDateTime rebootEndTime = LocalDateTime.now().minusHours(2);

        GameSetting gameSetting = new GameSetting(
                "1.35.0",
                "1.32.0",
                "1.30.0",
                "1.45.0",
                false,
                10,
                rebootStartTime.format(formatter),
                rebootEndTime.format(formatter),
                false,
                100,
                100,
                100,
                false);

        GetGameSettingResp resp = new GetGameSettingResp(
                gameSetting,
                false,
                false
        );

        String json = mapper.write(resp);

        logger.info("Response: " + json);
        return json;
    }
}
