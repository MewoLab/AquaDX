package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.model.Card;
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating;
import icu.samnyan.aqua.sega.general.service.CardService;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.*;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.request.UpsertUserAll;
import icu.samnyan.aqua.sega.ongeki.model.response.CodeResp;
import icu.samnyan.aqua.sega.ongeki.model.userdata.*;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The handler for saving all data of a ONGEKI profile
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiUserAllHandler")
public class UpsertUserAllHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpsertUserAllHandler.class);

    private final BasicMapper mapper;

    private final CardService cardService;

    private final UserDataRepository userDataRepository;
    private final UserOptionRepository userOptionRepository;
    private final UserPlaylogRepository userPlaylogRepository;
    private final UserActivityRepository userActivityRepository;
    private final UserMusicDetailRepository userMusicDetailRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final UserCardRepository userCardRepository;
    private final UserDeckRepository userDeckRepository;
    private final UserStoryRepository userStoryRepository;
    private final UserChapterRepository userChapterRepository;
    private final UserMemoryChapterRepository userMemoryChapterRepository;
    private final UserItemRepository userItemRepository;
    private final UserMusicItemRepository userMusicItemRepository;
    private final UserLoginBonusRepository userLoginBonusRepository;
    private final UserEventPointRepository userEventPointRepository;
    private final UserMissionPointRepository userMissionPointRepository;
    private final UserTrainingRoomRepository userTrainingRoomRepository;
    private final UserGeneralDataRepository userGeneralDataRepository;
    private final UserBossRepository userBossRepository;
    private final UserScenarioRepository userScenarioRepository;
    private final UserTechCountRepository userTechCountRepository;
    private final UserTradeItemRepository userTradeItemRepository;
    private final UserEventMusicRepository userEventMusicRepository;
    private final UserTechEventRepository userTechEventRepository;
    private final UserKopRepository userKopRepository;

    @Autowired
    public UpsertUserAllHandler(BasicMapper mapper,
                                CardService cardService, UserDataRepository userDataRepository, UserOptionRepository userOptionRepository, UserPlaylogRepository userPlaylogRepository, UserActivityRepository userActivityRepository, UserMusicDetailRepository userMusicDetailRepository, UserCharacterRepository userCharacterRepository, UserCardRepository userCardRepository, UserDeckRepository userDeckRepository, UserStoryRepository userStoryRepository, UserChapterRepository userChapterRepository, UserItemRepository userItemRepository, UserMusicItemRepository userMusicItemRepository, UserLoginBonusRepository userLoginBonusRepository, UserEventPointRepository userEventPointRepository, UserMissionPointRepository userMissionPointRepository, UserTrainingRoomRepository userTrainingRoomRepository, UserGeneralDataRepository userGeneralDataRepository, UserBossRepository userBossRepository, UserScenarioRepository userScenarioRepository, UserTechCountRepository userTechCountRepository, UserTradeItemRepository userTradeItemRepository, UserEventMusicRepository userEventMusicRepository, UserTechEventRepository userTechEventRepository, UserKopRepository userKopRepository, UserMemoryChapterRepository userMemoryChapterRepository) {
        this.mapper = mapper;
        this.cardService = cardService;
        this.userDataRepository = userDataRepository;
        this.userOptionRepository = userOptionRepository;
        this.userPlaylogRepository = userPlaylogRepository;
        this.userActivityRepository = userActivityRepository;
        this.userMusicDetailRepository = userMusicDetailRepository;
        this.userCharacterRepository = userCharacterRepository;
        this.userCardRepository = userCardRepository;
        this.userDeckRepository = userDeckRepository;
        this.userStoryRepository = userStoryRepository;
        this.userChapterRepository = userChapterRepository;
        this.userMemoryChapterRepository = userMemoryChapterRepository;
        this.userItemRepository = userItemRepository;
        this.userMusicItemRepository = userMusicItemRepository;
        this.userLoginBonusRepository = userLoginBonusRepository;
        this.userEventPointRepository = userEventPointRepository;
        this.userMissionPointRepository = userMissionPointRepository;
        this.userTrainingRoomRepository = userTrainingRoomRepository;
        this.userGeneralDataRepository = userGeneralDataRepository;
        this.userBossRepository = userBossRepository;
        this.userScenarioRepository = userScenarioRepository;
        this.userTechCountRepository = userTechCountRepository;
        this.userTradeItemRepository = userTradeItemRepository;
        this.userEventMusicRepository = userEventMusicRepository;
        this.userTechEventRepository = userTechEventRepository;
        this.userKopRepository = userKopRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        UpsertUserAll upsertUserAll = mapper.convert(request.get("upsertUserAll"), UpsertUserAll.class);

        // All the field should exist, no need to check now.
        // UserData
        UserData newUserData;
        {
            UserData userData;

            Optional<UserData> userOptional = userDataRepository.findByCard_ExtId(userId);

            // UserData might be empty on later runs
            if (userOptional.isEmpty() && upsertUserAll.getUserData().isEmpty()) {
                return null;
            }

            if (userOptional.isPresent()) {
                userData = userOptional.get();
            } else {
                userData = new UserData();
                Card card = cardService.getCardByExtId(userId).orElseThrow();
                userData.setCard(card);
            }

            // If new data exists, use new data. Otherwise, use old data
            newUserData = !upsertUserAll.getUserData().isEmpty() ? upsertUserAll.getUserData().get(0) : userData;

            newUserData.setId(userData.getId());
            newUserData.setCard(userData.getCard());

            // Set eventWatchedDate with lastPlayDate, because client doesn't update it
            newUserData.setEventWatchedDate(userData.getLastPlayDate());
            newUserData.setCmEventWatchedDate(userData.getLastPlayDate());

            userDataRepository.save(newUserData);
        }


        // UserOption
        UserOption newUserOption = upsertUserAll.getUserOption().get(0);

        Optional<UserOption> userOptionOptional = userOptionRepository.findByUser(newUserData);
        UserOption userOption = userOptionOptional.orElseGet(() -> new UserOption(newUserData));

        newUserOption.setId(userOption.getId());
        newUserOption.setUser(userOption.getUser());

        userOptionRepository.save(newUserOption);


        // UserPlaylogList
        List<UserPlaylog> userPlaylogList = upsertUserAll.getUserPlaylogList();
        List<UserPlaylog> newUserPlaylogList = new ArrayList<>();

        for (UserPlaylog newUserPlaylog : userPlaylogList) {
            newUserPlaylog.setUser(newUserData);
            newUserPlaylogList.add(newUserPlaylog);
        }

        userPlaylogRepository.saveAll(newUserPlaylogList);


        // UserSessionlogList, UserJewelboostlogLost doesn't need to be saved for a private server


        // UserActivityList
        List<UserActivity> userActivityList = upsertUserAll.getUserActivityList();
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
        newUserActivityList.sort((a, b) -> Integer.compare(b.getSortNumber(), a.getSortNumber()));
        userActivityRepository.saveAll(newUserActivityList);


        // UserRecentRatingList
        // This thing still need to save to solve the rating drop
        this.saveGeneralData(upsertUserAll.getUserRecentRatingList(), newUserData, "recent_rating_list");


        /*
         * The rating and battle point calculation is little bit complex.
         * So I just create a UserGeneralData class to store this value
         * into a csv format for convenience
         */
        // UserBpBaseList (For calculating Battle point)
        this.saveGeneralData(upsertUserAll.getUserBpBaseList(), newUserData, "battle_point_base");


        // This is the best rating of all charts. Best 30 + 10 after that.
        // userRatingBaseBestList
        this.saveGeneralData(upsertUserAll.getUserRatingBaseBestList(), newUserData, "rating_base_best");


        // userRatingBaseNextList
        this.saveGeneralData(upsertUserAll.getUserRatingBaseNextList(), newUserData, "rating_base_next");


        // This is the best rating of new charts. Best 15 + 10 after that.
        // New chart means same version
        // userRatingBaseBestNewList
        this.saveGeneralData(upsertUserAll.getUserRatingBaseBestNewList(), newUserData, "rating_base_new_best");

        // userRatingBaseNextNewList
        this.saveGeneralData(upsertUserAll.getUserRatingBaseNextNewList(), newUserData, "rating_base_new_next");

        // This is the recent best
        // userRatingBaseHotList
        this.saveGeneralData(upsertUserAll.getUserRatingBaseHotList(), newUserData, "rating_base_hot_best");

        // userRatingBaseHotNextList
        this.saveGeneralData(upsertUserAll.getUserRatingBaseHotNextList(), newUserData, "rating_base_hot_next");


        // UserMusicDetailList
        List<UserMusicDetail> userMusicDetailList = upsertUserAll.getUserMusicDetailList();
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


        // UserCharacterList
        List<UserCharacter> userCharacterList = upsertUserAll.getUserCharacterList();
        List<UserCharacter> newUserCharacterList = new ArrayList<>();

        for (UserCharacter newUserCharacter : userCharacterList) {
            int characterId = newUserCharacter.getCharacterId();

            Optional<UserCharacter> characterOptional = userCharacterRepository.findByUserAndCharacterId(newUserData, characterId);
            UserCharacter userCharacter = characterOptional.orElseGet(() -> new UserCharacter(newUserData));

            newUserCharacter.setId(userCharacter.getId());
            newUserCharacter.setUser(newUserData);
            newUserCharacterList.add(newUserCharacter);
        }
        userCharacterRepository.saveAll(newUserCharacterList);

        // UserCardList
        List<UserCard> userCardList = upsertUserAll.getUserCardList();
        List<UserCard> newUserCardList = new ArrayList<>();

        for (UserCard newUserCard : userCardList) {
            int cardId = newUserCard.getCardId();

            Optional<UserCard> cardOptional = userCardRepository.findByUserAndCardId(newUserData, cardId);
            UserCard userCard = cardOptional.orElseGet(() -> new UserCard(newUserData));

            newUserCard.setId(userCard.getId());
            newUserCard.setUser(newUserData);
            newUserCardList.add(newUserCard);
        }
        userCardRepository.saveAll(newUserCardList);


        // UserDeckList
        List<UserDeck> userDeckList = upsertUserAll.getUserDeckList();
        List<UserDeck> newUserDeckList = new ArrayList<>();

        for (UserDeck newUserDeck : userDeckList) {
            int deckId = newUserDeck.getDeckId();

            Optional<UserDeck> deckOptional = userDeckRepository.findByUserAndDeckId(newUserData, deckId);
            UserDeck userDeck = deckOptional.orElseGet(() -> new UserDeck(newUserData));

            newUserDeck.setId(userDeck.getId());
            newUserDeck.setUser(newUserData);
            newUserDeckList.add(newUserDeck);
        }
        userDeckRepository.saveAll(newUserDeckList);


        // userTrainingRoomList
        List<UserTrainingRoom> userTrainingRoomList = upsertUserAll.getUserTrainingRoomList();
        List<UserTrainingRoom> newUserTrainingRoomList = new ArrayList<>();

        for (UserTrainingRoom newUserTrainingRoom : userTrainingRoomList) {
            int roomId = newUserTrainingRoom.getRoomId();

            Optional<UserTrainingRoom> trainingRoomOptional = userTrainingRoomRepository.findByUserAndRoomId(newUserData, roomId);
            UserTrainingRoom trainingRoom = trainingRoomOptional.orElseGet(() -> new UserTrainingRoom(newUserData));

            newUserTrainingRoom.setId(trainingRoom.getId());
            newUserTrainingRoom.setUser(newUserData);
            newUserTrainingRoomList.add(newUserTrainingRoom);
        }
        userTrainingRoomRepository.saveAll(newUserTrainingRoomList);


        // UserStoryList
        List<UserStory> userStoryList = upsertUserAll.getUserStoryList();
        List<UserStory> newUserStoryList = new ArrayList<>();

        for (UserStory newUserStory : userStoryList) {
            int storyId = newUserStory.getStoryId();

            Optional<UserStory> storyOptional = userStoryRepository.findByUserAndStoryId(newUserData, storyId);
            UserStory userStory = storyOptional.orElseGet(() -> new UserStory(newUserData));

            newUserStory.setId(userStory.getId());
            newUserStory.setUser(newUserData);
            newUserStoryList.add(newUserStory);
        }
        userStoryRepository.saveAll(newUserStoryList);


        // UserChapterList
        List<UserChapter> userChapterList = upsertUserAll.getUserChapterList();
        List<UserChapter> newUserChapterList = new ArrayList<>();

        for (UserChapter newUserChapter : userChapterList) {
            int chapterId = newUserChapter.getChapterId();

            Optional<UserChapter> chapterOptional = userChapterRepository.findByUserAndChapterId(newUserData, chapterId);
            UserChapter userChapter = chapterOptional.orElseGet(() -> new UserChapter(newUserData));

            newUserChapter.setId(userChapter.getId());
            newUserChapter.setUser(newUserData);
            newUserChapterList.add(newUserChapter);
        }
        userChapterRepository.saveAll(newUserChapterList);


        // UserMemoryChapterList
        List<UserMemoryChapter> userMemoryChapterList = upsertUserAll.getUserMemoryChapterList();
        
        if (userMemoryChapterList != null) {
            List<UserMemoryChapter> newUserMemoryChapterList = new ArrayList<>();

            for (UserMemoryChapter newUserMemoryChapter : userMemoryChapterList) {
                int chapterId = newUserMemoryChapter.getChapterId();
    
                Optional<UserMemoryChapter> chapterOptional = userMemoryChapterRepository.findByUserAndChapterId(newUserData, chapterId);
                UserMemoryChapter userChapter = chapterOptional.orElseGet(() -> new UserMemoryChapter(newUserData));
    
                newUserMemoryChapter.setId(userChapter.getId());
                newUserMemoryChapter.setUser(newUserData);
                newUserMemoryChapterList.add(newUserMemoryChapter);
            }
            userMemoryChapterRepository.saveAll(newUserMemoryChapterList);
        }

        // UserItemList
        List<UserItem> userItemList = upsertUserAll.getUserItemList();
        List<UserItem> newUserItemList = new ArrayList<>();

        for (UserItem newUserItem : userItemList) {
            int itemKind = newUserItem.getItemKind();
            int itemId = newUserItem.getItemId();

            Optional<UserItem> itemOptional = userItemRepository.findByUserAndItemKindAndItemId(newUserData, itemKind, itemId);
            UserItem userItem = itemOptional.orElseGet(() -> new UserItem(newUserData));

            newUserItem.setId(userItem.getId());
            newUserItem.setUser(newUserData);
            newUserItemList.add(newUserItem);
        }
        userItemRepository.saveAll(newUserItemList);

        // UserMusicItemList
        List<UserMusicItem> userMusicItemList = upsertUserAll.getUserMusicItemList();
        List<UserMusicItem> newUserMusicItemList = new ArrayList<>();

        for (UserMusicItem newUserMusicItem : userMusicItemList) {
            int musicId = newUserMusicItem.getMusicId();

            Optional<UserMusicItem> musicItemOptional = userMusicItemRepository.findByUserAndMusicId(newUserData, musicId);
            UserMusicItem userMusicItem = musicItemOptional.orElseGet(() -> new UserMusicItem(newUserData));

            newUserMusicItem.setId(userMusicItem.getId());
            newUserMusicItem.setUser(newUserData);
            newUserMusicItemList.add(newUserMusicItem);
        }
        userMusicItemRepository.saveAll(newUserMusicItemList);


        // userLoginBonusList
        List<UserLoginBonus> userLoginBonusList = upsertUserAll.getUserLoginBonusList();
        List<UserLoginBonus> newUserLoginBonusList = new ArrayList<>();

        for (UserLoginBonus newUserLoginBonus : userLoginBonusList) {
            int bonusId = newUserLoginBonus.getBonusId();

            Optional<UserLoginBonus> loginBonusOptional = userLoginBonusRepository.findByUserAndBonusId(newUserData, bonusId);
            UserLoginBonus userLoginBonus = loginBonusOptional.orElseGet(() -> new UserLoginBonus(newUserData));

            newUserLoginBonus.setId(userLoginBonus.getId());
            newUserLoginBonus.setUser(newUserData);
            newUserLoginBonusList.add(newUserLoginBonus);
        }
        userLoginBonusRepository.saveAll(newUserLoginBonusList);


        // UserEventPointList
        List<UserEventPoint> userEventPointList = upsertUserAll.getUserEventPointList();
        List<UserEventPoint> newUserEventPointList = new ArrayList<>();

        for (UserEventPoint newUserEventPoint : userEventPointList) {
            int eventId = newUserEventPoint.getEventId();

            Optional<UserEventPoint> eventPointOptional = userEventPointRepository.findByUserAndEventId(newUserData, eventId);
            UserEventPoint userEventPoint = eventPointOptional.orElseGet(() -> new UserEventPoint(newUserData));

            newUserEventPoint.setId(userEventPoint.getId());
            newUserEventPoint.setUser(newUserData);
            newUserEventPointList.add(newUserEventPoint);
        }
        userEventPointRepository.saveAll(newUserEventPointList);


        // UserMissionPointList
        List<UserMissionPoint> userMissionPointList = upsertUserAll.getUserMissionPointList();
        List<UserMissionPoint> newUserMissionPointList = new ArrayList<>();

        for (UserMissionPoint newUserMissionPoint : userMissionPointList) {
            int eventId = newUserMissionPoint.getEventId();

            Optional<UserMissionPoint> userMissionPointOptional = userMissionPointRepository.findByUserAndEventId(newUserData, eventId);
            UserMissionPoint userMissionPoint = userMissionPointOptional.orElseGet(() -> new UserMissionPoint(newUserData));

            newUserMissionPoint.setId(userMissionPoint.getId());
            newUserMissionPoint.setUser(newUserData);
            newUserMissionPointList.add(newUserMissionPoint);
        }
        userMissionPointRepository.saveAll(newUserMissionPointList);

        // UserRatinglogList (For the highest rating of each version)

        // UserBossList
        List<UserBoss> userBossList = upsertUserAll.getUserBossList();
        if (userBossList != null) {
            List<UserBoss> newUserBossList = new ArrayList<>();
            for (UserBoss newUserBoss : userBossList) {
                int musicId = newUserBoss.getMusicId();

                Optional<UserBoss> userBossOptional = userBossRepository.findByUserAndMusicId(newUserData, musicId);
                UserBoss userBoss = userBossOptional.orElseGet(() -> new UserBoss(newUserData));

                newUserBoss.setId(userBoss.getId());
                newUserBoss.setUser(userBoss.getUser());
                newUserBossList.add(newUserBoss);
            }
            userBossRepository.saveAll(newUserBossList);
        }

        // UserTechCountList
        List<UserTechCount> userTechCountList = upsertUserAll.getUserTechCountList();
        if (userTechCountList != null) {
            List<UserTechCount> newUserTechCountList = new ArrayList<>();
            for (UserTechCount newUserTechCount : userTechCountList) {
                int levelId = newUserTechCount.getLevelId();

                Optional<UserTechCount> userTechCountOptional = userTechCountRepository.findByUserAndLevelId(newUserData, levelId);
                UserTechCount userTechCount = userTechCountOptional.orElseGet(() -> new UserTechCount(newUserData));

                newUserTechCount.setId(userTechCount.getId());
                newUserTechCount.setUser(userTechCount.getUser());
                newUserTechCountList.add(newUserTechCount);
            }
            userTechCountRepository.saveAll(newUserTechCountList);
        }

        // UserScenarioList
        List<UserScenario> userScenarioList = upsertUserAll.getUserScenarioList();
        if (userScenarioList != null) {
            List<UserScenario> newUserScenarioList = new ArrayList<>();
            for (UserScenario newUserScenario : userScenarioList) {
                int scenarioId = newUserScenario.getScenarioId();

                Optional<UserScenario> userScenarioOptional = userScenarioRepository.findByUserAndScenarioId(newUserData, scenarioId);
                UserScenario userScenario = userScenarioOptional.orElseGet(() -> new UserScenario(newUserData));

                newUserScenario.setId(userScenario.getId());
                newUserScenario.setUser(userScenario.getUser());
                newUserScenarioList.add(newUserScenario);
            }
            userScenarioRepository.saveAll(newUserScenarioList);
        }

        // UserTradeItemList
        List<UserTradeItem> userTradeItemList = upsertUserAll.getUserTradeItemList();
        List<UserTradeItem> newUserTradeItemList = new ArrayList<>();

        for (UserTradeItem newUserTradeItem : userTradeItemList) {
            int chapterId = newUserTradeItem.getChapterId();
            int tradeItemId = newUserTradeItem.getTradeItemId();

            Optional<UserTradeItem> tradeItemOptional = userTradeItemRepository.findByUserAndChapterIdAndTradeItemId(newUserData, chapterId, tradeItemId);
            UserTradeItem userTradeItem = tradeItemOptional.orElseGet(() -> new UserTradeItem(newUserData));

            newUserTradeItem.setId(userTradeItem.getId());
            newUserTradeItem.setUser(newUserData);
            newUserTradeItemList.add(newUserTradeItem);
        }
        userTradeItemRepository.saveAll(newUserTradeItemList);

        // UserEventMusicList
        List<UserEventMusic> userEventMusicList = upsertUserAll.getUserEventMusicList();
        List<UserEventMusic> newUserEventMusicList = new ArrayList<>();

        for (UserEventMusic newUserEventMusic : userEventMusicList) {
            int eventId = newUserEventMusic.getEventId();
            int type = newUserEventMusic.getType();
            int musicId = newUserEventMusic.getMusicId();

            Optional<UserEventMusic> eventMusicOptional = userEventMusicRepository.findByUserAndEventIdAndTypeAndMusicId(newUserData, eventId, type, musicId);
            UserEventMusic userEventMusic = eventMusicOptional.orElseGet(() -> new UserEventMusic(newUserData));

            newUserEventMusic.setId(userEventMusic.getId());
            newUserEventMusic.setUser(newUserData);
            newUserEventMusicList.add(newUserEventMusic);
        }
        userEventMusicRepository.saveAll(newUserEventMusicList);

        // UserTechEventList
        List<UserTechEvent> userTechEventList = upsertUserAll.getUserTechEventList();
        List<UserTechEvent> newUserTechEventList = new ArrayList<>();

        for (UserTechEvent newUserTechEvent : userTechEventList) {
            int eventId = newUserTechEvent.getEventId();

            Optional<UserTechEvent> techEventOptional = userTechEventRepository.findByUserAndEventId(newUserData, eventId);
            UserTechEvent userTechEvent = techEventOptional.orElseGet(() -> new UserTechEvent(newUserData));

            newUserTechEvent.setId(userTechEvent.getId());
            newUserTechEvent.setUser(newUserData);
            newUserTechEventList.add(newUserTechEvent);
        }
        userTechEventRepository.saveAll(newUserTechEventList);

        // UserKopList
        List<UserKop> userKopList = upsertUserAll.getUserKopList();
        List<UserKop> newUserKopList = new ArrayList<>();

        for (UserKop newUserKop : userKopList) {
            int kopId = newUserKop.getKopId();
            int areaId = newUserKop.getAreaId();

            Optional<UserKop> kopOptional = userKopRepository.findByUserAndKopIdAndAreaId(newUserData, kopId, areaId);
            UserKop userKop = kopOptional.orElseGet(() -> new UserKop(newUserData));

            newUserKop.setId(userKop.getId());
            newUserKop.setUser(newUserData);
            newUserKopList.add(newUserKop);
        }
        userKopRepository.saveAll(newUserKopList);

        String json = mapper.write(new CodeResp(1, "upsertUserAll"));
        logger.info("Response: " + json);
        return json;

    }

    private void saveGeneralData(List<UserRecentRating> itemList, UserData newUserData, String key) {
        StringBuilder sb = new StringBuilder();
        // Convert to a string
        for (UserRecentRating item :
                itemList) {
            sb.append(item.getMusicId()).append(":").append(item.getDifficultId()).append(":").append(item.getScore());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        Optional<UserGeneralData> uOptional = userGeneralDataRepository.findByUserAndPropertyKey(newUserData, key);
        UserGeneralData userGeneralData = uOptional.orElseGet(() -> new UserGeneralData(newUserData, key));
        userGeneralData.setPropertyValue(sb.toString());
        userGeneralDataRepository.save(userGeneralData);
    }
}
