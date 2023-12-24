package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService{

    @Autowired
    private AppointmentRepository appointmentRepository;


    public void saveAppointment(List<Appointment> availableTimePeriods){
        appointmentRepository.saveAll(availableTimePeriods);
    }

    public void saveAppointment(Appointment availableTimePeriod){
        appointmentRepository.save(availableTimePeriod);
    }


    public List<Appointment> findEmptyAppointmentByDoctor(Doctor doctor,int day){
        return appointmentRepository.findByDoctorAndStatusAndDayOfMonth(doctor, AppointmentStatus.empty,day);
    }
}
