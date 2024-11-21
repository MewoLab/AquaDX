package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.response.GetGameSettingResp;
import icu.samnyan.aqua.sega.chusan.model.response.data.GameSetting;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanGetGameSettingHandler")
public class GetGameSettingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameSettingHandler.class);

    private final StringMapper mapper;

    private final String ALLNET_HOST;
    private final String ALLNET_PORT;
    private final String SERVER_PORT;
    private final String ROM_VERSION;
    private final String GAME_VERSION;

    @Autowired
    public GetGameSettingHandler(StringMapper mapper, @Value("${allnet.server.host:}") String ALLNET_HOST, @Value("${allnet.server.port:}") String ALLNET_PORT, @Value("${server.port:}") String SERVER_PORT, @Value("${game.chusan.version:2.00.00}") String GAME_VERSION, @Value("${game.chusan.rom-version:2.00.01}") String ROM_VERSION) {
        this.mapper = mapper;
        this.ALLNET_HOST = ALLNET_HOST;
        this.ALLNET_PORT = ALLNET_PORT;
        this.SERVER_PORT = SERVER_PORT;
        this.GAME_VERSION = GAME_VERSION;
        this.ROM_VERSION = ROM_VERSION;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        // Fixed reboot time triggers chusan maintenance lockout, so let's try minime method which sets it dynamically
        // Special thanks to skogaby

        // Hardcode so that the reboot time always started 3 hours ago and ended 2 hours ago
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime rebootStartTime = LocalDateTime.now().minusHours(3);
        LocalDateTime rebootEndTime = LocalDateTime.now().minusHours(2);

        LocalDateTime matchingStartTime = LocalDateTime.now().minusHours(1);
        LocalDateTime matchingEndTime = LocalDateTime.now().plusHours(1);

        // Unless ip and port is explicitly overridden, use the guessed ip and port as same as AllNet Controller does.
        String localAddr;
        try {
            localAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // If above didn't work then how did this run? I really don't know.
            localAddr = "localhost";
        }

        String addr = ALLNET_HOST.isEmpty() ? localAddr : ALLNET_HOST;
        String port = ALLNET_PORT.isEmpty() ? SERVER_PORT : ALLNET_PORT;

        GameSetting gameSetting = new GameSetting(
                ROM_VERSION, // Chusan checks these two versions to determine if it can enable game modes
                GAME_VERSION, 
                false,
                0,
                rebootStartTime.format(formatter),
                rebootEndTime.format(formatter),
                false,
                300,
                300,
                300,
                matchingStartTime.format(formatter),
                matchingEndTime.format(formatter),
                10,
                10,
                "http://" + addr + ":" + port + "/ChusanServlet/",
                "http://" + addr + ":" + port + "/ChusanServlet/",
                "http://" + addr + ":" + port + "/ChusanServlet/",
                "http://" + addr + ":" + port + "/ChusanServlet/"
                );

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
