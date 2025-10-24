package icu.samnyan.aqua.sega.diva.handler.ingame;

import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerScreenShotRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.ingame.StoreSsRequest;
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerScreenShot;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static icu.samnyan.aqua.sega.diva.util.DivaStringUtils.arrToCsv;


/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class StoreSsHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(StoreSsHandler.class);
    private final PlayerProfileService playerProfileService;
    private final PlayerScreenShotRepository screenShotRepository;

    public String handle(StoreSsRequest request, MultipartFile file) {
        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);

        BaseResponse response;
        try {
            String filename = request.getPd_id() + "-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".jpg";
            Files.write(Paths.get("data/" + filename), file.getBytes());

            PlayerScreenShot ss = new PlayerScreenShot(
                    profile,
                    filename,
                    request.getPd_id(),
                    arrToCsv(request.getSs_mdl_id()),
                    arrToCsv(request.getSs_c_itm_id())
            );
            screenShotRepository.save(ss);

            response = new BaseResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok");

        } catch (IOException e) {
            logger.error("Screenshot save failed", e);

            response = new BaseResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "0");
        }

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }

}
