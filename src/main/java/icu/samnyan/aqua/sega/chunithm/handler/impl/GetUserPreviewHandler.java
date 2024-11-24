package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.response.GetUserPreviewResp;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserCharacter;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserData;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserGameOption;
import icu.samnyan.aqua.sega.chunithm.service.UserCharacterService;
import icu.samnyan.aqua.sega.chunithm.service.UserDataService;
import icu.samnyan.aqua.sega.chunithm.service.UserGameOptionService;
import icu.samnyan.aqua.sega.general.service.ClientSettingService;
import icu.samnyan.aqua.sega.util.VersionUtil;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static icu.samnyan.aqua.sega.util.AquaConst.SERIAL_KEY;

/**
 * The handler for loading basic profile information.
 * <p>
 * return null if no profile exist
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserPreviewHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserPreviewHandler.class);

    private final StringMapper mapper;

    private final ClientSettingService clientSettingService;

    private final UserDataService userDataService;
    private final UserCharacterService userCharacterService;
    private final UserGameOptionService userGameOptionService;

    @Autowired
    public GetUserPreviewHandler(StringMapper mapper,
                                 ClientSettingService clientSettingService, UserDataService userDataService,
                                 UserCharacterService userCharacterService,
                                 UserGameOptionService userGameOptionService
    ) {
        this.mapper = mapper;
        this.clientSettingService = clientSettingService;
        this.userDataService = userDataService;
        this.userCharacterService = userCharacterService;
        this.userGameOptionService = userGameOptionService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");


        Optional<UserData> userData = userDataService.getUserByExtId(userId);

        if (userData.isEmpty()) {
            return null;
        }


        UserData user = userData.get();

        GetUserPreviewResp resp = new GetUserPreviewResp();
        resp.setUserId(userId);
        resp.setLogin(false);
        resp.setLastLoginDate(user.getLastLoginDate());
        resp.setUserName(user.getUserName());
        resp.setReincarnationNum(user.getReincarnationNum());
        resp.setLevel(user.getLevel());
        resp.setExp(user.getExp());
        resp.setPlayerRating(user.getPlayerRating());
        resp.setLastGameId(user.getLastGameId());

        var vo = clientSettingService.getSetting((String) request.get(SERIAL_KEY));
        if (vo.isPresent()) {
            var version = vo.get();
            resp.setLastRomVersion(VersionUtil.getTargetVersion(user.getLastRomVersion(), version.getRomVersion()));
            resp.setLastDataVersion(VersionUtil.getTargetVersion(user.getLastDataVersion(), version.getDataVersion()));
        } else {
            resp.setLastRomVersion(user.getLastRomVersion());
            resp.setLastDataVersion(user.getLastDataVersion());
        }

        resp.setLastPlayDate(user.getLastPlayDate());
        resp.setTrophyId(user.getTrophyId());

        Optional<UserCharacter> userCharacterOptional = userCharacterService.getByUserAndCharacterId(user, user.getCharacterId());
        userCharacterOptional.ifPresent(resp::setUserCharacter);

        Optional<UserGameOption> userGameOptionOptional = userGameOptionService.getByUser(user);
        userGameOptionOptional.ifPresent(userGameOption -> {
            resp.setPlayerLevel(userGameOption.getPlayerLevel());
            resp.setRating(userGameOption.getRating());
            resp.setHeadphone(userGameOption.getHeadphone());
        });

        resp.setChargeState(1);
        resp.setUserNameEx(user.getUserName());

        String json = mapper.write(resp);
        logger.info("Response: " + json);
        return json;

    }
}
