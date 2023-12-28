package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.entity.Doctor;
import com.blubank.doctorappointment.model.entity.Patient;
import com.blubank.doctorappointment.model.dto.response.DeleteAppointmentResponse;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService{
    void saveAppointment(List<Appointment> availableTimePeriods);

    Appointment saveAppointment(Appointment availableTimePeriod);

    List<Appointment> findAll();

    List<Appointment> findAppointmentByPatientPhone(Patient patient);

    List<Appointment> findFreeAppointmentByDoctor(Doctor doctor , LocalDate day);

    List<Appointment> findEmptyAppointmentByDoctor(Doctor doctor , LocalDate day);

    Optional<Appointment> findAppointmentById(Long id);

    @Transactional
    DeleteAppointmentResponse deleteAppointment(Doctor doctor , int appointmentNumber , LocalDate day);
}
