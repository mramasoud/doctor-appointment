package com.blubank.doctorappointment.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode
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
