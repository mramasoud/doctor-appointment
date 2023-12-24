package com.blubank.doctorappointment.validation;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorValidationService implements ValidationService<DoctorAvailabilityDTO>{

    private static final int dateNotValidCode = 101;
    private static final String dateNotValidMessage = "day of month not valid ,please enter day between 1 and 31 ";
    private static final int timeNotValidCode = 102;
    private static final String timeNotValidMessage = "Time in day not valid, please enter time between 00:00 and 23:59";
    private static final String startTimeNotValidMessage = "start time is not valid, please enter time between 00:00 and 23:59";
    private static final String endTimeNotValidMessage = "end time is not valid, please enter time between 00:00 and 23:59";
    @Override
    public boolean validate(DoctorAvailabilityDTO dto , List<Response> responses){
        if(! DateUtil.dateTimeIsValid(dto.getStartTime()))
            responses.add(new Response(timeNotValidCode , startTimeNotValidMessage));
        if(! DateUtil.dateTimeIsValid(dto.getEndTime()))
            responses.add(new Response(timeNotValidCode , endTimeNotValidMessage));
        if(! DateUtil.dateTimeIsValid(dto.getStartTime() , dto.getEndTime()))
            responses.add(new Response(timeNotValidCode , timeNotValidMessage));
        if(! DateUtil.dayOfMonthValidation(dto.getDayOfMonth()))
            responses.add(new Response(dateNotValidCode , dateNotValidMessage));
        return responses.size() == 0;
    }
}

