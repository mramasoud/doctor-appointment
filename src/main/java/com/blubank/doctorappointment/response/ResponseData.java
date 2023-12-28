package com.blubank.doctorappointment.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T extends AbaseResponse> {
    private HttpStatus statusCode;
    private String message;
    T result;
    private List<T> body;
}
