package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.dao.userdata.UserCMissionProgressRepository;
import icu.samnyan.aqua.sega.chusan.dao.userdata.UserCMissionRepository;
import icu.samnyan.aqua.sega.chusan.model.request.UpsertUserAll;
import icu.samnyan.aqua.sega.chusan.model.response.CodeResp;
import icu.samnyan.aqua.sega.chusan.model.userdata.*;
import icu.samnyan.aqua.sega.chusan.service.*;
import icu.samnyan.aqua.sega.general.model.Card;
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating;
import icu.samnyan.aqua.sega.general.service.CardService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The handler for save user data. Only send in the end of the session.
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@AllArgsConstructor
@Component("ChusanUpsertUserAllHandler")
public class UpsertUserAllHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpsertUserAllHandler.class);

    private final StringMapper mapper;

    private final CardService cardService;

    private final UserDataService userDataService;
    private final UserCharacterService userCharacterService;
    private final UserGameOptionService userGameOptionService;
    private final UserMapAreaService userMapService;
    private final UserItemService userItemService;
    private final UserMusicDetailService userMusicDetailService;
    private final UserActivityService userActivityService;
    private final UserPlaylogService userPlaylogService;
    private final UserChargeService userChargeService;
    private final UserCourseService userCourseService;
    private final UserDuelService userDuelService;
    private final UserGeneralDataService userGeneralDataService;
    private final UserLoginBonusService userLoginBonusService;
    private final UserCMissionRepository userCMissionRepository;
    private final UserCMissionProgressRepository userCMissionProgressRepository;


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        UpsertUserAll upsertUserAll = mapper.convert(request.get("upsertUserAll"), UpsertUserAll.class);

        // Not all field will be sent. Check if they are exist first.

        Chu3UserData newUserData;
        // UserData
        if (upsertUserAll.getUserData() == null) {
            return null;
        } else {
            newUserData = upsertUserAll.getUserData().get(0);

            Chu3UserData userData = userDataService.getUserByExtId(userId).orElseGet(() -> {
                var data = new Chu3UserData();
                Card card = cardService.getCardByExtId(userId).orElseThrow();
                data.setCard(card);
                return data;
            });

            newUserData.setId(userData.getId());
            newUserData.setCard(userData.getCard());

            // Decode Username
            String userName = new String(newUserData.getUserName()
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            newUserData.setUserName(userName);
            newUserData.setUserNameEx("");
            userDataService.saveAndFlushUserData(newUserData);
        }

        // userGameOption
        if (upsertUserAll.getUserGameOption() != null) {
            UserGameOption newUserGameOption = upsertUserAll.getUserGameOption().get(0);

            Optional<UserGameOption> userGameOptionOptional = userGameOptionService.getByUser(newUserData);

            UserGameOption userGameOption = userGameOptionOptional.orElseGet(() -> new UserGameOption(newUserData));

            newUserGameOption.setId(userGameOption.getId());
            newUserGameOption.setUser(userGameOption.getUser());

            userGameOptionService.save(newUserGameOption);
        }

        // userMapList
        if (upsertUserAll.getUserMapAreaList() != null) {
            List<UserMap> userMapList = upsertUserAll.getUserMapAreaList();
            Map<Integer, UserMap> newUserMapMap = new HashMap<>();

            userMapList.forEach(newUserMap -> {
                int mapId = newUserMap.getMapAreaId();
                UserMap userMap;
                Optional<UserMap> userMapOptional = userMapService.getByUserAndMapAreaId(newUserData, mapId);
                userMap = userMapOptional.orElseGet(() -> new UserMap(newUserData));

                newUserMap.setId(userMap.getId());
                newUserMap.setUser(userMap.getUser());

                newUserMapMap.put(mapId, newUserMap);
            });
            userMapService.saveAll(newUserMapMap.values());
        }

        // userCharacterList
        if (upsertUserAll.getUserCharacterList() != null) {
            List<UserCharacter> userCharacterList = upsertUserAll.getUserCharacterList();
            Map<Integer, UserCharacter> newCharacterMap = new HashMap<>();

            userCharacterList.forEach(newUserCharacter -> {
                int characterId = newUserCharacter.getCharacterId();

                Optional<UserCharacter> userCharacterOptional = userCharacterService.getByUserAndCharacterId(newUserData, characterId);
                UserCharacter userCharacter = userCharacterOptional.orElseGet(() -> new UserCharacter(newUserData));

                newUserCharacter.setId(userCharacter.getId());
                newUserCharacter.setUser(userCharacter.getUser());

                newCharacterMap.put(characterId, newUserCharacter);
            });
            userCharacterService.saveAll(newCharacterMap.values());
        }

        // userItemList
        if (upsertUserAll.getUserItemList() != null) {
            List<UserItem> userItemList = upsertUserAll.getUserItemList();
            Map<String, UserItem> newUserItemMap = new HashMap<>();

            userItemList.forEach(newUserItem -> {
                int itemId = newUserItem.getItemId();
                int itemKind = newUserItem.getItemKind();


                Optional<UserItem> userItemOptional = userItemService.getByUserAndItemIdAndKind(newUserData, itemId, itemKind);
                UserItem userItem = userItemOptional.orElseGet(() -> new UserItem(newUserData));

                newUserItem.setId(userItem.getId());
                newUserItem.setUser(userItem.getUser());

                newUserItemMap.put(itemId + "-" + itemKind, newUserItem);
            });
            userItemService.saveAll(newUserItemMap.values());
        }

        // userMusicDetailList
        if (upsertUserAll.getUserMusicDetailList() != null) {

            List<UserMusicDetail> userMusicDetailList = upsertUserAll.getUserMusicDetailList();
            Map<String, UserMusicDetail> newUserMusicDetailMap = new HashMap<>();

            userMusicDetailList.forEach(newUserMusicDetail -> {
                int musicId = newUserMusicDetail.getMusicId();
                int level = newUserMusicDetail.getLevel();

                Optional<UserMusicDetail> userMusicDetailOptional = userMusicDetailService.getByUserAndMusicIdAndLevel(newUserData, musicId, level);
                UserMusicDetail userMusicDetail = userMusicDetailOptional.orElseGet(() -> new UserMusicDetail(newUserData));

                newUserMusicDetail.setId(userMusicDetail.getId());
                newUserMusicDetail.setUser(userMusicDetail.getUser());

                newUserMusicDetailMap.put(musicId + "-" + level, newUserMusicDetail);
            });
            userMusicDetailService.saveAll(newUserMusicDetailMap.values());
        }

        // userActivityList
        if (upsertUserAll.getUserActivityList() != null) {
            List<UserActivity> userActivityList = upsertUserAll.getUserActivityList();
            List<UserActivity> newUserActivityList = new LinkedList<>();

            userActivityList.forEach(newUserActivity -> {
                // No need to rename to activityId. jackson auto handle that
                int activityId = newUserActivity.getActivityId();
                int kind = newUserActivity.getKind();

                Optional<UserActivity> userActivityOptional = userActivityService.getByUserAndActivityIdAndKind(newUserData, activityId, kind);
                UserActivity userActivity = userActivityOptional.orElseGet(() -> new UserActivity(newUserData));

                newUserActivity.setId(userActivity.getId());
                newUserActivity.setUser(userActivity.getUser());

                newUserActivityList.add(newUserActivity);
            });
            userActivityService.saveAll(newUserActivityList);
        }

        // userRecentRatingList
        if(upsertUserAll.getUserRecentRatingList() != null) {
            List<UserRecentRating> userRecentRatingList = upsertUserAll.getUserRecentRatingList();

            StringBuilder sb = new StringBuilder();
            userRecentRatingList.forEach(userRecentRating -> {
                sb.append(userRecentRating.getMusicId()).append(":");
                sb.append(userRecentRating.getDifficultId()).append(":");
                sb.append(userRecentRating.getScore()).append(",");
            });
            if(sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            UserGeneralData userGeneralData = userGeneralDataService.getByUserAndKey(newUserData, "recent_rating_list")
                    .orElseGet(() -> new UserGeneralData(newUserData, "recent_rating_list"));
            userGeneralData.setPropertyValue(sb.toString());
            userGeneralDataService.save(userGeneralData);
        }

        // userChargeList
        if (upsertUserAll.getUserChargeList() != null) {
            List<UserCharge> userChargeList = upsertUserAll.getUserChargeList();
            List<UserCharge> newUserChargeList = new ArrayList<>();

            userChargeList.forEach(newUserCharge -> {
                int chargeId = newUserCharge.getChargeId();

                Optional<UserCharge> userChargeOptional = userChargeService.getByUserAndChargeId(newUserData, chargeId);
                UserCharge userCharge = userChargeOptional.orElseGet(() -> new UserCharge(newUserData));

                newUserCharge.setId(userCharge.getId());
                newUserCharge.setUser(userCharge.getUser());

                newUserChargeList.add(newUserCharge);
            });
            userChargeService.saveAll(newUserChargeList);
        }

        // userPlaylogList
        if (upsertUserAll.getUserPlaylogList() != null) {
            List<UserPlaylog> userPlaylogList = upsertUserAll.getUserPlaylogList();
            List<UserPlaylog> newUserPlaylogList = new ArrayList<>();

            userPlaylogList.forEach(newUserPlaylog -> {
                newUserPlaylog.setUser(newUserData);

                newUserPlaylogList.add(newUserPlaylog);
            });
            if (newUserPlaylogList.size() > 0) userPlaylogService.saveAll(newUserPlaylogList);
        }


        // userCourseList
        if (upsertUserAll.getUserCourseList() != null) {
            List<UserCourse> userCourseList = upsertUserAll.getUserCourseList();

            userCourseList.forEach(newUserCourse -> {
                int courseId = newUserCourse.getCourseId();

                Optional<UserCourse> userCourseOptional = userCourseService.getByUserAndCourseId(newUserData, courseId);
                UserCourse userCourse = userCourseOptional.orElseGet(() -> new UserCourse(newUserData));

                newUserCourse.setId(userCourse.getId());
                newUserCourse.setUser(userCourse.getUser());

                userCourseService.save(newUserCourse);
            });
        }

        // userDuelList
        if (upsertUserAll.getUserDuelList() != null) {
            List<UserDuel> userDuelList = upsertUserAll.getUserDuelList();
            Map<Integer, UserDuel> newUserDuelMap = new HashMap<>();

            userDuelList.forEach(newUserDuel -> {
                int duelId = newUserDuel.getDuelId();

                Optional<UserDuel> userDuelOptional = userDuelService.getByUserAndDuelId(newUserData, duelId);
                UserDuel userDuel = userDuelOptional.orElseGet(() -> new UserDuel(newUserData));

                newUserDuel.setId(userDuel.getId());
                newUserDuel.setUser(userDuel.getUser());

                newUserDuelMap.put(duelId, newUserDuel);
            });
            userDuelService.saveAll(newUserDuelMap.values());
        }

        if (upsertUserAll.getUserLoginBonusList() != null){
            List<Map<String, Object>> userLoginBonusList = upsertUserAll.getUserLoginBonusList();
            Map<Integer, UserLoginBonus> newUserLoginBonusMap = new HashMap<>();

            userLoginBonusList.forEach(newUserLoginBonus -> {
                int loginBonusPresetId = Integer.parseInt((String) newUserLoginBonus.get("presetId"));
                Optional<UserLoginBonus> userLoginBonusOptional = userLoginBonusService.getUserLoginBonus(Integer.parseInt(userId), loginBonusPresetId);
                UserLoginBonus userLoginBonus = userLoginBonusOptional.orElseGet(UserLoginBonus::new);
                userLoginBonus.setLastUpdateDate(LocalDateTime.now());
                userLoginBonus.setWatched(true);
                newUserLoginBonusMap.put(loginBonusPresetId, userLoginBonus);
            });
            userLoginBonusService.saveAll(newUserLoginBonusMap.values());
        }

        // userCMissionList
        if (upsertUserAll.getUserCMissionList() != null){
            List<Map<String, Object>> userCMissionList = upsertUserAll.getUserCMissionList();
            userCMissionList.forEach(userCMission -> {
                int missionId = Integer.parseInt((String) userCMission.get("missionId"));
                int point = Integer.parseInt((String) userCMission.get("point"));
                List<Map<String, Object>> userCMissionProgressList = (List<Map<String, Object>>) userCMission.get("userCMissionProgressList");
                userCMissionRepository.findByUser_Card_ExtIdAndMissionId(Long.parseLong(userId), missionId).ifPresentOrElse(userCMission1 -> {
                    userCMission1.setPoint(point);
                    userCMissionRepository.save(userCMission1);
                }, () -> {
                    UserCMission userCMission1 = new UserCMission();
                    userCMission1.setMissionId(missionId);
                    userCMission1.setPoint(point);
                    userCMission1.setUser(newUserData);
                    userCMissionRepository.save(userCMission1);
                });

                userCMissionProgressList.forEach(userCMissionProgress -> {
                    int order = Integer.parseInt((String) userCMissionProgress.get("order"));
                    int progress = Integer.parseInt((String) userCMissionProgress.get("progress"));
                    int stage = Integer.parseInt((String) userCMissionProgress.get("stage"));
                    userCMissionProgressRepository.findByUser_Card_ExtIdAndMissionIdAndOrder(Long.parseLong(userId), missionId, order).ifPresentOrElse(userCMissionProgress1 -> {
                        userCMissionProgress1.setProgress(progress);
                        userCMissionProgress1.setStage(stage);
                        userCMissionProgressRepository.save(userCMissionProgress1);
                    }, () -> {
                        UserCMissionProgress userCMissionProgress1 = new UserCMissionProgress();
                        userCMissionProgress1.setMissionId(missionId);
                        userCMissionProgress1.setOrder(order);
                        userCMissionProgress1.setProgress(progress);
                        userCMissionProgress1.setStage(stage);
                        userCMissionProgress1.setUser(newUserData);
                        userCMissionProgressRepository.save(userCMissionProgress1);
                    });
                });
            });
        }

        String json = mapper.write(new CodeResp(1));
        logger.info("Response: " + json);
        return json;
    }
}
