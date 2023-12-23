package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.mapper.DoctorMapper;
import com.blubank.doctorappointment.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
public class DoctorService{
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    public Doctor addDoctor(DoctorDTO doctorDTO){
        Doctor doctor = DoctorMapper.INSTANCE.toEntity(doctorDTO);
        return doctorRepository.save(doctor);
    }

    public DoctorDTO getDoctorById(Long id){
        Optional<Doctor> doctorById = doctorRepository.findById(id);
        return doctorById.map(DoctorMapper.INSTANCE :: toDTO).orElseThrow(() -> new NotFoundException("Doctor not found"));
    }
}