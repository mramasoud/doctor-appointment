package com.blubank.doctorappointment.response;

import lombok.Data;

@Data
public class DeleteAppointmentResponse extends AbaseResponse{
    private int code;
    private String message;

    public DeleteAppointmentResponse(String message) {
        this.message = message;
    }

    public DeleteAppointmentResponse(int code , String message){
        this.code = code;
        this.message = message;
    }

}
