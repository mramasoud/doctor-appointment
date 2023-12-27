package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.CacheService;
import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
public class PatientService {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    CacheService cacheService;

    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    public List<DoctorAppointmentViewResponse> showPatientFreeDoctorAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        return appointments.stream()
                .map(appointment -> {
                    DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
                    response.setDigit(appointments.indexOf(appointment) + 1L);
                    response.setStartTime(appointment.getStartTime());
                    response.setEndTime(appointment.getEndTime());
                    response.setStatus(appointment.getStatus());
                    Optional<Patient> patient = Optional.ofNullable(appointment.getPatient());
                    patient.ifPresent(p -> {
                        response.setPatientName(p.getName());
                        response.setPatientPhoneNumber(p.getPhoneNumber());
                    });
                    return response;
                })
                .collect(Collectors.toList());

    }

    public List<DoctorAppointmentViewResponse> findAppointmentByPatient(String phone) {
        List<DoctorAppointmentViewResponse> responses = new ArrayList<>();
        Optional<Patient> patient = patientRepository.findByPhoneNumber(phone);
        if (patient.isPresent()) {
            List<Appointment> appointments = appointmentService.findAppointmentByPatientPhone(patient.get());
            for (Appointment appointment : appointments) {
                responses.add(new DoctorAppointmentViewResponse(appointments.indexOf(appointment) + 1L, appointment.getStartTime(), appointment.getEndTime(), appointment.getStatus(), patient.get().getName(), patient.get().getPhoneNumber()));
            }
        }
        return responses;
    }

    @Transactional
    public Appointment getAppointmentForPatient(PatientReservingAppointmentDTO dto) {
        try {
            Doctor doctor = cacheService.findDoctor(1L);
            Optional.ofNullable(doctor.getName()).orElseThrow(() -> new NotFoundException(messages.getString("doctorNotFound")));
            List<Appointment> appointments = appointmentService.findEmptyAppointmentByDoctor(doctor, dto.getDayOfMonth());
            if (appointments.isEmpty()) {
                throw new NotFoundException(messages.getString("appointmentFreeNotFound"));
            }
            Appointment appointment = appointments.get(dto.getAppointmentDigit() - 1);
            switch (appointment.getStatus()) {
                case reserved:
                case reserving:
                    throw new NotFoundException(messages.getString("appointmentNotAvailable"));
                default:
                    reservingAppointment(appointment.getAppointmentsId());
                    return appointment;
            }
        } catch (NotFoundException e) {
            throw new NotFoundException(messages.getString("appointmentFreeNotFound"));
        }
    }

    @Transactional
    public Response unreserved(Long id) {
        try {
            Optional<Appointment> appointmentById = appointmentService.findAppointmentById(id);
            if (appointmentById.isPresent()) {
                appointmentById.get().setStatus(AppointmentStatus.empty);
                appointmentService.saveAppointment(appointmentById.get());
                return new Response("success");
            } else throw new NotFoundException(messages.getString("appointmentNotFound"));
        } catch (NotFoundException e) {
            return new Response(messages.getString("appointmentReservationFailed"));
        }
    }

    @Transactional
    public void reservingAppointment(Long id) {
        try {
            Optional<Appointment> appointmentById = appointmentService.findAppointmentById(id);
            if (appointmentById.isPresent()) {
                appointmentById.get().setStatus(AppointmentStatus.reserving);
                appointmentService.saveAppointment(appointmentById.get());
            } else throw new NotFoundException(messages.getString("appointmentNotFound"));
        } catch (NotFoundException e) {
            throw new NotFoundException(messages.getString("appointmentNotFound"));
        }
    }

    @Transactional
    public DoctorAppointmentViewResponse reserveAppointment(FinalPatientReserveAppointmentDTO dto) {
        DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
        Optional<Appointment> appointment = appointmentService.findAppointmentById(dto.getAppointmentDigit());
        if (appointment.isPresent()) {
            appointment.get().setStatus(AppointmentStatus.reserved);
            appointment.get().setPatient(addPatient(dto.getName(), dto.getPhoneNumber()));
            appointmentService.saveAppointment(appointment.get());
            response.setDigit(1);
            response.setStartTime(appointment.get().getStartTime());
            response.setEndTime(appointment.get().getEndTime());
            response.setPatientName(dto.getName());
            response.setPatientPhoneNumber(dto.getPhoneNumber());
            response.setStatus(appointment.get().getStatus());
        }


        return response;
    }

    @Transactional
    public Patient addPatient(String name, String phone) {
        Optional<Patient> byPhoneNumber = patientRepository.findByPhoneNumber(phone);
        return byPhoneNumber.orElseGet(() -> patientRepository.save(new Patient(name, phone)));
    }
}
