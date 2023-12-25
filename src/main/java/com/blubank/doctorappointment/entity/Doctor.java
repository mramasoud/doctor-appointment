package com.blubank.doctorappointment.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name ="DOCTOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorsId;
    private String name;

    public Doctor(String name) {
        this.name = name;
    }
}