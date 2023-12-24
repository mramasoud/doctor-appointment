package com.blubank.doctorappointment.response;

import com.blubank.doctorappointment.dto.AbaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorAppointmentViewResponse extends AbaseDto{
    private long digit;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    public DoctorAppointmentViewResponse(long digit , LocalTime startTime , LocalTime endTime){
        this.digit = digit;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
