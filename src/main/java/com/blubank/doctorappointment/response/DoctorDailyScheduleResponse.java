package com.blubank.doctorappointment.response;

import lombok.Data;

@Data
public class DoctorDailyScheduleResponse extends AbaseResponse{

    private int code;
    private String message;

    public DoctorDailyScheduleResponse(){
    }

    public DoctorDailyScheduleResponse(String message) {
        this.message = message;
    }

    public DoctorDailyScheduleResponse(int code , String message){
        this.code = code;
        this.message = message;
    }
}
