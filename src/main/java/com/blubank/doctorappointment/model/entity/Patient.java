package com.blubank.doctorappointment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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