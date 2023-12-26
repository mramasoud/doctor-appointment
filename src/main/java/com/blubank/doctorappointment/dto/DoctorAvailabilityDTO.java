package com.blubank.doctorappointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorAvailabilityDTO extends AbaseDto{

    private String doctorName;
    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate dayOfMonth;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public DoctorAvailabilityDTO(String doctorName, LocalDate dayOfMonth, LocalTime startTime, LocalTime endTime) {
        this.doctorName = doctorName;
        this.dayOfMonth = dayOfMonth;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DoctorAvailabilityDTO() {
    }
}

