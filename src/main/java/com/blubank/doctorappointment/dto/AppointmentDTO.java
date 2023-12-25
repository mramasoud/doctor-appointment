package com.blubank.doctorappointment.dto;

import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import lombok.Data;

import java.util.Date;

@Data
public class AppointmentDTO {
    private Long id;
    private Date startTime;
    private Date endTime;
    private Integer dayOfMonth;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private AppointmentStatus status;

    public AppointmentDTO( Integer dayOfMonth ,Date startTime , Date endTime , AppointmentStatus status, DoctorDTO doctorDTO ){
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfMonth = dayOfMonth;
        this.doctorDTO = doctorDTO;
        this.status = status;
    }
}