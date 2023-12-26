package com.blubank.doctorappointment.dto;

import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate dayOfMonth;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private AppointmentStatus status;

    public AppointmentDTO(LocalDate dayOfMonth , LocalTime startTime , LocalTime endTime , AppointmentStatus status, DoctorDTO doctorDTO ){
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfMonth = dayOfMonth;
        this.doctorDTO = doctorDTO;
        this.status = status;
    }
}