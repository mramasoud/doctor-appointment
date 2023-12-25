package com.blubank.doctorappointment.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name ="PATIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientsId;
    @Column
    private String name;
    @Column
    private String phoneNumber;

    public Patient(String name , String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}