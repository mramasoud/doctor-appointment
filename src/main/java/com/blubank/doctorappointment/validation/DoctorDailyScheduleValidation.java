package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.model.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.model.ordinal.DateTimeErrorCodeEnum;
import com.blubank.doctorappointment.model.dto.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
@Slf4j
public class DoctorDailyScheduleValidation implements ValidationService<DoctorAvailabilityDTO,DoctorDailyScheduleResponse>{
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");


    @Override
    public boolean validate(DoctorAvailabilityDTO request , DoctorDailyScheduleResponse response){
        boolean result= true;

        if(! DateUtil.dateTimeIsValid(request.getStartTime())){
            response.setCode(DateTimeErrorCodeEnum.startTimeNotValid.getErrorCode());
            response.setMessage(messages.getString("startTimeNotValid"));
            log.info("validation - > startTimeNotValid " + request.getStartTime());
            result = false;
        }
        if(! DateUtil.dateTimeIsValid(request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.endTimeNotValid.getErrorCode());
            response.setMessage(messages.getString("endTimeNotValid"));
            log.info("validation - > endTimeNotValid " + request.getEndTime());
            result = false;
        }
        if(! DateUtil.dateTimeIsValid(request.getStartTime() , request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.timeNotValid.getErrorCode());
            response.setMessage(messages.getString("timeNotValid"));
            log.info("validation - > timeNotValid " + request.getStartTime() +"-"+ request.getEndTime());
            result = false;
        }
        if(!DateUtil.timeIsValid(request.getStartTime() , request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorCode());
            response.setMessage(messages.getString("endTimeBeforeStartTime"));
            log.info("validation - > endTimeBeforeStartTime " + request.getEndTime() +"-"+ request.getEndTime());
            result = false;

        }
        if(! DateUtil.dayOfMonthValidation(request.getDayOfMonth())){
            response.setCode(DateTimeErrorCodeEnum.dateNotValid.getErrorCode());
            response.setMessage(messages.getString("dateNotValid"));
            log.info("validation - > dateNotValid " + request.getDayOfMonth());

            result = false;
        }
        if(DateUtil.equalsTime(request.getStartTime() , request.getEndTime())){
            response.setCode(DateTimeErrorCodeEnum.equalsTime.getErrorCode());
            response.setMessage(messages.getString("equalsTime"));
            log.info("validation - > equalsTime " + request.getStartTime() +"-"+ request.getEndTime());

            result = false;
        }
        return result;
    }
}

