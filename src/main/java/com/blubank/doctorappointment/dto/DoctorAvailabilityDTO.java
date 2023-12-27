package com.blubank.doctorappointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
public class DoctorAvailabilityDTO extends AbaseDto{

    private String doctorName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayOfMonth;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public DoctorAvailabilityDTO(LocalDate dayOfMonth , LocalTime startTime , LocalTime endTime){
        this.dayOfMonth = dayOfMonth;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DoctorAvailabilityDTO(String doctorName, LocalDate dayOfMonth, LocalTime startTime, LocalTime endTime) {
        this.doctorName = doctorName;
        this.dayOfMonth = dayOfMonth;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DoctorAvailabilityDTO() {
    }
}

