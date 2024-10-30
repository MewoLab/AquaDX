package icu.samnyan.aqua.sega.maimai2.handler;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.maimai2.model.Mai2UserCardRepo;
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserDataRepo;
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserPrintDetailRepo;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai2.model.request.UpsertUserPrint;
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserCard;
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserDetail;
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserPrintDetail;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("Maimai2UpsertUserPrintHandler")
public class UpsertUserPrintHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpsertUserPrintHandler.class);
    private final BasicMapper mapper;

    private final Mai2UserCardRepo userCardRepository;
    private final Mai2UserPrintDetailRepo userPrintDetailRepository;
    private final Mai2UserDataRepo userDataRepository;

    private long expirationTime;

    public UpsertUserPrintHandler(BasicMapper mapper,
                                @Value("${game.cardmaker.card.expiration:15}") long expirationTime,
                                Mai2UserCardRepo userCardRepository,
                                Mai2UserPrintDetailRepo userPrintDetailRepository,
                                Mai2UserDataRepo userDataRepository
                                ) {
        this.mapper = mapper;
        this.expirationTime = expirationTime;
        this.userCardRepository = userCardRepository;
        this.userPrintDetailRepository = userPrintDetailRepository;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public String handle(Map<String, Object> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Mai2UserDetail userData;

        Optional<Mai2UserDetail> userOptional = userDataRepository.findByCardExtId(userId);
        if (userOptional.isPresent()) {
            userData = userOptional.get();
        } else {
            logger.error("User not found. userId: {}", userId);
            return null;
        }

        UpsertUserPrint upsertUserPrint = mapper.convert(request, UpsertUserPrint.class);

        Mai2UserPrintDetail userPrintDetail = upsertUserPrint.getUserPrintDetail();
        Mai2UserCard newUserCard = userPrintDetail.getUserCard();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String currentDateTime = LocalDateTime.now().format(formatter);
        String expirationDateTime = LocalDateTime.now().plusDays(expirationTime).format(formatter);
        String randomSerialId =
            String.format("%010d", ThreadLocalRandom.current().nextLong(0L, 9999999999L)) +
            String.format("%010d", ThreadLocalRandom.current().nextLong(0L, 9999999999L));

        newUserCard.setUser(userData);
        userPrintDetail.setUser(userData);

        newUserCard.setStartDate(currentDateTime);
        newUserCard.setEndDate(expirationDateTime);
        userPrintDetail.setSerialId(randomSerialId);

        Optional<Mai2UserCard> userCardOptional = userCardRepository.findByUserAndCardId(newUserCard.getUser(), newUserCard.getCardId());
        if (userCardOptional.isPresent()) {
            Mai2UserCard userCard = userCardOptional.get();
            newUserCard.setId(userCard.getId());
        }

        userCardRepository.save(newUserCard);
        userPrintDetailRepository.save(userPrintDetail);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("returnCode", 1);
        resultMap.put("orderId", 0);
        resultMap.put("serialId", randomSerialId);
        resultMap.put("startDate", currentDateTime);
        resultMap.put("endDate", expirationDateTime);

        return mapper.write(resultMap);
    }
}
