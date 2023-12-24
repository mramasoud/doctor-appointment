package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.enumbration.DateTimeErrorCodeEnum;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorValidationService implements ValidationService<DoctorAvailabilityDTO>{
    @Override
    public boolean validate(DoctorAvailabilityDTO dto , List<Response> responses){
        if(! DateUtil.dateTimeIsValid(dto.getStartTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.startTimeNotValid.getErrorCode() , DateTimeErrorCodeEnum.startTimeNotValid.getErrorDescription()));
        if(! DateUtil.dateTimeIsValid(dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.endTimeNotValid.getErrorCode() , DateTimeErrorCodeEnum.endTimeNotValid.getErrorDescription()));
        if(! DateUtil.dateTimeIsValid(dto.getStartTime() , dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.timeNotValid.getErrorCode() , DateTimeErrorCodeEnum.timeNotValid.getErrorDescription()));
        if(! DateUtil.timeIsValid(dto.getStartTime() , dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorCode() , DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorDescription()));
        if(! DateUtil.dayOfMonthValidation(dto.getDayOfMonth()))
            responses.add(new Response(DateTimeErrorCodeEnum.dateNotValid.getErrorCode() ,DateTimeErrorCodeEnum.dateNotValid.getErrorDescription()));
        if(DateUtil.equalsTime(dto.getStartTime(),dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.equalsTime.getErrorCode() ,DateTimeErrorCodeEnum.equalsTime.getErrorDescription()));
        return responses.size() == 0;
    }
}

