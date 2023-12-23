package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.PatientDTO;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.mapper.PatientMapper;
import com.blubank.doctorappointment.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService{
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(PatientDTO patientDTO){
        Patient patient = PatientMapper.INSTANCE.toEntity(patientDTO);
        return patientRepository.save(patient);
    }
}