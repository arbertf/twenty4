package com.twenty4.util;

import java.time.*;

public class DateUtil {
    public static ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static LocalDate convertToLocalDate(Long longDate) {
        return Instant.ofEpochMilli(longDate).atZone(UTC_ZONE_ID).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Long longDate) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), UTC_ZONE_ID);
    }

    public static LocalTime convertToLocalTime(Long longTime) {
        return LocalTime.ofInstant(Instant.ofEpochMilli(longTime), UTC_ZONE_ID);
    }

    public static long localDateTimeToMilliseconds(LocalDateTime localDateTime){
        if(localDateTime==null)
            return 0;
        return ZonedDateTime.of(localDateTime, UTC_ZONE_ID).toInstant().toEpochMilli();
    }

    public static long localDateToMilliseconds(LocalDate localDate){
        if(localDate==null)
            return 0;
        Instant instant = localDate.atStartOfDay(UTC_ZONE_ID).toInstant();
        return instant.toEpochMilli();
    }

    public static LocalDateTime now(){
        return LocalDateTime.now(UTC_ZONE_ID);
    }

    public static LocalDate nowLocalDate(){
        return LocalDate.now(UTC_ZONE_ID);
    }
}
