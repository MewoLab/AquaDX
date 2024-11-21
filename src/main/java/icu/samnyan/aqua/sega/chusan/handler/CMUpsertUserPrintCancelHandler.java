package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanCMUpsertUserPrintCancelHandler")
public class CMUpsertUserPrintCancelHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CMUpsertUserPrintCancelHandler.class);
    private final Chu3UserCardPrintStateRepo userCardPrintStateRepository;
    private final BasicMapper mapper;

    @Autowired
    public CMUpsertUserPrintCancelHandler(Chu3UserCardPrintStateRepo userCardPrintStateRepository, BasicMapper mapper) {
        this.userCardPrintStateRepository = userCardPrintStateRepository;
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = String.valueOf(request.get("userId"));
        List<Integer> orderIdList = mapper.convert(request.get("orderIdList"), new TypeReference<List<Integer>>() {});

        for (Integer orderId : orderIdList) {
            Optional<UserCardPrintState> userCardPrintStateOptional = userCardPrintStateRepository.findById(orderId.longValue());
            if (userCardPrintStateOptional.isPresent()) {
                UserCardPrintState newUserCardPrintState = userCardPrintStateOptional.get();
                newUserCardPrintState.setHasCompleted(true);
                userCardPrintStateRepository.save(newUserCardPrintState);
            }
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("returnCode", 1);
        resultMap.put("apiName", "CMUpsertUserPrintCancelApi");

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
