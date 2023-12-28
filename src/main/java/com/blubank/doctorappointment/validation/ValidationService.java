package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.model.dto.AbaseDto;
import com.blubank.doctorappointment.model.dto.response.AbaseResponse;

public interface ValidationService<T extends AbaseDto,R extends AbaseResponse>{

    public boolean validate(T t , R response);
}
