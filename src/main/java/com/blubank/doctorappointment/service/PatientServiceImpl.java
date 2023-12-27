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
import com.sun.jdi.request.DuplicateRequestException;
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
public class PatientServiceImpl implements PatientService{
    @Autowired
    AppointmentService appointmentServiceImpl;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    CacheService cacheService;
    public final static Long CONSTANT_NUMBER_TO_START_FROM_ONE = 1L;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @Override
    public List<DoctorAppointmentViewResponse> showPatientFreeDoctorAppointments(){
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
                    return doctorAppointmentViewResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorAppointmentViewResponse> findAppointmentByPatient(String phone){
        List<DoctorAppointmentViewResponse> responses = new ArrayList<>();
        Optional<Patient> patient = patientRepository.findByPhoneNumber(phone);
        if(patient.isPresent()){
            List<Appointment> appointments = appointmentServiceImpl.findAppointmentByPatientPhone(patient.get());
            for(Appointment appointment : appointments){
                responses.add(new DoctorAppointmentViewResponse(
                        appointments.indexOf(appointment) + CONSTANT_NUMBER_TO_START_FROM_ONE ,
                        appointment.getStartTime() ,
                        appointment.getEndTime() ,
                        appointment.getStatus() ,
                        patient.get().getName() ,
                        patient.get().getPhoneNumber()));
            }
        }
        return responses;
    }

    @Override
    @Transactional
    public Appointment getAppointmentForPatient(PatientReservingAppointmentDTO dto){
        try{
            Doctor doctor = cacheService.findDoctor(1L);
            Optional.ofNullable(doctor.getName()).orElseThrow(() -> new NotFoundException(messages.getString("doctorNotFound")));
            List<Appointment> appointments = appointmentServiceImpl.findEmptyAppointmentByDoctor(doctor , dto.getDayOfMonth());
            if(appointments.isEmpty()){
                throw new NotFoundException(messages.getString("appointmentFreeNotFound"));
            }
            Appointment appointment = appointments.get(dto.getAppointmentDigit() - 1);
            switch(appointment.getStatus()){
                case reserved:
                case reserving:
                    throw new NotFoundException(messages.getString("appointmentNotAvailable"));
                default:
                    reservingAppointment(appointment.getAppointmentsId());
                    return appointment;
            }
        }catch(NotFoundException e){
            throw new NotFoundException(messages.getString("appointmentFreeNotFound"));
        }
    }

    @Override
    @Transactional
    public Response unreserved(Long id){
        try{
            Optional<Appointment> appointmentById = appointmentServiceImpl.findAppointmentById(id);
            if(appointmentById.isPresent()){
                appointmentById.get().setStatus(AppointmentStatus.empty);
                appointmentServiceImpl.saveAppointment(appointmentById.get());
                return new Response("success");
            }else throw new NotFoundException(messages.getString("appointmentNotFound"));
        }catch(NotFoundException e){
            return new Response(messages.getString("appointmentReservationFailed"));
        }
    }

    @Override
    @Transactional
    public void reservingAppointment(Long id){
        try{
            Optional<Appointment> appointmentById = appointmentServiceImpl.findAppointmentById(id);
            if(appointmentById.isPresent()){
                if(appointmentById.get().getStatus()==AppointmentStatus.empty){
                    appointmentById.get().setStatus(AppointmentStatus.reserving);
                    appointmentServiceImpl.saveAppointment(appointmentById.get());
                }else throw new DuplicateRequestException(messages.getString("appointmentNotAvailable"));
            }else throw new NotFoundException(messages.getString("appointmentNotFound"));
        }catch(NotFoundException e){
            throw new NotFoundException(messages.getString("appointmentNotFound"));
        }catch(DuplicateRequestException e){
            throw new DuplicateRequestException(messages.getString("appointmentNotAvailable"));
        }
    }

    @Override
    @Transactional
    public DoctorAppointmentViewResponse reserveAppointment(FinalPatientReserveAppointmentDTO dto){
        DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
        Optional<Appointment> appointment = appointmentServiceImpl.findAppointmentById(dto.getAppointmentDigit());
        if(appointment.isPresent()){
            appointment.get().setStatus(AppointmentStatus.reserved);
            appointment.get().setPatient(addPatient(dto.getName() , dto.getPhoneNumber()));
            appointmentServiceImpl.saveAppointment(appointment.get());
            response.setDigit(1);
            response.setStartTime(appointment.get().getStartTime());
            response.setEndTime(appointment.get().getEndTime());
            response.setPatientName(dto.getName());
            response.setPatientPhoneNumber(dto.getPhoneNumber());
            response.setStatus(appointment.get().getStatus());
        }


        return response;
    }

    @Override
    @Transactional
    public Patient addPatient(String name , String phone){
        Optional<Patient> byPhoneNumber = patientRepository.findByPhoneNumber(phone);
        return byPhoneNumber.orElseGet(() -> patientRepository.save(new Patient(name , phone)));
    }
}
