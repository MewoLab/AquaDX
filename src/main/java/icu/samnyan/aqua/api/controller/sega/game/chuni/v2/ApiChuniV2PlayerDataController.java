package icu.samnyan.aqua.api.controller.sega.game.chuni.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import icu.samnyan.aqua.api.model.MessageResponse;
import icu.samnyan.aqua.api.model.ReducedPageResponse;
import icu.samnyan.aqua.api.model.resp.sega.chuni.v2.RecentResp;
import icu.samnyan.aqua.api.model.resp.sega.chuni.v2.external.Chu3DataExport;
import icu.samnyan.aqua.api.util.ApiMapper;
import icu.samnyan.aqua.sega.chusan.model.userdata.*;
import icu.samnyan.aqua.sega.chusan.service.*;
import icu.samnyan.aqua.sega.general.service.CardService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * For all aimeId parameter, should use String
 * @author samnyan (privateamusement@protonmail.com)
 */
@RestController
@RequestMapping("api/game/chuni/v2")
@ConditionalOnProperty(prefix = "aquaviewer.api", name = "enabled", havingValue = "true")
@AllArgsConstructor
public class ApiChuniV2PlayerDataController {

    private static final Logger logger = LoggerFactory.getLogger(ApiChuniV2PlayerDataController.class);

    private final ApiMapper mapper;

    private final CardService cardService;

    private final UserActivityService userActivityService;
    private final UserCharacterService userCharacterService;
    private final UserChargeService userChargeService;
    private final UserCourseService userCourseService;
    private final UserDataService userDataService;
    private final UserDuelService userDuelService;
    private final UserGameOptionService userGameOptionService;
    private final UserItemService userItemService;
    private final UserMapAreaService userMapAreaService;
    private final UserMusicDetailService userMusicDetailService;
    private final UserPlaylogService userPlaylogService;
    private final UserGeneralDataService userGeneralDataService;

