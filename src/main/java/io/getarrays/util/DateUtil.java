package io.getarrays.util;

import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormatter.ofPattern("hh:mm:ss dd-MM-yyyy");
    }
}
