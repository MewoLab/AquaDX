package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository;
import icu.samnyan.aqua.sega.general.model.PropertyEntry;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.GetGameSettingResp;
import icu.samnyan.aqua.sega.ongeki.model.response.data.GameSetting;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetGameSettingHandler")
public class GetGameSettingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameSettingHandler.class);

    private final BasicMapper mapper;

    private final PropertyEntryRepository propertyEntryRepository;

    private final String GAME_VERSION;

    @Autowired
    public GetGameSettingHandler(BasicMapper mapper, PropertyEntryRepository propertyEntryRepository, @Value("${game.ongeki.version:1.05.00}") String GAME_VERSION) {
        this.mapper = mapper;
        this.propertyEntryRepository = propertyEntryRepository;
        this.GAME_VERSION = GAME_VERSION;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        PropertyEntry start = propertyEntryRepository.findByPropertyKey("reboot_start_time")
                .orElseGet(() -> new PropertyEntry("reboot_start_time", "2020-01-01 23:59:00.0"));
        PropertyEntry end = propertyEntryRepository.findByPropertyKey("reboot_end_time")
                .orElseGet(() -> new PropertyEntry("reboot_end_time", "2020-01-01 23:59:00.0"));

        GameSetting gameSetting = new GameSetting(
                GAME_VERSION,
                GAME_VERSION,
                false,
                10,
                start.getPropertyValue(),
                end.getPropertyValue(),
                false,
                50,
                300,
                300,
                50,
                300,
                300);

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
