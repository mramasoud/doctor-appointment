package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.PatientReserveAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService{
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    PatientRepository patientRepository;


    public List<DoctorAppointmentViewResponse> showDoctorFreeAppointments(String doctorName , int dayOfMonth){

        Doctor doctor = doctorRepository.findByName(doctorName);
        List<Appointment> appointments = appointmentService.findEmptyAppointmentByDoctor(doctor , dayOfMonth);
        return appointments.stream()
                .map(appointment -> {
                    DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
                    response.setDigit(appointments.indexOf(appointment) + 1L);
                    response.setStartTime(DateUtil.dateConvertor(appointment.getStartTime()));
                    response.setEndTime(DateUtil.dateConvertor(appointment.getEndTime()));
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

    public DoctorAppointmentViewResponse reserveAppointment(PatientReserveAppointmentDTO dto){
        Doctor doctor = doctorRepository.findByName(dto.getDoctorName());
        if(doctor == null){
            throw new NotFoundException("doctor notfound ");
        }
        List<Appointment> appointments = appointmentService.findEmptyAppointmentByDoctor(doctor , dto.getDayOfMonth());
        if(appointments.isEmpty()){
            throw new NotFoundException("No free appointments available");
        }
        Appointment appointment = appointments.get(dto.getAppointmentDigit() - 1);
        if(appointment.getStatus() == AppointmentStatus.reserved || appointment.getStatus() == AppointmentStatus.reserving){
            throw new NotFoundException("No free appointments available");
        }
        appointment.setStatus(AppointmentStatus.reserving);
        appointment.setPatient(addPatient(dto.getName() , dto.getPhoneNumber()));
        appointment = appointmentService.saveAppointment(appointment);
        DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
        response.setDigit(1);
        response.setStartTime(DateUtil.dateConvertor(appointment.getStartTime()));
        response.setEndTime(DateUtil.dateConvertor(appointment.getEndTime()));
        response.setPatientName(dto.getName());
        response.setPatientPhoneNumber(dto.getPhoneNumber());
        appointment.setStatus(AppointmentStatus.reserved);
        appointmentService.saveAppointment(appointment);
        response.setStatus(appointment.getStatus());
        return response;
    }


    public List<DoctorAppointmentViewResponse> findAppointmentByPatient(String phone){
        Optional<Patient> patient = patientRepository.findByPhoneNumber(phone);
        List<Appointment> appointments;
        if(patient.isPresent()){
            appointments = appointmentService.findAppointmentByPatientPhone(patient.get());
        }else{
            throw new NotFoundException("appointment Not found");
        }
        List<DoctorAppointmentViewResponse> responses = new ArrayList<>();
        if(appointments.size() != 0){
            for(Appointment appointment : appointments){
                responses.add(new DoctorAppointmentViewResponse(appointments.indexOf(appointment) + 1L , DateUtil.dateConvertor(appointment.getStartTime()) , DateUtil.dateConvertor(appointment.getEndTime()) , appointment.getStatus() , patient.get().getName() , patient.get().getPhoneNumber()));
            }
        }
        return responses;
    }


    public Patient addPatient(String name , String phone){
        Optional<Patient> byPhoneNumber = patientRepository.findByPhoneNumber(phone);
        return byPhoneNumber.orElseGet(() -> patientRepository.save(new Patient(name , phone)));
    }
}
