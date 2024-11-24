package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.response.GetUserPreviewResp;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCharacter;
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserGameOption;
import icu.samnyan.aqua.sega.chusan.service.UserCharacterService;
import icu.samnyan.aqua.sega.chusan.service.UserDataService;
import icu.samnyan.aqua.sega.chusan.service.UserGameOptionService;
import icu.samnyan.aqua.sega.general.service.ClientSettingService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
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
@Component("ChusanGetUserPreviewHandler")
public class GetUserPreviewHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserPreviewHandler.class);

    private final StringMapper mapper;

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
        this.userDataService = userDataService;
        this.userCharacterService = userCharacterService;
        this.userGameOptionService = userGameOptionService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        Optional<Chu3UserData> userData = userDataService.getUserByExtId(userId);

        if (userData.isEmpty()) {
            return null;
        }

        Chu3UserData user = userData.get();

        GetUserPreviewResp resp = new GetUserPreviewResp();
        resp.setUserId(userId);
        resp.setLogin(false);
        resp.setLastLoginDate(user.getLastPlayDate()); // Chusan seems doesn't save LastLoginDate, so use LastPlayDate instead
        resp.setUserName(user.getUserName());
        resp.setReincarnationNum(user.getReincarnationNum());
        resp.setLevel(user.getLevel());
        resp.setExp(user.getExp());
        resp.setPlayerRating(user.getPlayerRating());
        resp.setLastGameId(user.getLastGameId());

        resp.setLastRomVersion(user.getLastRomVersion());
        resp.setLastDataVersion(user.getLastDataVersion());

        resp.setLastPlayDate(user.getLastPlayDate());
        resp.setEmoneyBrandId(0);
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
        resp.setUserNameEx("");
        resp.setBanState(0);
        resp.setClassEmblemMedal(user.getClassEmblemMedal());
        resp.setClassEmblemBase(user.getClassEmblemBase());
        resp.setBattleRankId(user.getBattleRankId());

        String json = mapper.write(resp);
        logger.info("Response: " + json);
        return json;

    }
}
