package icu.samnyan.aqua.sega.diva.handler.databank;

import icu.samnyan.aqua.sega.diva.dao.gamedata.PvEntryRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Difficulty;
import icu.samnyan.aqua.sega.diva.model.gamedata.PvEntry;
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest;
import icu.samnyan.aqua.sega.diva.model.response.databank.PvListResponse;
import icu.samnyan.aqua.sega.diva.util.URIEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class PvListHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(BannerDataHandler.class);
    private final PvEntryRepository pvEntryRepository;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String handle(BaseRequest request) {
        StringBuilder sb = new StringBuilder();

        List<PvEntry> easyList = pvEntryRepository.findByDifficulty(Difficulty.EASY);
        List<PvEntry> normalList = pvEntryRepository.findByDifficulty(Difficulty.NORMAL);
        List<PvEntry> hardList = pvEntryRepository.findByDifficulty(Difficulty.HARD);
        List<PvEntry> extremeList = pvEntryRepository.findByDifficulty(Difficulty.EXTREME);

        sb.append(URIEncoder.encode(difficultyString(easyList))).append(",");
        sb.append(URIEncoder.encode(difficultyString(normalList))).append(",");
        sb.append(URIEncoder.encode(difficultyString(hardList))).append(",");
        sb.append(URIEncoder.encode(difficultyString(extremeList))).append(",");
        sb.append("%2A%2A%2A");

        PvListResponse response = new PvListResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                LocalDateTime.now(),
                sb.toString());

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }

    private String entryString(PvEntry entry) {
        return "" + entry.getPvId() + "," +
                entry.getVersion() + "," +
                entry.getEdition().getValue() + "," +
                df.format(entry.getDemoStart()) + "," +
                df.format(entry.getDemoEnd()) + "," +
                df.format(entry.getPlayableStart()) + "," +
                df.format(entry.getPlayableEnd());
    }

    private String difficultyString(List<PvEntry> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(x -> sb.append(URIEncoder.encode(entryString(x))).append(","));
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
