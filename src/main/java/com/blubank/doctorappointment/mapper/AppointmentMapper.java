package com.blubank.doctorappointment.mapper;

import com.blubank.doctorappointment.dto.AppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    AppointmentDTO toDTO(Appointment appointment);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    Appointment toEntity(AppointmentDTO appointmentDTO);
}