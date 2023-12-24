package com.blubank.doctorappointment.dto;

import lombok.Data;

@Data
public class DoctorDTO extends AbaseDto{
    private Long id;
    private String name;
}