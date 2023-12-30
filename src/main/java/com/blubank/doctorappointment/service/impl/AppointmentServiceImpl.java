package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.entity.Doctor;
import com.blubank.doctorappointment.model.entity.Patient;
import com.blubank.doctorappointment.model.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.model.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.model.dto.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public void saveAppointment(List<Appointment> availableTimePeriods){
        appointmentRepository.saveAll(availableTimePeriods);
    }

    @Override
    @Transactional
    public Appointment saveAppointment(Appointment availableTimePeriod){
        return appointmentRepository.saveAndFlush(availableTimePeriod);
    }

    @Override
    public List<Appointment> findAll(){
        return appointmentRepository.findAllByStatusOrderByAppointmentsId(AppointmentStatus.empty);
    }

    @Override
    public List<Appointment> findAppointmentByPatientPhone(Patient patient){
        return appointmentRepository.findByPatientAndStatus(patient , AppointmentStatus.reserved);
    }

    @Override
    public List<Appointment> findFreeAppointmentByDoctor(Doctor doctor , LocalDate day){
        return appointmentRepository.findByDoctorAndDayOfMonthOrderByAppointmentsId(doctor , day);
    }

    @Override
    public List<Appointment> findEmptyAppointmentByDoctor(Doctor doctor , LocalDate day){
        return appointmentRepository.findByDoctorAndDayOfMonthAndStatusOrderByAppointmentsId(doctor , day , AppointmentStatus.empty);
    }

    @Override
    public Optional<Appointment> findAppointmentById(Long id){
        return appointmentRepository.findById(id);
    }

    @Override
    @Transactional
    public DeleteAppointmentResponse deleteAppointment(Doctor doctor , int appointmentNumber , LocalDate day){
        log.info("start deleteAppointment for : " + day +" appointmentNumber" +appointmentNumber + " doctor " + doctor.getName());
        List<Appointment> appointments = findFreeAppointmentByDoctor(doctor , day);
        if(appointments.isEmpty() || appointmentNumber < 1 || appointmentNumber > appointments.size()){
            log.info("deleteAppointment fail because appointment notFound for : " + day +" appointmentNumber" +appointmentNumber + " doctor " + doctor.getName());
            return new DeleteAppointmentResponse(CodeProjectEnum.notFound.getCode() , messages.getString("appointmentNotFound"));
        }
        Appointment appointment = appointments.get(appointmentNumber - 1);
        if(appointment.getStatus() == AppointmentStatus.reserving){
            log.info("deleteAppointment fail because appointment is reserving by other patient for : " + day +" appointmentNumber" +appointmentNumber + " doctor " + doctor.getName());
            return new DeleteAppointmentResponse(CodeProjectEnum.appointmentReserved.getCode() , messages.getString("appointmentReserved"));
        }
        if(appointment.getStatus() == AppointmentStatus.reserved){
            log.info("deleteAppointment fail because appointment is reserved by other patient for : " + day +" appointmentNumber" +appointmentNumber + " doctor " + doctor.getName());
            return new DeleteAppointmentResponse(CodeProjectEnum.appointmentReserved.getCode() , messages.getString("appointmentReserved"));
        }
        appointmentRepository.delete(appointment);
        log.info("deleteAppointment success for : " + day +" appointmentNumber" +appointmentNumber + " doctor " + doctor.getName());
        return new DeleteAppointmentResponse(CodeProjectEnum.appointmentDeleted.getCode() , messages.getString("appointmentDeleted"));
    }
}
