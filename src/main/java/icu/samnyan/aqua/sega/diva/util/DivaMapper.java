package icu.samnyan.aqua.sega.diva.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import icu.samnyan.aqua.sega.util.BooleanNumberDeserializer;
import icu.samnyan.aqua.sega.util.BooleanNumberSerializer;
import icu.samnyan.aqua.sega.util.ZonedDateTimeDeserializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class DivaMapper {
    private final ObjectMapper mapper;

    public DivaMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new DivaDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0")));
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        module.addSerializer(Boolean.class, new BooleanNumberSerializer());
        module.addSerializer(boolean.class, new BooleanNumberSerializer());
        module.addDeserializer(Boolean.class, new BooleanNumberDeserializer());
        module.addDeserializer(boolean.class, new BooleanNumberDeserializer());

        mapper = JsonMapper.builder().enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        mapper.registerModule(module);
    }

    public String write(Object o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);

    }

    public <T> T convert(Map<String, Object> map, Class<T> toClass) {
        return mapper.convertValue(map, toClass);
    }

    public LinkedHashMap<String, Object> toMap(Object object) {
        return mapper.convertValue(object, new TypeReference<>() {
        });
    }
}
