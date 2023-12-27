package com.blubank.doctorappointment.ordinal;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DateTimeErrorCodeEnum{
    dateNotValid(400),
    timeNotValid(400 ),
    endTimeBeforeStartTime(400 ),
    equalsTime(400 ),
    startTimeNotValid(400 ),
    endTimeNotValid(400 );

     DateTimeErrorCodeEnum(int errorCode ){
        this.errorCode = errorCode;

    }

    private int errorCode;


    @JsonValue
    public int getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }


}
