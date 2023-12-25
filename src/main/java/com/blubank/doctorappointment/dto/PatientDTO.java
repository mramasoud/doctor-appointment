package com.blubank.doctorappointment.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PatientDTO extends AbaseDto{
    private Long id;
    @Pattern(regexp = "[a-zA-Z]+")
    private String name;
    @Pattern(regexp = "^09[0|1|2|3][0-9]{8}$")
    private String phoneNumber;
}