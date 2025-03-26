package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserDeckRepository;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserDeck;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserSkinHandler")
public class GetUserSkinHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserSkinHandler.class);

    private final BasicMapper mapper;

    private final UserDeckRepository userDeckRepository;

    @Autowired
    public GetUserSkinHandler(BasicMapper mapper, UserDeckRepository userDeckRepository) {
        this.mapper = mapper;
        this.userDeckRepository = userDeckRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        // Get the list of user decks
        List<UserDeck> deckList = userDeckRepository.findByUser_Card_ExtId(userId);

        // Convert each UserDeck to UserSkin
        List<Map<String, Object>> userSkinList = deckList.stream().map(deck -> {
            Map<String, Object> skinMap = new LinkedHashMap<>();
            skinMap.put("deckId", deck.getDeckId());
            skinMap.put("isValid", false);
            skinMap.put("cardId1", deck.getCardId1());
            skinMap.put("cardId2", deck.getCardId2());
            skinMap.put("cardId3", deck.getCardId3());
            return skinMap;
        }).collect(Collectors.toList());

        resultMap.put("length", userSkinList.size());
        resultMap.put("userSkinList", userSkinList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
