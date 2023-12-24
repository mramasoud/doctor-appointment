package com.blubank.doctorappointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorAvailabilityDTO extends AbaseDto{
    private String doctorName;
    private int dayOfMonth;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public DoctorAvailabilityDTO(int dayOfMonth , LocalTime startTime , LocalTime endTime){
        this.dayOfMonth = dayOfMonth;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

