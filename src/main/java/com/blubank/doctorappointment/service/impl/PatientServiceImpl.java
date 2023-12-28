package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.model.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.model.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.entity.Doctor;
import com.blubank.doctorappointment.model.entity.Patient;
import com.blubank.doctorappointment.model.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.model.dto.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.model.dto.response.Response;
import com.blubank.doctorappointment.service.AppointmentService;
import com.blubank.doctorappointment.service.PatientService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PatientServiceImpl implements PatientService {
    @Autowired
    AppointmentService appointmentServiceImpl;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    CacheService cacheService;
    public final static Long CONSTANT_NUMBER_TO_START_FROM_ONE = 1L;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @Override
    public List<DoctorAppointmentViewResponse> showPatientFreeDoctorAppointments() {
        log.info("Getting all appointments for the patient");
        List<Appointment> appointments = appointmentServiceImpl.findAll();

        return appointments.stream()
                .map(appointment -> {
                    DoctorAppointmentViewResponse doctorAppointmentViewResponse = new DoctorAppointmentViewResponse();
                    doctorAppointmentViewResponse.setDigit(appointments.indexOf(appointment) + CONSTANT_NUMBER_TO_START_FROM_ONE);
                    doctorAppointmentViewResponse.setStartTime(appointment.getStartTime());
                    doctorAppointmentViewResponse.setEndTime(appointment.getEndTime());
                    doctorAppointmentViewResponse.setStatus(appointment.getStatus());
                    Optional<Patient> patient = Optional.ofNullable(appointment.getPatient());
                    patient.ifPresent(p -> {
                        doctorAppointmentViewResponse.setPatientName(p.getName());
                        doctorAppointmentViewResponse.setPatientPhoneNumber(p.getPhoneNumber());
                    });
                    log.info("Patient free doctor appointments retrieved successfully " + appointment.getAppointmentsId());
                    return doctorAppointmentViewResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorAppointmentViewResponse> findAppointmentByPatient(String phone) {
        log.info("Finding appointments for the patient with phone number: " + phone);
        List<DoctorAppointmentViewResponse> responses = new ArrayList<>();
        Optional<Patient> patient = patientRepository.findByPhoneNumber(phone);
        if (patient.isPresent()) {
            List<Appointment> appointments = appointmentServiceImpl.findAppointmentByPatientPhone(patient.get());
            for (Appointment appointment : appointments) {
                responses.add(new DoctorAppointmentViewResponse(
                        appointments.indexOf(appointment) + CONSTANT_NUMBER_TO_START_FROM_ONE,
                        appointment.getStartTime(),
                        appointment.getEndTime(),
                        appointment.getStatus(),
                        patient.get().getName(),
                        patient.get().getPhoneNumber()));
            }
        }
        return responses;
    }

    @Override
    @Transactional
    public Appointment reservingAppointmentForPatient(PatientReservingAppointmentDTO dto) {
        log.info("reserving appointment for patient");
        try {
            Doctor doctor = cacheService.findDoctor(1L);
            Optional.ofNullable(doctor.getName()).orElseThrow(() -> new NotFoundException(messages.getString("doctorNotFound")));
            List<Appointment> appointments = appointmentServiceImpl.findEmptyAppointmentByDoctor(doctor, dto.getDayOfMonth());
            if (appointments.isEmpty()) {
                log.info("No appointment found for the patient");
                throw new NotFoundException(messages.getString("appointmentFreeNotFound"));
            }
            Appointment appointment = appointments.get(dto.getAppointmentDigit() - 1);
            switch (appointment.getStatus()) {
                case reserved:
                case reserving:
                    log.info("No appointment found for the patient because reserved " + appointment.getAppointmentsId() + appointment.getStatus());
                    throw new NotFoundException(messages.getString("appointmentNotAvailable"));
                default:
                    log.info("reserving appointment for the patient " + appointment.getAppointmentsId() + appointment.getStatus());
                    reservingAppointment(appointment.getAppointmentsId());
                    return appointment;
            }
        } catch (NotFoundException e) {
            log.info("No appointment found for the patient");
            throw new NotFoundException(messages.getString("appointmentFreeNotFound"));
        }
    }

    @Override
    @Transactional
    public Response unreserved(Long id) {
        log.info("unReserving appointment for the patient " + id);
        try {
            Optional<Appointment> appointmentById = appointmentServiceImpl.findAppointmentById(id);
            if (appointmentById.isPresent()) {
                appointmentById.get().setStatus(AppointmentStatus.empty);
                appointmentById.get().setPatient(null);
                appointmentServiceImpl.saveAppointment(appointmentById.get());
                log.info("unReserving appointment for the patient success " + id);
                return new Response("success");
            } else {
                log.info("No appointment found for the patient");
                throw new NotFoundException(messages.getString("appointmentNotFound"));
            }
        } catch (NotFoundException e) {
            log.info("No appointment found for the patient");
            return new Response(messages.getString("appointmentReservationFailed"));
        }
    }

    @Override
    @Transactional
    public void reservingAppointment(Long id) {
        log.info("start  reserving appointment for patient : " + id);
        try {
            Optional<Appointment> appointmentById = appointmentServiceImpl.findAppointmentById(id);
            if (appointmentById.isPresent()) {
                if (appointmentById.get().getStatus() == AppointmentStatus.empty) {
                    appointmentById.get().setStatus(AppointmentStatus.reserving);
                    appointmentServiceImpl.saveAppointment(appointmentById.get());
                } else throw new DuplicateRequestException(messages.getString("appointmentNotAvailable"));
            } else throw new NotFoundException(messages.getString("appointmentNotFound"));
        } catch (NotFoundException e) {
            log.info("No appointment found for the patient " + id);
            throw new NotFoundException(messages.getString("appointmentNotFound"));
        } catch (DuplicateRequestException e) {
            log.info("Appointment is reserved. " + id);
            throw new DuplicateRequestException(messages.getString("appointmentNotAvailable"));
        }
    }

    @Override
    @Transactional
    public DoctorAppointmentViewResponse reserveAppointment(FinalPatientReserveAppointmentDTO dto) {
        log.info("start final reserving appointment for patient : " + dto.getName());
        DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
        Optional<Appointment> appointment = appointmentServiceImpl.findAppointmentById(dto.getAppointmentDigit());
        if (appointment.isPresent()) {
            appointment.get().setStatus(AppointmentStatus.reserved);
            appointment.get().setPatient(addPatient(dto.getName(), dto.getPhoneNumber()));
            appointmentServiceImpl.saveAppointment(appointment.get());
            response.setDigit(1);
            response.setStartTime(appointment.get().getStartTime());
            response.setEndTime(appointment.get().getEndTime());
            response.setPatientName(dto.getName());
            response.setPatientPhoneNumber(dto.getPhoneNumber());
            response.setStatus(appointment.get().getStatus());
            log.info("add new patient:  " +"appointmentId" +appointment.get().getAppointmentsId()+ "name: " + dto.getName() +"phone: "+dto.getPhoneNumber());
            log.info("Appointment is reserved. " + dto.getAppointmentDigit());
        } else {
            log.info("Appointment is not reserved. " + dto.getAppointmentDigit());
            throw new NotFoundException(messages.getString("appointmentNotFound"));
        }
        return response;
    }

    @Override
    @Transactional
    public Patient addPatient(String name, String phone) {
        Optional<Patient> byPhoneNumber = patientRepository.findByPhoneNumber(phone);
        return byPhoneNumber.orElseGet(() -> patientRepository.save(new Patient(name, phone)));
    }
}
