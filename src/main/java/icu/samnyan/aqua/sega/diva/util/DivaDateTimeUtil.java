package icu.samnyan.aqua.sega.diva.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public class DivaDateTimeUtil {

    public static String getString(LocalDateTime time) {
        return URIEncoder.encode(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0").format(time));
    }

    public static String format(LocalDateTime time) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0").format(time);
    }
}
