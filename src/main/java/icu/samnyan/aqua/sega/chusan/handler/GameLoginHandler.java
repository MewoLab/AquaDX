package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.gamedata.GameLoginBonus;
import icu.samnyan.aqua.sega.chusan.model.gamedata.GameLoginBonusPreset;
import icu.samnyan.aqua.sega.chusan.model.response.CodeResp;
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserItem;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserLoginBonus;
import icu.samnyan.aqua.sega.chusan.service.*;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanGameLoginHandler")
public class GameLoginHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GameLoginHandler.class);

    private final StringMapper mapper;

    private boolean enableLoginBonus = false;

    private final UserDataService userDataService;

    private final UserItemService userItemService;

    private final GameLoginBonusPresetService gameLoginBonusPresetService;

    private final GameLoginBonusService gameLoginBonusService;

    private final UserLoginBonusService userLoginBonusService;

    public GameLoginHandler(StringMapper mapper,
                            @Value("${game.chusan.loginbonus-enable:}") boolean enableLoginBonus,
                            UserDataService userDataService,
                            UserItemService userItemService,
                            GameLoginBonusPresetService gameLoginBonusPresetService,
                            GameLoginBonusService gameLoginBonusService,
                            UserLoginBonusService userLoginBonusService
                            ) {
        this.mapper = mapper;
        this.enableLoginBonus = enableLoginBonus;
        this.userDataService = userDataService;
        this.userItemService = userItemService;
        this.gameLoginBonusPresetService = gameLoginBonusPresetService;
        this.gameLoginBonusService = gameLoginBonusService;
        this.userLoginBonusService = userLoginBonusService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        Optional<Chu3UserData> userDataOptional = userDataService.getUserByExtId(userId);
        boolean userPresent = userDataOptional.isPresent();
        if (userPresent){
            userDataService.updateLoginTime(userDataOptional.get());
            if(this.enableLoginBonus){
                List<GameLoginBonusPreset> gameLoginBonusList = this.gameLoginBonusPresetService.getGameLoginBonusPresets(1);

                for (GameLoginBonusPreset preset: gameLoginBonusList) {
                    // check if a user already has some progress and if not, add the login bonus entry
                    Optional<UserLoginBonus> userLoginBonus = userLoginBonusService.getUserLoginBonus(Integer.parseInt(userId), preset.getId());
                    if (userLoginBonus.isEmpty()){
                        UserLoginBonus u = new UserLoginBonus(1, Integer.parseInt(userId), preset.getId());
                        userLoginBonusService.saveUserLoginBonus(u);
                        userLoginBonus = userLoginBonusService.getUserLoginBonus(Integer.parseInt(userId), preset.getId());
                    }
                    UserLoginBonus userLoginBonusResult = userLoginBonus.get();
                    if (userLoginBonusResult.isFinished()){
                        continue;
                    }
                    // last login is 24 hours+ ago
                    if (userLoginBonusResult.getLastUpdateDate().toEpochSecond(ZoneOffset.ofHours(0)) < (LocalDateTime.now().minusHours(24).toEpochSecond(ZoneOffset.ofHours(0)))){
                        int bonusCount = userLoginBonusResult.getBonusCount() + 1;
                        LocalDateTime lastUpdateDate = LocalDateTime.now();
                        List<GameLoginBonus> allLoginBonus = gameLoginBonusService.getAllGameLoginBonus(preset.getId());
                        if (allLoginBonus.size() == 0){
                            logger.info("No bonus entry found for" + preset.getId());
                            continue;
                        }
                        int maxNeededDays = allLoginBonus.get(0).getNeedLoginDayCount();

                        // if all items are redeemed, then don't show the login bonuses.
                        boolean isFinished = false;
                        if (bonusCount > maxNeededDays){
                            if (preset.getId() < 3000){
                                bonusCount = 1;
                            }else{
                                isFinished = true;
                            }
                        }
                        Optional<GameLoginBonus> gameLoginBonus = this.gameLoginBonusService.getGameLoginBonusByDay(preset.getId(), bonusCount);
                        if(gameLoginBonus.isPresent()){
                            GameLoginBonus gameLoginBonusResult = gameLoginBonus.get();
                            Chu3UserData userData = this.userDataService.getUserByExtId(userId).orElseThrow();
                            UserItem userItem = new UserItem(userData);
                            userItem.setItemId(gameLoginBonusResult.getPresentId());
                            userItem.setItemKind(6);
                            userItem.setStock(gameLoginBonusResult.getItemNum());
                            userItem.setValid(true);
                            this.userItemService.save(userItem);
                        }
                        Optional<UserLoginBonus> userLoginBonusOptional = this.userLoginBonusService.getUserLoginBonus(Integer.parseInt(userId), preset.getId());
                        UserLoginBonus userLoginBonusSave;
                        if(userLoginBonusOptional.isPresent()){
                            userLoginBonusSave = userLoginBonusOptional.get();
                            userLoginBonusSave.setBonusCount(bonusCount);
                            userLoginBonusSave.setLastUpdateDate(lastUpdateDate);
                            userLoginBonusSave.setWatched(false);
                            userLoginBonusSave.setFinished(isFinished);
                        }else{
                            userLoginBonusSave = new UserLoginBonus();
                            userLoginBonusSave.setUser(Integer.parseInt(userId));
                            userLoginBonusSave.setPresetId(preset.getId());
                            userLoginBonusSave.setVersion(1);
                            userLoginBonusSave.setBonusCount(bonusCount);
                            userLoginBonusSave.setLastUpdateDate(lastUpdateDate);
                            userLoginBonusSave.setWatched(false);
                            userLoginBonusSave.setFinished(isFinished);
                        }

                        this.userLoginBonusService.saveUserLoginBonus(userLoginBonusSave);
                    }
                }
            }
        }

        String json = mapper.write(new CodeResp(1));
        logger.info("Response: " + json);
        return json;
    }
}
