package icu.samnyan.aqua.sega.cardmaker.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.cardmaker.model.response.data.GameConnect;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("CardMakerGetGameConnectHandler")
public class GetGameConnectHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameConnectHandler.class);

    private final BasicMapper mapper;
    private final String ALLNET_HOST;
    private final String ALLNET_PORT;
    private final String SERVER_PORT;

    @Autowired
    public GetGameConnectHandler(BasicMapper mapper, @Value("${allnet.server.host:}") String ALLNET_HOST,
    @Value("${allnet.server.port:}") String ALLNET_PORT, @Value("${server.port:}") String SERVER_PORT) {
        this.mapper = mapper;
        this.ALLNET_HOST = ALLNET_HOST;
        this.ALLNET_PORT = ALLNET_PORT;
        this.SERVER_PORT = SERVER_PORT;
    }

    @Override
    public String handle(Map<String, Object> request) throws JsonProcessingException {
        int type = ((Number) request.get("type")).intValue(); // Allnet enabled or not
        long version = ((Number) request.get("version")).longValue(); // Rom version
        
        // Unless ip and port is explicitly overridden, use the guessed ip and port as same as AllNet Controller does.
        String localAddr;
        try {
            localAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // If above didn't work then how did this run? I really don't know.
            localAddr = "localhost";
        }

        String addr = ALLNET_HOST.equals("") ? localAddr : ALLNET_HOST;
        String port = ALLNET_PORT.equals("") ? SERVER_PORT : ALLNET_PORT;

        List<GameConnect> gameConnectList = new ArrayList<>();
        GameConnect chuni = new GameConnect(0, 1, "http://" + addr + ":" + port + "/g/chu3/" + version + "/");
        GameConnect mai = new GameConnect(1, 1, "http://" + addr + ":" + port + "/g/mai2/");
        GameConnect ongeki = new GameConnect(2, 1, "http://" + addr + ":" + port + "/g/ongeki/");
        gameConnectList.add(chuni);
        gameConnectList.add(mai);
        gameConnectList.add(ongeki);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("length", gameConnectList.size());
        resultMap.put("gameConnectList", gameConnectList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
