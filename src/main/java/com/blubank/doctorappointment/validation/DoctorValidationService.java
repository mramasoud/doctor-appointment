package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.ordinal.DateTimeErrorCodeEnum;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class DoctorValidationService implements ValidationService<DoctorAvailabilityDTO>{
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @Override
    public boolean validate(DoctorAvailabilityDTO dto , List<Response> responses){
        if(! DateUtil.dateTimeIsValid(dto.getStartTime()))
          //  throw new ValidationException(messages.getString("startTimeNotValid"));
            responses.add(new Response(DateTimeErrorCodeEnum.startTimeNotValid.getErrorCode() , DateTimeErrorCodeEnum.startTimeNotValid.getErrorDescription()));
        if(! DateUtil.dateTimeIsValid(dto.getEndTime()))
            //throw new ValidationException(messages.getString("endTimeNotValid"));
            responses.add(new Response(DateTimeErrorCodeEnum.endTimeNotValid.getErrorCode() , DateTimeErrorCodeEnum.endTimeNotValid.getErrorDescription()));
        if(! DateUtil.dateTimeIsValid(dto.getStartTime() , dto.getEndTime()))
           // throw new ValidationException(messages.getString("timeNotValid"));
            responses.add(new Response(DateTimeErrorCodeEnum.timeNotValid.getErrorCode() , DateTimeErrorCodeEnum.timeNotValid.getErrorDescription()));
        if(! DateUtil.timeIsValid(dto.getStartTime() , dto.getEndTime()))
            //throw new ValidationException(messages.getString("endTimeBeforeStartTime"));
            responses.add(new Response(DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorCode() , DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorDescription()));
        if(! DateUtil.dayOfMonthValidation(dto.getDayOfMonth()))
           // throw new ValidationException(messages.getString("dateNotValid"));
            responses.add(new Response(DateTimeErrorCodeEnum.dateNotValid.getErrorCode() ,DateTimeErrorCodeEnum.dateNotValid.getErrorDescription()));
        if(DateUtil.equalsTime(dto.getStartTime(),dto.getEndTime()))
           // throw new ValidationException(messages.getString("equalsTime"));
            responses.add(new Response(DateTimeErrorCodeEnum.equalsTime.getErrorCode() ,DateTimeErrorCodeEnum.equalsTime.getErrorDescription()));
        return responses.size() == 0;
    }
}

