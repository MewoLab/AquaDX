package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserItemRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserItem;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserItemHandler")
public class GetUserItemHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserItemHandler.class);

    private final BasicMapper mapper;

    private final UserItemRepository userItemRepository;

    @Autowired
    public GetUserItemHandler(BasicMapper mapper, UserItemRepository userItemRepository) {
        this.mapper = mapper;
        this.userItemRepository = userItemRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        Long nextIndexVal = (Long) request.get("nextIndex");
        Integer maxCount = (Integer) request.get("maxCount");

        Long mul = 10000000000L;

        int kind = (int) (nextIndexVal / mul);
        int nextIndex = (int) (nextIndexVal % mul);
        int pageNum = nextIndex / maxCount;

        Page<UserItem> dbPage = userItemRepository.findByUser_Card_ExtIdAndItemKind(userId, kind, PageRequest.of(pageNum, maxCount));

        long currentIndex = kind * mul + maxCount * pageNum + dbPage.getNumberOfElements();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", dbPage.getNumberOfElements());
        resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : currentIndex);
        resultMap.put("itemKind", kind);
        resultMap.put("userItemList", dbPage.getContent());

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
