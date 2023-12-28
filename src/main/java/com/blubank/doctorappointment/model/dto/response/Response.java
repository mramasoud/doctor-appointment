package com.blubank.doctorappointment.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode
public class Response extends AbaseResponse{

    private int code;
    private String message;

    public Response(String message) {
        this.message = message;
    }

    public Response(int code , String message){
        this.code = code;
        this.message = message;
    }
}
