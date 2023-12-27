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
    public boolean validate(DoctorAvailabilityDTO dto , List<DoctorDailyScheduleResponse> response){
        return false;
    }

    @Override
    public boolean validate(DoctorAvailabilityDTO request , DoctorDailyScheduleResponse response){
        DoctorDailyScheduleResponse doctorDailyScheduleResponse = new DoctorDailyScheduleResponse();
        if(! DateUtil.dateTimeIsValid(request.getStartTime()))
            new DoctorDailyScheduleResponse(DateTimeErrorCodeEnum.startTimeNotValid.getErrorCode() , messages.getString("startTimeNotValid"));
        if(! DateUtil.dateTimeIsValid(request.getEndTime()))
            new DoctorDailyScheduleResponse(DateTimeErrorCodeEnum.endTimeNotValid.getErrorCode() , messages.getString("endTimeNotValid"));
        if(! DateUtil.dateTimeIsValid(request.getStartTime() , request.getEndTime()))
            new DoctorDailyScheduleResponse(DateTimeErrorCodeEnum.timeNotValid.getErrorCode() , messages.getString("timeNotValid"));
        if(! DateUtil.timeIsValid(request.getStartTime() , request.getEndTime()))
            new DoctorDailyScheduleResponse(DateTimeErrorCodeEnum.endTimeBeforeStartTime.getErrorCode() , messages.getString("endTimeBeforeStartTime"));
        if(! DateUtil.dayOfMonthValidation(request.getDayOfMonth()))
            new DoctorDailyScheduleResponse(DateTimeErrorCodeEnum.dateNotValid.getErrorCode() , messages.getString("dateNotValid"));
        if(DateUtil.equalsTime(request.getStartTime() , request.getEndTime()))
            new DoctorDailyScheduleResponse(DateTimeErrorCodeEnum.equalsTime.getErrorCode() , messages.getString("equalsTime"));
        return doctorDailyScheduleResponse.getMessage() == null;
    }
}

