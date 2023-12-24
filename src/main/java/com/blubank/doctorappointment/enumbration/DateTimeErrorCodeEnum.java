package com.blubank.doctorappointment.enumbration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DateTimeErrorCodeEnum{
    dateNotValid(101,"day of month not valid ,please enter day between 1 and 31 "),
    timeNotValid(102, "Time in day not valid, please enter time between 00:00 and 23:59"),
    endTimeBeforeStartTime(102, "endTime is before startTime"),
    equalsTime(102, "endTime and startTime is equals"),
    startTimeNotValid(102,"start time is not valid, please enter time between 00:00 and 23:59"),
    endTimeNotValid(102, "end time is not valid, please enter time between 00:00 and 23:59");

     DateTimeErrorCodeEnum(int errorCode , String errorDescription ){
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    private int errorCode;
    private String errorDescription;

    @JsonValue
    public int getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorDescription(){
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription){
        this.errorDescription = errorDescription;
    }

}
