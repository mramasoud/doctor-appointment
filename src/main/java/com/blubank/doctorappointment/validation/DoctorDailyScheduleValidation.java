package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.ordinal.DateTimeErrorCodeEnum;
import com.blubank.doctorappointment.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;

@Service
public class DoctorDailyScheduleValidation implements ValidationService<DoctorAvailabilityDTO,DoctorDailyScheduleResponse>{
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");


    @Override
    public boolean validate(DoctorAvailabilityDTO request , DoctorDailyScheduleResponse response){
        boolean result= true;

        if(! DateUtil.dateTimeIsValid(request.getStartTime())){
            response.setCode(DateTimeErrorCodeEnum.startTimeNotValid.getErrorCode());
            response.setMessage(messages.getString("startTimeNotValid"));
            result = false;
        }
        if(! DateUtil.dateTimeIsValid(request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.endTimeNotValid.getErrorCode());
            response.setMessage(messages.getString("endTimeNotValid"));
            result = false;
        }
        if(! DateUtil.dateTimeIsValid(request.getStartTime() , request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.timeNotValid.getErrorCode());
            response.setMessage(messages.getString("timeNotValid"));
            result = false;
        }
        if(! DateUtil.timeIsValid(request.getStartTime() , request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorCode());
            response.setMessage(messages.getString("endTimeBeforeStartTime"));
            result = false;

        }
        if(! DateUtil.dayOfMonthValidation(request.getDayOfMonth())){
            response.setCode(DateTimeErrorCodeEnum.dateNotValid.getErrorCode());
            response.setMessage(messages.getString("dateNotValid"));
            result = false;
        }
        if(DateUtil.equalsTime(request.getStartTime() , request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.equalsTime.getErrorCode());
            response.setMessage(messages.getString("equalsTime"));
            result = false;
        }
        return result;
    }
}

