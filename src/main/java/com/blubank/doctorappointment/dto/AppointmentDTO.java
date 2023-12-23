package com.blubank.doctorappointment.dto;

import com.blubank.doctorappointment.enumbration.AppointmentStatus;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class AppointmentDTO {
    private Long id;
    private Date startTime;
    private Date endTime;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private AppointmentStatus status;
}