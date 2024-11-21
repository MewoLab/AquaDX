package icu.samnyan.aqua.sega.maimai2.handler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import icu.samnyan.aqua.sega.general.BaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.maimai2.model.Mai2UserMusicDetailRepo;
import icu.samnyan.aqua.sega.maimai2.model.response.data.UserRivalMusic;
import icu.samnyan.aqua.sega.maimai2.model.response.data.UserRivalMusicDetail;
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserMusicDetail;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;

@Component("Maimai2GetUserRivalMusicHandler")
public class GetUserRivalMusicHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory
            .getLogger(GetUserRivalMusicHandler.class);

    private final StringMapper mapper;
    private final Mai2UserMusicDetailRepo userMusicDetailRepository;

    @Autowired
    public GetUserRivalMusicHandler(StringMapper mapper, Mai2UserMusicDetailRepo userMusicDetailRepository) {
        this.mapper = mapper;
        this.userMusicDetailRepository = userMusicDetailRepository;
    }

    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        long rivalId = ((Number) request.get("rivalId")).intValue();

        List<Mai2UserMusicDetail> details = userMusicDetailRepository.findByUserId(rivalId);
        List<UserRivalMusic> userRivalMusicList = new LinkedList<>();
        Map<Integer, UserRivalMusic> userRivalMusicMap = new HashMap<>();
        for (Mai2UserMusicDetail detail : details) {
            int musicId = detail.getMusicId();
            UserRivalMusic info = userRivalMusicMap.getOrDefault(musicId, null);
            if (info == null) {
                info = new UserRivalMusic(musicId, new LinkedList<>());
                userRivalMusicList.add(info);
                userRivalMusicMap.put(musicId, info);
            }

            info.getUserRivalMusicDetailList().add(
                new UserRivalMusicDetail(detail.getLevel(), detail.getAchievement(), detail.getDeluxscoreMax())
            );
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("rivalId", rivalId);
        resultMap.put("nextIndex", 0);
        resultMap.put("userRivalMusicList", userRivalMusicList);

        return mapper.write(resultMap);
    }
}
