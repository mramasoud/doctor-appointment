package com.blubank.doctorappointment.ordinal;

import lombok.Getter;

@Getter
public enum DoctorCodeProjectEnum{
    doctorNotFound(404,"doctor not found"),
    appointmentSaved(200,"  time period appointments have been created."),
    appointmentNotSaved(200,"A 30-minute time period was not found on a working day."),
    serverError(500,"serverError");


    private int errorCode;
    private String errorDescription;

    DoctorCodeProjectEnum(int errorCode , String errorDescription){
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

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
