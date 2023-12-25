package com.blubank.doctorappointment.ordinal;

import lombok.Getter;

@Getter
public enum CodeProjectEnum{
    doctorNotFound(404),
    AppointmentNotFound(404),
    appointmentSaved(200),
    appointmentReserved(406),
    doctorSaved(200),
    appointmentDeleted(200),
    appointmentNotSaved(200),
    serverError(500),
    duplicateTime(500),
    duplicate(500);

    private int errorCode;


    CodeProjectEnum(int errorCode ){
        this.errorCode = errorCode;

    }

    public int getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }
}
