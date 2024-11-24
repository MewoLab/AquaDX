package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserDataRepository;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserOptionRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.GetUserPreviewResp;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserData;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * The handler for loading basic profile information.
 * <p>
 * return null if no profile exist
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserPreviewHandler")
public class GetUserPreviewHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserPreviewHandler.class);

    private final BasicMapper mapper;

    private final UserDataRepository userDataRepository;

    private final UserOptionRepository userOptionRepository;

    @Autowired
    public GetUserPreviewHandler(BasicMapper mapper,
                                 UserDataRepository userDataRepository, UserOptionRepository userOptionRepository) {
        this.mapper = mapper;
        this.userDataRepository = userDataRepository;
        this.userOptionRepository = userOptionRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Optional<UserData> userData = userDataRepository.findByCard_ExtId(userId);

        GetUserPreviewResp resp = new GetUserPreviewResp();
        resp.setUserId(userId);
        if (userData.isEmpty()) {
             // From BaseDialogController.cs
             // <code>if (string.IsNullOrEmpty(instance.userPreview.lastPlayDate))</code>
             // so send a null value will trigger new user register
            resp.setLastPlayDate(null);

        } else {
            UserData user = userData.get();

            resp.setLogin(false);

            resp.setLastLoginDate(user.getLastPlayDate());
            resp.setUserName(user.getUserName());
            resp.setReincarnationNum(user.getReincarnationNum());
            resp.setLevel(user.getLevel());
            resp.setExp(user.getExp());
            resp.setPlayerRating(user.getPlayerRating());

            resp.setLastGameId(user.getLastGameId());

            resp.setLastRomVersion(user.getLastRomVersion());
            resp.setLastDataVersion(user.getLastDataVersion());

            resp.setLastPlayDate(user.getLastPlayDate());
            resp.setNameplateId(user.getNameplateId());
            resp.setTrophyId(user.getTrophyId());
            resp.setCardId(user.getCardId());

            resp.setDispPlayerLv(1);
            resp.setDispRating(1);
            resp.setDispBP(1);
            resp.setHeadphone(0);
            userOptionRepository.findByUser(user).ifPresent(x -> {
                resp.setDispPlayerLv(x.getDispPlayerLv());
                resp.setDispRating(x.getDispRating());
                resp.setDispBP(x.getDispBP());
                resp.setHeadphone(x.getHeadphone());
            });

            // test for now
            resp.setLastEmoneyBrand(4);
            resp.setLastEmoneyCredit(10000);
        }

        String json = mapper.write(resp);
        logger.info("Response: " + json);
        return json;

    }
}
