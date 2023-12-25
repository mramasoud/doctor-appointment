package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.ui.ConsoleUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;


public class AppointmentService{


    @Autowired
    private AppointmentRepository appointmentRepository;



    public void saveAppointment(List<Appointment> availableTimePeriods){
        appointmentRepository.saveAll(availableTimePeriods);
    }

    public Appointment saveAppointment(Appointment availableTimePeriod){
        return appointmentRepository.save(availableTimePeriod);
    }

    public List<Appointment> findAppointmentByPatientPhone(Patient patient){
        return appointmentRepository.findByPatientAndStatus(patient , AppointmentStatus.reserved);
    }

    public List<Appointment> findFreeAppointmentByDoctor(Doctor doctor , int day){
        return appointmentRepository.findByDoctorAndDayOfMonthOrderByAppointmentsId(doctor , day);
    }

    public List<Appointment> findEmptyAppointmentByDoctor(Doctor doctor , int day){
        return appointmentRepository.findByDoctorAndDayOfMonthAndStatusOrderByAppointmentsId(doctor , day , AppointmentStatus.empty);
    }

    public Response deleteAppointment(Doctor doctor , int appointmentNumber , int day){
        List<Appointment> appointments = findFreeAppointmentByDoctor(doctor , day);
        if(appointments.isEmpty() || appointmentNumber < 1 || appointmentNumber > appointments.size()){
            return new Response(CodeProjectEnum.AppointmentNotFound.getErrorCode() , CodeProjectEnum.AppointmentNotFound.getErrorDescription());
        }
        Appointment appointment = appointments.get(appointmentNumber - 1);
        if(appointment.getStatus() == AppointmentStatus.reserving){
            return new Response(CodeProjectEnum.appointmentReserved.getErrorCode() , CodeProjectEnum.appointmentReserved.getErrorDescription());
        }
        if(appointment.getStatus() == AppointmentStatus.reserved){
            return new Response(CodeProjectEnum.appointmentReserved.getErrorCode() , CodeProjectEnum.appointmentReserved.getErrorDescription());
        }
        appointmentRepository.delete(appointment);
        return new Response(CodeProjectEnum.appointmentDeleted.getErrorCode() , CodeProjectEnum.appointmentDeleted.getErrorDescription());
    }
}
