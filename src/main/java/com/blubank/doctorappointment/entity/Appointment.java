package com.blubank.doctorappointment.entity;

import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "APPOINTMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentsId;
    @Column
    private LocalTime startTime;
    @Column
    private LocalTime endTime;
    @Column
    private LocalDate dayOfMonth;
    @Column
    private AppointmentStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;


    public Appointment(LocalTime startTime, LocalTime endTime, LocalDate dayOfMonth, AppointmentStatus status, Doctor doctor) {
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfMonth = dayOfMonth;
        this.status = status;
    }
}
