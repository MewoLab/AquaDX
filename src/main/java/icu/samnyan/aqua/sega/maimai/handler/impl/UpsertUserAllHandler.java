package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.model.Card;
import icu.samnyan.aqua.sega.general.service.CardService;
import icu.samnyan.aqua.sega.maimai.dao.userdata.*;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.request.UpsertUserAll;
import icu.samnyan.aqua.sega.maimai.model.request.data.UserAll;
import icu.samnyan.aqua.sega.maimai.model.response.data.UserGradeStatus;
import icu.samnyan.aqua.sega.maimai.model.response.data.UserRecentRating;
import icu.samnyan.aqua.sega.maimai.model.userdata.*;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiUpsertUserAllHandler")
public class UpsertUserAllHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpsertUserAllHandler.class);

    private final BasicMapper mapper;

    private final CardService cardService;

    private final UserDataRepository userDataRepository;
    private final UserOptionRepository userOptionRepository;
    private final UserWebOptionRepository userWebOptionRepository;
    private final UserMusicDetailRepository userMusicDetailRepository;
    private final UserGeneralDataRepository userGeneralDataRepository;
    private final UserActivityRepository userActivityRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final UserPlaylogRepository userPlaylogRepository;

    public UpsertUserAllHandler(BasicMapper mapper, CardService cardService, UserDataRepository userDataRepository, UserOptionRepository userOptionRepository, UserWebOptionRepository userWebOptionRepository, UserMusicDetailRepository userMusicDetailRepository, UserGeneralDataRepository userGeneralDataRepository, UserActivityRepository userActivityRepository, UserCharacterRepository userCharacterRepository, UserPlaylogRepository userPlaylogRepository) {
        this.mapper = mapper;
        this.cardService = cardService;
        this.userDataRepository = userDataRepository;
        this.userOptionRepository = userOptionRepository;
        this.userWebOptionRepository = userWebOptionRepository;
        this.userMusicDetailRepository = userMusicDetailRepository;
        this.userGeneralDataRepository = userGeneralDataRepository;
        this.userActivityRepository = userActivityRepository;
        this.userCharacterRepository = userCharacterRepository;
        this.userPlaylogRepository = userPlaylogRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        UpsertUserAll upsertUserAll = mapper.convert(request, UpsertUserAll.class);
        long userId = upsertUserAll.getUserId();
        UserAll userAll = upsertUserAll.getUpsertUserAll();

        // UserData
        UserData userData;
        UserData newUserData;
        if (userAll.getUserData() == null) {
            return null;
        } else {
            newUserData = userAll.getUserData().get(0);
            Optional<UserData> userOptional = userDataRepository.findByCard_ExtId(userId);

            if (userOptional.isPresent()) {
                userData = userOptional.get();
            } else {
                userData = new UserData();
                Card card = cardService.getCardByExtId(userId).orElseThrow();
                userData.setCard(card);
            }

            newUserData.setId(userData.getId());
            newUserData.setCard(userData.getCard());
            // Decode Username
            String userName = new String(newUserData.getUserName()
                    .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);


            newUserData.setUserName(userName);
            userDataRepository.saveAndFlush(newUserData);
        }

        // UserOption
        if (userAll.getUserOption() != null) {
            UserOption newUserOption = userAll.getUserOption().get(0);

            Optional<UserOption> userOptionOptional = userOptionRepository.findByUser(newUserData);
            UserOption userOption = userOptionOptional.orElseGet(() -> new UserOption(newUserData));

            newUserOption.setId(userOption.getId());
            newUserOption.setUser(userOption.getUser());

            userOptionRepository.save(newUserOption);
        }

        // UserWebOption
        if (userAll.getUserWebOption() != null) {
            UserWebOption newUserWebOption = userAll.getUserWebOption().get(0);

            Optional<UserWebOption> userWebOptionOptional = userWebOptionRepository.findByUser(newUserData);
            UserWebOption userWebOption = userWebOptionOptional.orElseGet(() -> new UserWebOption(newUserData));

            newUserWebOption.setId(userWebOption.getId());
            newUserWebOption.setUser(userWebOption.getUser());

            userWebOptionRepository.save(newUserWebOption);
        }

        // UserMusicDetailList
        if (userAll.getUserMusicDetailList() != null) {
            List<UserMusicDetail> userMusicDetailList = userAll.getUserMusicDetailList();
            List<UserMusicDetail> newUserMusicDetailList = new ArrayList<>();

            for (UserMusicDetail newUserMusicDetail : userMusicDetailList) {
                int musicId = newUserMusicDetail.getMusicId();
                int level = newUserMusicDetail.getLevel();

                Optional<UserMusicDetail> musicDetailOptional = userMusicDetailRepository.findByUserAndMusicIdAndLevel(newUserData, musicId, level);
                UserMusicDetail userMusicDetail = musicDetailOptional.orElseGet(() -> new UserMusicDetail(newUserData));

                newUserMusicDetail.setId(userMusicDetail.getId());
                newUserMusicDetail.setUser(newUserData);
                newUserMusicDetailList.add(newUserMusicDetail);
            }
            userMusicDetailRepository.saveAll(newUserMusicDetailList);
        }

        // UserRecentRatingList
        if (userAll.getUserRecentRatingList() != null) {
            List<UserRecentRating> recentRatingList = userAll.getUserRecentRatingList();
            StringBuilder userRecentRating = new StringBuilder();
            recentRatingList.forEach(rating -> {
                userRecentRating.append(rating.getMusicId()).append(":").append(rating.getLevel()).append(":").append(rating.getAchieve()).append(":");
                userRecentRating.append(",");
            });
            if (userRecentRating.length() > 0) {
                userRecentRating.deleteCharAt(userRecentRating.length() - 1);
            }
            UserGeneralData ratingData = userGeneralDataRepository.findByUserAndPropertyKey(newUserData, "user_recent_rating")
                    .orElseGet(() -> new UserGeneralData(newUserData, "user_recent_rating"));
            ratingData.setPropertyValue(userRecentRating.toString());
            userGeneralDataRepository.save(ratingData);
        }

        // UserActivityList
        if (userAll.getUserActivityList() != null) {
            List<UserActivity> userActivityList = userAll.getUserActivityList();
            List<UserActivity> newUserActivityList = new ArrayList<>();

            for (UserActivity newUserActivity : userActivityList) {
                int kind = newUserActivity.getKind();
                int id = newUserActivity.getActivityId();

                if (kind != 0 && id != 0) {
                    Optional<UserActivity> activityOptional = userActivityRepository.findByUserAndKindAndActivityId(newUserData, kind, id);
                    UserActivity userActivity = activityOptional.orElseGet(() -> new UserActivity(newUserData));

                    newUserActivity.setId(userActivity.getId());
                    newUserActivity.setUser(newUserData);
                    newUserActivityList.add(newUserActivity);
                }
            }
            newUserActivityList.sort((a, b) -> Long.compare(b.getSortNumber(), a.getSortNumber()));
            userActivityRepository.saveAll(newUserActivityList);
        }

        // UserGradeStatusList
        if (userAll.getUserGradeStatusList() != null) {
            UserGradeStatus userGradeStatus = userAll.getUserGradeStatusList().get(0);
            UserGeneralData gradeData = userGeneralDataRepository.findByUserAndPropertyKey(newUserData, "user_grade_status")
                    .orElseGet(() -> new UserGeneralData(newUserData, "user_grade_status"));
            gradeData.setPropertyValue(userGradeStatus.getGradeVersion() + "," + userGradeStatus.getGradeLevel() + "," + userGradeStatus.getGradeSubLevel() + "," + userGradeStatus.getGradeMaxId());
            userGeneralDataRepository.save(gradeData);
        }

        // UserCharacterList
        if (userAll.getUserCharacterList() != null) {
            List<UserCharacter> userCharacterList = userAll.getUserCharacterList();
            List<UserCharacter> newUserCharacterList = new ArrayList<>();
            for (UserCharacter newUserCharacter : userCharacterList) {
                int id = newUserCharacter.getCharacterId();

                Optional<UserCharacter> characterOptional = userCharacterRepository.findByUserAndCharacterId(newUserData, id);
                UserCharacter userCharacter = characterOptional.orElseGet(() -> new UserCharacter(newUserData));

                newUserCharacter.setId(userCharacter.getId());
                newUserCharacter.setUser(newUserData);
                newUserCharacterList.add(newUserCharacter);
            }
            userCharacterRepository.saveAll(newUserCharacterList);
        }

        // UserPlaylogList
        if (userAll.getUserPlaylogList() != null) {
            List<UserPlaylog> userPlaylogList = userAll.getUserPlaylogList();
            userPlaylogList.forEach(log -> log.setUser(newUserData));
            userPlaylogRepository.saveAll(userPlaylogList);
        }

        return "{\"returnCode\":1,\"apiName\":\"com.sega.maimaiservlet.api.UpsertUserAllApi\"}";
    }
}
