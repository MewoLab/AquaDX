package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.chusan.model.Chu3UserCardPrintStateRepo;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCardPrintState;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanGetUserCardPrintErrorHandler")
public class GetUserCardPrintErrorHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserCardPrintErrorHandler.class);
    private final Chu3UserCardPrintStateRepo userCardPrintStateRepository;
    private final BasicMapper mapper;

    @Autowired
    public GetUserCardPrintErrorHandler(Chu3UserCardPrintStateRepo userCardPrintStateRepository, BasicMapper mapper) {
        this.mapper = mapper;
        this.userCardPrintStateRepository = userCardPrintStateRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        Long userId = ((Number) request.get("userId")).longValue();

        List<UserCardPrintState> userCardPrintStateList = userCardPrintStateRepository.findByUser_Card_ExtIdAndHasCompleted(userId, false);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userCardPrintStateList.size());
        resultMap.put("userCardPrintStateList", userCardPrintStateList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
