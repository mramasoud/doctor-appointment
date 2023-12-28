package com.blubank.doctorappointment.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode
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
