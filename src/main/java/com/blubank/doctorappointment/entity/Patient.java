package com.blubank.doctorappointment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name ="PATIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patients_Id;
    @Column
    private String name;
    @Column
    private String phoneNumber;

}