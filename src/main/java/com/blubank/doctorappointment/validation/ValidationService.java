package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.dto.AbaseDto;
import com.blubank.doctorappointment.response.AbaseResponse;
import com.blubank.doctorappointment.response.Response;

import java.util.List;

public interface ValidationService<T extends AbaseDto,R extends AbaseResponse>{


    public boolean validate(T t , List<R> response);
    public boolean validate(T t , R response);
}
