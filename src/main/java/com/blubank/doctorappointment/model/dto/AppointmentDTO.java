package com.blubank.doctorappointment.model.dto;

import com.blubank.doctorappointment.model.ordinal.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentDTO {
    private Long id;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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