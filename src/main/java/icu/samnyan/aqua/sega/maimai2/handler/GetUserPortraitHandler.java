package icu.samnyan.aqua.sega.maimai2.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.net.db.AquaNetUser;
import icu.samnyan.aqua.net.utils.PathProps;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.general.dao.CardRepository;
import icu.samnyan.aqua.sega.general.model.Card;
import icu.samnyan.aqua.sega.maimai2.model.request.data.UserPortrait;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("Maimai2GetUserPortraitHandler")
public class GetUserPortraitHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserPortraitHandler.class);

    private final BasicMapper mapper;
    private final boolean enable;
    private final CardRepository cardRepo;
    private final String portraitPath;

    public GetUserPortraitHandler(BasicMapper mapper,
    @Value("${game.maimai2.userPhoto.enable:true}") boolean enable,
    CardRepository cardRepo,
    PathProps paths) {
        this.mapper = mapper;
        this.enable = enable;
        this.cardRepo = cardRepo;
        this.portraitPath = paths.getAquaNetPortrait();
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        if (enable) {
            var userId = ((Number) request.get("userId")).longValue();
            var list = new ArrayList<UserPortrait>();
            var card = cardRepo.findByExtId(userId);
            var user = card.map(Card::getAquaUser);
            var profilePicture = user.map(AquaNetUser::getProfilePicture).orElse(null);

            try {
                if (!StringUtils.isEmpty(profilePicture)) {
                    var filePath = Paths.get(portraitPath, profilePicture);
                    var buffer = new byte[10240];

                    var stream = new FileInputStream(filePath.toFile());
                    while (stream.available() > 0) {
                        var read = stream.read(buffer, 0, 10240);

                        var encodeBuffer = read == 10240 ? buffer : Arrays.copyOfRange(buffer, 0, read);

                        var userPortrait = new UserPortrait();

                        userPortrait.setFileName("portrait.jpg");
                        userPortrait.setPlaceId(0);
                        userPortrait.setUserId(userId);
                        userPortrait.setClientId("");
                        userPortrait.setUploadDate("1970-01-01 09:00:00.0");
                        userPortrait.setDivData(Utf8.decode(Base64.getEncoder().encode(encodeBuffer)));

                        userPortrait.setDivNumber(list.size());

                        list.add(userPortrait);
                    }

                    stream.close();
                    for (var i = 0; i < list.size(); i++) {
                        var userPortrait = list.get(i);
                        userPortrait.setDivLength(list.size());
                    }

                    var map = new HashMap<String, Object>();
                    map.put("length", list.size());
                    map.put("userPortraitList", list);

                    var respJson = mapper.write(map);
                    return respJson;
                }
            } catch (Exception e) {
                logger.error("Result: User photo save failed", e);
            }
        }

        return "{\"length\":0,\"userPortraitList\":[]}";
    }
}
