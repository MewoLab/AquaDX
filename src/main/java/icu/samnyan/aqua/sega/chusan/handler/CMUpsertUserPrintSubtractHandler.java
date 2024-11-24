package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import icu.samnyan.aqua.sega.chusan.model.Chu3UserCardPrintStateRepo;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCardPrintState;
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserItem;
import icu.samnyan.aqua.sega.chusan.service.UserDataService;
import icu.samnyan.aqua.sega.chusan.service.UserItemService;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanCMUpsertUserPrintSubtractHandler")
public class CMUpsertUserPrintSubtractHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CMUpsertUserPrintSubtractHandler.class);
    private final Chu3UserCardPrintStateRepo userCardPrintStateRepository;
    private final UserItemService userItemService;
    private final UserDataService userDataService;
    private final BasicMapper mapper;

    @Autowired
    public CMUpsertUserPrintSubtractHandler(UserItemService userItemService, UserDataService userDataService, Chu3UserCardPrintStateRepo userCardPrintStateRepository, BasicMapper mapper) {
        this.userCardPrintStateRepository = userCardPrintStateRepository;
        this.userItemService = userItemService;
        this.userDataService = userDataService;
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = String.valueOf(request.get("userId"));
        UserCardPrintState userCardPrintState = mapper.convert(request.get("userCardPrintState"), UserCardPrintState.class);
        List<UserItem> userItemList = mapper.convert(request.get("userItemList"), new TypeReference<List<UserItem>>() {});

        Chu3UserData userData;

        Optional<Chu3UserData> userOptional = userDataService.getUserByExtId(userId);
        if (userOptional.isPresent()) {
            userData = userOptional.get();
        } else {
            logger.error("User not found. userId: {}", userId);
            return null;
        }

        List<UserItem> userItemListToSave = new ArrayList<>();

        userItemList.forEach(newUserItem -> {
            int itemId = newUserItem.getItemId();
            int itemKind = newUserItem.getItemKind();

            Optional<UserItem> userItemOptional = userItemService.getByUserAndItemIdAndKind(userData, itemId, itemKind);
            UserItem userItem = userItemOptional.orElseGet(() -> new UserItem(userData));

            newUserItem.setId(userItem.getId());
            newUserItem.setUser(userItem.getUser());

            userItemListToSave.add(newUserItem);
        });
        userItemService.saveAll(userItemListToSave);

        Optional<UserCardPrintState> userCardPrintStateOptional = userCardPrintStateRepository.findById(userCardPrintState.getId());
        if (userCardPrintStateOptional.isPresent()) {
            UserCardPrintState newUserCardPrintState = userCardPrintStateOptional.get();
            newUserCardPrintState.setHasCompleted(true);
            userCardPrintStateRepository.save(newUserCardPrintState);
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("returnCode", 1);
        resultMap.put("apiName", "CMUpsertUserPrintSubtractApi");

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
