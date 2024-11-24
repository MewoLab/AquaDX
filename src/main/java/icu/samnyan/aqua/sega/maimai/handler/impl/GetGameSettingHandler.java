package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository;
import icu.samnyan.aqua.sega.general.model.PropertyEntry;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.GetGameSettingResp;
import icu.samnyan.aqua.sega.maimai.model.response.data.GameSetting;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetGameSettingHandler")
public class GetGameSettingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameSettingHandler.class);

    private final BasicMapper mapper;

    private final PropertyEntryRepository propertyEntryRepository;

    private final String HOST_OVERRIDE;
    private final String PORT_OVERRIDE;

    @Autowired
    public GetGameSettingHandler(BasicMapper mapper, PropertyEntryRepository propertyEntryRepository,
                                 @Value("${allnet.server.host:}") String HOST,
                                 @Value("${allnet.server.port:}") String PORT) {
        this.mapper = mapper;
        this.propertyEntryRepository = propertyEntryRepository;
        this.HOST_OVERRIDE = HOST;
        this.PORT_OVERRIDE = PORT;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        PropertyEntry start = propertyEntryRepository.findByPropertyKey("reboot_start_time")
                .orElseGet(() -> new PropertyEntry("reboot_start_time", "2020-01-01 07:00:00.0"));
        PropertyEntry end = propertyEntryRepository.findByPropertyKey("reboot_end_time")
                .orElseGet(() -> new PropertyEntry("reboot_end_time", "2020-01-01 07:59:59.0"));

        String addr = HOST_OVERRIDE.equals("") ? (String) request.get("localAddr") : HOST_OVERRIDE;
        String port = PORT_OVERRIDE.equals("") ? (String) request.get("localPort") : PORT_OVERRIDE;

        GameSetting gameSetting = new GameSetting(
                false,
                1800,
                start.getPropertyValue(),
                end.getPropertyValue(),
                0,
                0,
                "",
                "",
                "http://" + addr + ":" + port + "/",
                "");

        GetGameSettingResp resp = new GetGameSettingResp(
                false,
                gameSetting
        );

        String json = mapper.write(resp);

        logger.info("Response: " + json);
        return json;
    }
}
