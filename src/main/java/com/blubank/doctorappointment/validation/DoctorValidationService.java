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
public class DoctorValidationService implements ValidationService<DoctorAvailabilityDTO,Response>{
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");
    @Override
    public boolean validate(DoctorAvailabilityDTO dto , List<Response> responses){
        if(! DateUtil.dateTimeIsValid(dto.getStartTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.startTimeNotValid.getErrorCode() , messages.getString("startTimeNotValid")));
        if(! DateUtil.dateTimeIsValid(dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.endTimeNotValid.getErrorCode() , messages.getString("endTimeNotValid")));
        if(! DateUtil.dateTimeIsValid(dto.getStartTime() , dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.timeNotValid.getErrorCode() , messages.getString("timeNotValid")));
        if(! DateUtil.timeIsValid(dto.getStartTime() , dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorCode() , messages.getString("endTimeBeforeStartTime")));
        if(! DateUtil.dayOfMonthValidation(dto.getDayOfMonth()))
            responses.add(new Response(DateTimeErrorCodeEnum.dateNotValid.getErrorCode() ,messages.getString("dateNotValid")));
        if(DateUtil.equalsTime(dto.getStartTime(),dto.getEndTime()))
            responses.add(new Response(DateTimeErrorCodeEnum.equalsTime.getErrorCode() ,messages.getString("equalsTime")));
        return responses.size() == 0;
    }

    @Override
    public boolean validate(DoctorAvailabilityDTO dto , Response response){
        return false;
    }
}

