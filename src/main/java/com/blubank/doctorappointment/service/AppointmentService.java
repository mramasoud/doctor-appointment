package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Service

public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    public void saveAppointment(List<Appointment> availableTimePeriods){
        appointmentRepository.saveAll(availableTimePeriods);
    }
    public Appointment saveAppointment(Appointment availableTimePeriod){
        return appointmentRepository.save(availableTimePeriod);
    }

    public List<Appointment> findAll(){
       return appointmentRepository.findAllByStatusOrderByAppointmentsId(AppointmentStatus.empty);
    }
    public List<Appointment> findAppointmentByPatientPhone(Patient patient){
        return appointmentRepository.findByPatientAndStatus(patient , AppointmentStatus.reserved);
    }

    public List<Appointment> findFreeAppointmentByDoctor(Doctor doctor , LocalDate day){
        return appointmentRepository.findByDoctorAndDayOfMonthOrderByAppointmentsId(doctor , day);
    }

    public List<Appointment> findEmptyAppointmentByDoctor(Doctor doctor , LocalDate day){
        return appointmentRepository.findByDoctorAndDayOfMonthAndStatusOrderByAppointmentsId(doctor , day , AppointmentStatus.empty);
    }
    Optional<Appointment> findAppointmentById(Long id){
        return appointmentRepository.findById(id);
    }
    @Transactional
    public Response deleteAppointment(Doctor doctor , int appointmentNumber , LocalDate day){
        List<Appointment> appointments = findFreeAppointmentByDoctor(doctor , day);
        if(appointments.isEmpty() || appointmentNumber < 1 || appointmentNumber > appointments.size()){
            return new Response(CodeProjectEnum.notFound.getCode() , messages.getString("appointmentNotFound"));
        }
        Appointment appointment = appointments.get(appointmentNumber - 1);
        if(appointment.getStatus() == AppointmentStatus.reserving){
            return new Response(CodeProjectEnum.appointmentReserved.getCode() ,messages.getString("appointmentReserved"));
        }
        if(appointment.getStatus() == AppointmentStatus.reserved){
            return new Response(CodeProjectEnum.appointmentReserved.getCode() , messages.getString("appointmentReserved"));
        }
        appointmentRepository.delete(appointment);
        return new Response(CodeProjectEnum.appointmentDeleted.getCode() , messages.getString("appointmentDeleted"));
    }
}
