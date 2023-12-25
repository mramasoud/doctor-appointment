package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.dto.AbaseDto;
import com.blubank.doctorappointment.response.Response;

import java.util.List;

public interface ValidationService<T extends AbaseDto>{


    public boolean validate(T t , List<Response> response);
}
