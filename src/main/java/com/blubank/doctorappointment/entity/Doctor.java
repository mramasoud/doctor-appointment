package com.blubank.doctorappointment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name ="DOCTOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctors_Id;
    private String name;

}