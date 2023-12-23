package com.blubank.doctorappointment.mapper;

import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DoctorDTO toDTO(Doctor doctor);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Doctor toEntity(DoctorDTO doctorDTO);
}