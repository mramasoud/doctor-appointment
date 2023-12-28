package com.blubank.doctorappointment.model.ordinal;

import lombok.Getter;

@Getter
public enum CodeProjectEnum{

    notFound(404),
    savedItem(200),
    appointmentReserved(406),
    appointmentDeleted(202),
    appointmentNotSaved(400),
    serverError(500),
    duplicate(409);

    private int code;


    CodeProjectEnum(int code){
        this.code = code;

    }

    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }
}
