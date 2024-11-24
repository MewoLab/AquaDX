package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserMusicDetailRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.UserRivalMusic;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import icu.samnyan.aqua.spring.data.OffsetPageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GEEKiDoS (geek_ds@foxmail.com)
 */
@Component("OngekiGetUserRivalMusicHandler")
public class GetUserRivalMusicHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserRivalMusicHandler.class);

    private final BasicMapper mapper;
    private final UserMusicDetailRepository userMusicDetailRepository;

    @Autowired
    public GetUserRivalMusicHandler(BasicMapper mapper, UserMusicDetailRepository userMusicDetailRepository) {
        this.mapper = mapper;
        this.userMusicDetailRepository = userMusicDetailRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        var userId = ((Number) request.get("userId")).longValue();
        var rivalUserId = ((Number) request.get("rivalUserId")).longValue();
        var nextIndex = ((Number) request.get("nextIndex")).intValue();
        var maxCount = ((Number) request.get("maxCount")).intValue();

        //fix maxCount and sort by musicId so that we could fetch entity music with full fumen diffs.
        var fixedMaxCount = maxCount + (maxCount % 5 == 0 ? 0 : 5);
        var sorter = Sort.by(Sort.Direction.ASC, "musicId");

        var groupedMusicDetailsItor = userMusicDetailRepository
                .findByUser_Card_ExtId(rivalUserId, OffsetPageRequest.of(nextIndex, fixedMaxCount, sorter))
                .stream()
                .collect(Collectors.groupingBy(x -> x.getMusicId()))
                .values()
                .stream()
                .map(x -> new UserRivalMusic(x,x.size()))
                .iterator();

        var filterMusicDetails = new ArrayList<UserRivalMusic>();

        var actualReadCount = 0;
        while(groupedMusicDetailsItor.hasNext()){
            var musicDetail = groupedMusicDetailsItor.next();
            if (musicDetail.getLength() + actualReadCount > maxCount)
                break;
            filterMusicDetails.add(musicDetail);
            actualReadCount += musicDetail.getLength();
        }

        var respNextIndex = nextIndex + actualReadCount;
        if (filterMusicDetails.size() <= 0)
            respNextIndex = 0; //nofity client that All music details had been sent (for this rival).
        else
            filterMusicDetails.sort(Comparator.comparingInt(a -> a.getUserRivalMusicDetailList().get(0).getMusicId()));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("rivalUserId", rivalUserId);
        resultMap.put("length", filterMusicDetails.size());
        resultMap.put("nextIndex", respNextIndex);
        resultMap.put("userRivalMusicList", filterMusicDetails);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
