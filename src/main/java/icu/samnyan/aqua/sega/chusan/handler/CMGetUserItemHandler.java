package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserItem;
import icu.samnyan.aqua.sega.chusan.service.UserItemService;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for getting user item.
 * This get call before profile create.
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanCMGetUserItemHandler")
public class CMGetUserItemHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CMGetUserItemHandler.class);

    private final BasicMapper mapper;

    private final UserItemService userItemService;

    public CMGetUserItemHandler(BasicMapper mapper, UserItemService userItemService) {
        this.mapper = mapper;
        this.userItemService = userItemService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = String.valueOf(request.get("userId"));
        Long nextIndexVal = ((Number) request.get("nextIndex")).longValue();
        int maxCount = ((Number) request.get("maxCount")).intValue();

        Long mul = 10000000000L;

        int kind = (int) (nextIndexVal / mul);
        int nextIndex = (int) (nextIndexVal % mul);
        int pageNum = nextIndex / maxCount;

        Page<UserItem> userItemPage = userItemService.getByUserAndItemKind(userId, kind, pageNum, maxCount);

        List<UserItem> userItemList = userItemPage.getContent();

        long currentIndex = kind * mul + maxCount * pageNum + userItemPage.getNumberOfElements();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userItemPage.getNumberOfElements());
        resultMap.put("nextIndex", userItemPage.getNumberOfElements() < maxCount ? -1 : currentIndex);
        resultMap.put("itemKind", kind);
        resultMap.put("userItemList", userItemList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
