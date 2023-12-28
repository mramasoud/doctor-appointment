package com.blubank.doctorappointment.model.dto;

import com.blubank.doctorappointment.model.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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