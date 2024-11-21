package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.chusan.dto.UserMusicDetailForAncientChusan;
import icu.samnyan.aqua.sega.chusan.dto.UserMusicListItemForAncientChusan;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.response.data.UserMusicListItem;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserMusicDetail;
import icu.samnyan.aqua.sega.chusan.service.UserMusicDetailService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import icu.samnyan.aqua.spring.data.OffsetPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Response:
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanGetUserMusicHandler")
public class GetUserMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMusicHandler.class);

    private final StringMapper mapper;

    private final UserMusicDetailService userMusicDetailService;

    @Autowired
    public GetUserMusicHandler(StringMapper mapper, UserMusicDetailService userMusicDetailService) {
        this.mapper = mapper;
        this.userMusicDetailService = userMusicDetailService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        int currentIndex = Integer.parseInt((String) request.get("nextIndex"));
        int maxCount = Integer.parseInt((String) request.get("maxCount"));
        if(currentIndex < 0) {
            currentIndex = 0;
        }

        Page<UserMusicDetail> dbPage = userMusicDetailService
                .getByUserId(userId, OffsetPageRequest.of(currentIndex, maxCount, Sort.by("musicId")));


        // Convert to result format
        // Result Map
        Map<Integer, UserMusicListItem> userMusicMap = new LinkedHashMap<>();

        dbPage.getContent().forEach(userMusicDetail -> {
            UserMusicListItem list;
            if (userMusicMap.containsKey(userMusicDetail.getMusicId())) {
                list = userMusicMap.get(userMusicDetail.getMusicId());
            } else {
                list = new UserMusicListItem(0, new ArrayList<>());
                userMusicMap.put(userMusicDetail.getMusicId(), list);
            }
            list.getUserMusicDetailList().add(userMusicDetail);
            list.setLength(list.getUserMusicDetailList().size());
        });

        // Remove the last music id if the result length is the same as maxCount,
        // to prevent a music id split across multiple page, which will cause some
        // problem with the game.
        int lastListSize = 0;
        if(dbPage.getNumberOfElements() >= maxCount) {
            // Get last key
            int lastMusicId = userMusicMap.keySet().stream().reduce((a, b) -> b).orElseThrow();
            List<UserMusicDetail> lastList = userMusicMap.get(lastMusicId).getUserMusicDetailList();
            lastListSize = lastList.size();
            // Remove last one from map
            userMusicMap.remove(lastMusicId);
        }

        String version = (String) request.get("version");
        Map<Integer, UserMusicListItemForAncientChusan> userMusicMapForAncientChusan = new LinkedHashMap<>();
        boolean isAncient = false;
        try {
            if (Double.parseDouble(version) < 2.15){
                userMusicMap.forEach((k, v) -> {
                    UserMusicListItemForAncientChusan list = new UserMusicListItemForAncientChusan();
                    list.setLength(v.getLength());
                    List<UserMusicDetailForAncientChusan> userMusicDetailForAncientChusanList = new ArrayList<>();
                    v.getUserMusicDetailList().forEach(userMusicDetail -> {
                        UserMusicDetailForAncientChusan userMusicDetailForAncientChusan = new UserMusicDetailForAncientChusan(
                                userMusicDetail.getMusicId(),
                                userMusicDetail.getLevel(),
                                userMusicDetail.getPlayCount(),
                                userMusicDetail.getScoreMax(),
                                userMusicDetail.getMissCount(),
                                userMusicDetail.getMaxComboCount(),
                                userMusicDetail.isFullCombo(),
                                userMusicDetail.isAllJustice(),
                                userMusicDetail.getIsSuccess(),
                                userMusicDetail.getFullChain(),
                                userMusicDetail.getMaxChain(),
                                userMusicDetail.getScoreRank(),
                                userMusicDetail.isLock(),
                                userMusicDetail.getTheoryCount(),
                                userMusicDetail.getExt1()
                        );
                        userMusicDetailForAncientChusanList.add(userMusicDetailForAncientChusan);
                    });
                    list.setUserMusicDetailList(userMusicDetailForAncientChusanList);
                    userMusicMapForAncientChusan.put(k, list);
                });
                isAncient = true;
            }
        } catch (Exception e) {
            logger.error("Error when handling ancient version of chusan", e);
        }

        long nextIndex = currentIndex + dbPage.getNumberOfElements() - lastListSize;

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userMusicMap.size());
        resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : nextIndex);
        resultMap.put("userMusicList", isAncient ? userMusicMapForAncientChusan.values() : userMusicMap.values());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
