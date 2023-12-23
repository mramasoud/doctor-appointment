package com.blubank.doctorappointment.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Patient {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String phoneNumber;
}