package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetTransferFriendHandler")
public class GetTransferFriendHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetTransferFriendHandler.class);

    private final BasicMapper mapper;

    public GetTransferFriendHandler(BasicMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Response:
     * transferFriendList
     * playUserId
     * playUserName
     * playDate
     * friendPoint
     * isFavorite
     *
     */
    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        return "{}";
    }
}
