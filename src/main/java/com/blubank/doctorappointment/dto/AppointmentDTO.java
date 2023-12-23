package com.blubank.doctorappointment.dto;

import com.blubank.doctorappointment.enumbration.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private String patientName;
    private String patientPhoneNumber;
    private AppointmentStatus status;
    private Long doctorId;
    private Long PatientId;


}