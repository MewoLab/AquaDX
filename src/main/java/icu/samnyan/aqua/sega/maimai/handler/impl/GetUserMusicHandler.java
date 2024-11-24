package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserMusicDetailRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.data.UserMusic;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserMusicDetail;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserMusicHandler")
public class GetUserMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMusicHandler.class);

    private final BasicMapper mapper;

    private final UserMusicDetailRepository userMusicDetailRepository;

    public GetUserMusicHandler(BasicMapper mapper, UserMusicDetailRepository userMusicDetailRepository) {
        this.mapper = mapper;
        this.userMusicDetailRepository = userMusicDetailRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        long nextIndex = ((Number) request.get("nextIndex")).longValue();
        int maxCount = ((Number) request.get("maxCount")).intValue();

        List<Integer> musicIds = userMusicDetailRepository.findMusicIdsByUser_Card_ExtIdAndOffset(userId, nextIndex, PageRequest.of(0, maxCount));

        List<UserMusicDetail> detailList = userMusicDetailRepository.findByUser_Card_ExtIdAndMusicIdIn(userId, musicIds);

        Map<Integer, UserMusic> userMusicMap = new LinkedHashMap<>();

        detailList.forEach(music -> {
            UserMusic userMusic;
            if (userMusicMap.containsKey(music.getMusicId())) {
                userMusic = userMusicMap.get(music.getMusicId());
            } else {
                userMusic = new UserMusic(new LinkedList<>(), 0);
                userMusicMap.put(music.getMusicId(), userMusic);
            }
            userMusic.getUserMusicDetailList().add(music);
        });

        userMusicMap.forEach((key, val) -> {
            val.setLength(val.getUserMusicDetailList().size());
        });

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", musicIds.size());
        resultMap.put("nextIndex", musicIds.size() != 50 ? 0 : musicIds.get(musicIds.size() - 1) + 1);
        resultMap.put("userMusicList", userMusicMap.values());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
