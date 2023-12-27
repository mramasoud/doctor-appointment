package com.blubank.doctorappointment.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil{


    public static boolean dateTimeIsValid(LocalTime startTime , LocalTime endTime){

        return startTime.isAfter(LocalTime.of(0 , 0)) && endTime.isBefore(LocalTime.of(23 , 59));
    }
    public static boolean timeIsValid(LocalTime startTime , LocalTime endTime){

        return  !startTime.isAfter(endTime);
    }
    public static boolean equalsTime(LocalTime startTime , LocalTime endTime){
        return startTime.equals(endTime);
    }

    public static boolean dateTimeIsValid(LocalTime time){
        return time.isAfter(LocalTime.of(0 , 0)) && time.isBefore(LocalTime.of(23 , 59));
    }

    public static boolean dayOfMonthValidation(LocalDate day){
        return day.isAfter(LocalDate.now().minusDays(1));
    }

    public static Date dateConvertor(LocalTime time){
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault()).with(time);
        return Date.from(dateTime.toInstant());
    }
    public static LocalTime dateConvertor(Date time){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault());
        return localDateTime.toLocalTime();
    }



}
