package com.blubank.doctorappointment.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public static boolean dayOfMonthValidation(int day){
        return day > 0 && day < 32;
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
