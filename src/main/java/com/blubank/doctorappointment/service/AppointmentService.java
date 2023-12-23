package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.AppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.mapper.AppointmentMapper;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository , DoctorRepository doctorRepository , PatientRepository patientRepository){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Appointment addAppointment(AppointmentDTO appointmentDTO){
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId()).orElseThrow(() -> new NotFoundException("Doctor not found"));
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId()).orElseThrow(() -> new NotFoundException("Patient not found"));
        Appointment appointment = AppointmentMapper.INSTANCE.toEntity(appointmentDTO);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }

}