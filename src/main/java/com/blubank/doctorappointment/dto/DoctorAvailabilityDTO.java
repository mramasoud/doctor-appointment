package com.blubank.doctorappointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorAvailabilityDTO {
    private String doctorName;
    private int dayOfMonth; // The day of the month
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime; // The start time for the day
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime; // The end time for the day

    public DoctorAvailabilityDTO(int dayOfMonth, LocalTime startTime, LocalTime endTime) {
        this.dayOfMonth = dayOfMonth;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

