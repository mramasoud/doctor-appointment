package com.blubank.doctorappointment.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String name;
    private String phoneNumber;
}