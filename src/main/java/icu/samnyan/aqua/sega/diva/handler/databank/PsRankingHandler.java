package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerPvRecordRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Difficulty;
import icu.samnyan.aqua.sega.diva.model.common.Edition;
import icu.samnyan.aqua.sega.diva.model.common.collection.PsRankingCollection;
import icu.samnyan.aqua.sega.diva.model.request.databank.PsRankingRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.PsRankingResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvRecord;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class PsRankingHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PsRankingHandler.class);
    private final PlayerPvRecordRepository playerPvRecordRepository;

    public String handle(PsRankingRequest request) {
        Edition edition = Edition.ORIGINAL;
        Difficulty difficulty = Difficulty.HARD;

        switch (request.getRnk_ps_idx()) {
            case 0:
                difficulty = Difficulty.HARD;
                break;
            case 1:
                difficulty = Difficulty.EXTREME;
                break;
            case 2: {
                difficulty = Difficulty.EXTREME;
                edition = Edition.EXTRA;
                break;
            }
        }

        int[] list = request.getRnk_ps_pv_id_lst();
        Map<Integer, PsRankingCollection> resultCollections = new LinkedHashMap<>();
        for (int i :
                list) {

            List<PlayerPvRecord> records = playerPvRecordRepository.findTop3ByPvIdAndEditionAndDifficultyOrderByMaxScoreDesc(i, edition, difficulty);
            resultCollections.put(i, new PsRankingCollection(i, edition, records));
        }

        List<Integer> pvIds = new LinkedList<>();
        List<Integer> edition1 = new LinkedList<>();
        List<Integer> edition2 = new LinkedList<>();
        List<Integer> edition3 = new LinkedList<>();
        List<Integer> score1 = new LinkedList<>();
        List<Integer> score2 = new LinkedList<>();
        List<Integer> score3 = new LinkedList<>();
        List<String> name1 = new LinkedList<>();
        List<String> name2 = new LinkedList<>();
        List<String> name3 = new LinkedList<>();

        resultCollections.forEach((key, obj) -> {
            pvIds.add(key);
            edition1.add(obj.getFirst().getEdition().getValue());
            edition2.add(obj.getSecond().getEdition().getValue());
            edition3.add(obj.getThird().getEdition().getValue());
            score1.add(obj.getFirst().getMaxScore());
            score2.add(obj.getSecond().getMaxScore());
            score3.add(obj.getThird().getMaxScore());
            name1.add(URIEncoder.encode(obj.getFirst().getPdId() != null ? obj.getFirst().getPdId().getPlayerName() : "xxx"));
            name2.add(URIEncoder.encode(obj.getSecond().getPdId() != null ? obj.getSecond().getPdId().getPlayerName() : "xxx"));
            name3.add(URIEncoder.encode(obj.getThird().getPdId() != null ? obj.getThird().getPdId().getPlayerName() : "xxx"));
        });

        PsRankingResponse response = new PsRankingResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                LocalDateTime.now(),
                LocalDateTime.now(),
                request.getRnk_ps_idx(),
                pvIds.stream().map(Object::toString).collect(Collectors.joining(",")),
                edition1.stream().map(Object::toString).collect(Collectors.joining(",")),
                edition2.stream().map(Object::toString).collect(Collectors.joining(",")),
                edition3.stream().map(Object::toString).collect(Collectors.joining(",")),
                score1.stream().map(Object::toString).collect(Collectors.joining(",")),
                score2.stream().map(Object::toString).collect(Collectors.joining(",")),
                score3.stream().map(Object::toString).collect(Collectors.joining(",")),
                name1.stream().map(Object::toString).collect(Collectors.joining(",")),
                name2.stream().map(Object::toString).collect(Collectors.joining(",")),
                name3.stream().map(Object::toString).collect(Collectors.joining(","))
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
