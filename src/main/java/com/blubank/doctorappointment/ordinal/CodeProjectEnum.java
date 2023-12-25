package com.blubank.doctorappointment.ordinal;

import lombok.Getter;

@Getter
public enum CodeProjectEnum{
    doctorNotFound(404,"doctor not found"),
    AppointmentNotFound(404,"Appointment not found"),
    appointmentSaved(200,"time period appointments have been created."),
    appointmentReserved(406,"cannot delete appointment because appointment is reserving."),
    doctorSaved(200,"doctor have been created."),
    appointmentDeleted(200,"appointment have been deleted."),
    appointmentNotSaved(200,"A 30-minute time period was not found on a working day."),
    serverError(500,"serverError"),
    duplicateTime(500,"duplicate time work in day:  "),
    duplicate(500,"duplicate item ");

    private int errorCode;
    private String errorDescription;

    CodeProjectEnum(int errorCode , String errorDescription){
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
