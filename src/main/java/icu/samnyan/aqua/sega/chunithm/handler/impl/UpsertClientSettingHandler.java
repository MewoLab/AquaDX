package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.requet.ClientSettingRequest;
import icu.samnyan.aqua.sega.general.model.GameVersion;
import icu.samnyan.aqua.sega.general.service.ClientSettingService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static icu.samnyan.aqua.sega.util.AquaConst.DEFAULT_KEYCHIP_ID;
import static icu.samnyan.aqua.sega.util.AquaConst.SERIAL_KEY;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class UpsertClientSettingHandler implements BaseHandler {

    private final StringMapper mapper;

    private final ClientSettingService clientSettingService;

    public UpsertClientSettingHandler(StringMapper mapper, ClientSettingService clientSettingService) {
        this.mapper = mapper;
        this.clientSettingService = clientSettingService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        var req = mapper.convert(request, ClientSettingRequest.class);
        var set = req.getClientSetting();
        var serial = (String) request.get(SERIAL_KEY);
        if (!serial.equals(DEFAULT_KEYCHIP_ID)) {
            clientSettingService.writeSetting(new GameVersion(serial, set.getRomVersion(), set.getDataVersion(), LocalDateTime.now()));
        }
        return "{\"returnCode\":\"1\"}";
    }
}
