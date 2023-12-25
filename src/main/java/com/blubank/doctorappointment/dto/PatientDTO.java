package com.blubank.doctorappointment.dto;

import lombok.Data;

@Data
public class PatientDTO extends AbaseDto{
    private Long id;
    private String name;
    private String phoneNumber;
}