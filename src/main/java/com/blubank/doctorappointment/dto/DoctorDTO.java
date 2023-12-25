package com.blubank.doctorappointment.dto;

import com.blubank.doctorappointment.entity.Doctor;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO extends AbaseDto{
    private Long id;
    private String name;

    public DoctorDTO(String name) {
        this.name = name;
    }

    public static DoctorDTO fromEntity(Doctor doctor){
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getDoctorsId());
        dto.setName(doctor.getName());
        return dto;
    }
}