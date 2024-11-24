package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCourse;
import icu.samnyan.aqua.sega.chusan.service.UserCourseService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle GetUserCourse request
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanGetUserCourseHandler")
public class GetUserCourseHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserCourseHandler.class);

    private final StringMapper mapper;

    private final UserCourseService userCourseService;

    @Autowired
    public GetUserCourseHandler(StringMapper mapper, UserCourseService userCourseService) {
        this.mapper = mapper;
        this.userCourseService = userCourseService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);

        if(request.containsKey("nextIndex")) {
            int nextIndex = Integer.parseInt((String) request.get("nextIndex"));
            int maxCount = Integer.parseInt((String) request.get("maxCount"));

            int pageNum = nextIndex / maxCount;

            Page<UserCourse> dbPage = userCourseService.getByUserId(userId, pageNum, maxCount);

            long currentIndex = maxCount * pageNum + dbPage.getNumberOfElements();

            resultMap.put("length", dbPage.getNumberOfElements());
            resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : currentIndex);
            resultMap.put("userCourseList", dbPage.getContent());
        } else {
            List<UserCourse> courseList = userCourseService.getByUserId(userId);
            resultMap.put("length", courseList.size());
            resultMap.put("userCourseList", courseList);
        }

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
