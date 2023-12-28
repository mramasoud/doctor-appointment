package com.blubank.doctorappointment.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientDTO extends AbaseDto{
    private Long id;
    private String name;
    private String phoneNumber;
}