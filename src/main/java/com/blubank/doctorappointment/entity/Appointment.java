package com.blubank.doctorappointment.entity;

import com.blubank.doctorappointment.enumbration.AppointmentStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Appointment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column
    private LocalDate start;
    @Column
    private LocalDate end;
    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private Patient patient;
    @Enumerated(EnumType.ORDINAL)
    private AppointmentStatus status;

}