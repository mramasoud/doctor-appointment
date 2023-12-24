package com.blubank.doctorappointment.entity;

import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APPOINTMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointments_Id;
    @Column
    private Date startTime;
    @Column
    private Date endTime;
    @Column
    private Integer dayOfMonth;
    @Column
    private AppointmentStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Appointment(Date startTime , Date endTime , Integer dayOfMonth , AppointmentStatus status , Doctor doctor){
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfMonth = dayOfMonth;
        this.status = status;
        this.doctor = doctor;
    }
}