    @PutMapping("profile/username")
    public Chu3UserData updateName(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setUserName((String) request.get("userName"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/romversion")
    public Chu3UserData updateRomVersion(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setLastRomVersion((String) request.get("romVersion"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/dataversion")
    public Chu3UserData updateDataVersion(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setLastDataVersion((String) request.get("dataVersion"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/plate")
    public Chu3UserData updatePlate(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setNameplateId((Integer) request.get("nameplateId"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/frame")
    public Chu3UserData updateFrame(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setFrameId((Integer) request.get("frameId"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/trophy")
    public Chu3UserData updateTrophy(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setTrophyId((Integer) request.get("trophyId"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/mapicon")
    public Chu3UserData updateMapIcon(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setMapIconId((Integer) request.get("mapiconId"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/sysvoice")
    public Chu3UserData updateSystemVoice(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        profile.setVoiceId((Integer) request.get("voiceId"));
        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/avatar")
    public Chu3UserData updateAvatar(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        int category = (Integer) request.get("category");
        switch (category) {
            case 1:
            profile.setAvatarWear((Integer) request.get("accId"));
            break;
            case 2:
            profile.setAvatarHead((Integer) request.get("accId"));
            break;
            case 3:
            profile.setAvatarFace((Integer) request.get("accId"));
            break;
            case 4:
            profile.setAvatarSkin((Integer) request.get("accId"));
            break;
            case 5:
            profile.setAvatarItem((Integer) request.get("accId"));
            break;
            case 6:
            profile.setAvatarFront((Integer) request.get("accId"));
            break;
            case 7:
            profile.setAvatarBack((Integer) request.get("accId"));
            break;
        }

        return userDataService.saveUserData(profile);
    }

    @PutMapping("profile/privacy")
    public ResponseEntity<Object> updatePrivacy(@RequestBody Map<String, Object> request) {
        Chu3UserData profile = userDataService.getUserByExtId((String) request.get("aimeId")).orElseThrow();
        UserGameOption option = userGameOptionService.getByUser(profile).orElseThrow();
        int privacy = (Integer) request.get("privacy");
        if (privacy != 1 && privacy != 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong data"));
        }
        option.setPrivacy(privacy);
        return ResponseEntity.ok(userDataService.saveUserData(profile));
    }

    @GetMapping("recent")
    public ReducedPageResponse<RecentResp> getRecentPlay(@RequestParam String aimeId,
                                          @RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        Page<UserPlaylog> playLogs = userPlaylogService.getRecentPlays(aimeId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userPlayDate")));
        return new ReducedPageResponse<>(mapper.convert(playLogs.getContent(), new TypeReference<>() {
        }), playLogs.getPageable().getPageNumber(), playLogs.getTotalPages(), playLogs.getTotalElements());
    }

    @GetMapping("song/{id}")
    public List<UserMusicDetail> getSongDetail(@RequestParam String aimeId, @PathVariable int id) {
        return userMusicDetailService.getByUserIdAndMusicId(aimeId, id);
    }

    @GetMapping("song/{id}/{level}")
    public List<UserPlaylog> getLevelPlaylog(@RequestParam String aimeId, @PathVariable int id, @PathVariable int level) {
        return userPlaylogService.getByUserIdAndMusicIdAndLevel(aimeId, id, level);
    }

    @GetMapping("song/{id}/isfavorite")
    public boolean getSongFavorite(@RequestParam String aimeId, @PathVariable String id) {
        Optional<UserGeneralData> favOptional;
        favOptional = userGeneralDataService.getByUserIdAndKey(aimeId, "favorite_music");
        if(favOptional.isPresent()) {
            String val = favOptional.get().getPropertyValue();
            if(StringUtils.isNotBlank(val) && val.contains(",")) {
                String[] records = val.split(",");
                for (String record : records) {
                    if (record.equals(id)) return true;
                }
            }
        }
        return false;
    }

    @GetMapping("character")
    public ReducedPageResponse<UserCharacter> getCharacter(@RequestParam String aimeId,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        Page<UserCharacter> characters = userCharacterService.getByUserId(aimeId, page, size);
        return new ReducedPageResponse<>(characters.getContent(), characters.getPageable().getPageNumber(), characters.getTotalPages(), characters.getTotalElements());
    }

    @GetMapping("course")
    public List<UserCourse> getCourse(@RequestParam String aimeId) {
        return userCourseService.getByUserId(aimeId);
    }

    @GetMapping("duel")
    public List<UserDuel> getDuel(@RequestParam String aimeId) {
        return userDuelService.getByUserId(aimeId);
    }

    @GetMapping("item")
    public ReducedPageResponse<UserItem> getItem(@RequestParam String aimeId,
                                  @RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "10") int size) {
        Page<UserItem> items = userItemService.getByUserId(aimeId, page, size);
        return new ReducedPageResponse<>(items.getContent(), items.getPageable().getPageNumber(), items.getTotalPages(), items.getTotalElements());
    }

    @GetMapping("item/{itemKind}")
    public List<UserItem> getItemByKind(@RequestParam String aimeId, @PathVariable int itemKind) {
        return userItemService.getByUserAndItemKind(aimeId, itemKind);
    }

    @GetMapping("general")
    public ResponseEntity<Object> getGeneralData(@RequestParam String aimeId, @RequestParam String key) {
        Optional<UserGeneralData> userGeneralDataOptional = userGeneralDataService.getByUserIdAndKey(aimeId,key);
        return userGeneralDataOptional.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User or value not found.")));
    }

    @GetMapping("export")
    public ResponseEntity<Object> exportAllUserData(@RequestParam String aimeId) {
        Chu3DataExport data = new Chu3DataExport();
        try {
            data.setGameId("SDHD");
            data.setUserData(userDataService.getUserByExtId(aimeId).orElseThrow());
            data.setUserActivityList(userActivityService.getByUserId(aimeId));
            data.setUserCharacterList(userCharacterService.getByUserId(aimeId));
            data.setUserChargeList(userChargeService.getByUserId(aimeId));
            data.setUserCourseList(userCourseService.getByUserId(aimeId));
            data.setUserDuelList(userDuelService.getByUserId(aimeId));
            data.setUserGameOption(userGameOptionService.getByUserId(aimeId).orElseThrow());
            data.setUserItemList(userItemService.getByUserId(aimeId));
            data.setUserMapList(userMapAreaService.getByUserId(aimeId));
            data.setUserMusicDetailList(userMusicDetailService.getByUserId(aimeId));
            data.setUserPlaylogList(userPlaylogService.getByUserId(aimeId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during data export. Reason: " + e.getMessage()));
        }
        // Set filename
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-disposition", "attachment; filename=chusan_" + aimeId + "_exported.json");
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
