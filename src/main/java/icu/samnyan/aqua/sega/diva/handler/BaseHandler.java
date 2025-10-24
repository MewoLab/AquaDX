package icu.samnyan.aqua.sega.diva.handler;

import icu.samnyan.aqua.sega.diva.util.DivaMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class BaseHandler {
    static protected DivaMapper mapper = new DivaMapper();

    protected String build(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, val) -> {
            if (val != null) {
                if (val instanceof String) {
                    if (!val.equals("")) {
                        sb.append(key).append("=");
                        sb.append(val);
                        sb.append("&");
                    }
                } else {
                    sb.append(key).append("=");
//                sb.append(URLEncoder.encode(String.valueOf(val), StandardCharsets.UTF_8));
                    sb.append(val);
                    sb.append("&");
                }
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
