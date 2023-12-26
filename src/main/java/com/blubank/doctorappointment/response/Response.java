package com.blubank.doctorappointment.response;

import lombok.Data;

@Data
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
