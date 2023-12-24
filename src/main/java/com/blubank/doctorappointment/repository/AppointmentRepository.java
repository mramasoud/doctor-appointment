package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.Doc;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
    List<Appointment> findByDoctorAndStatusNotAndDayOfMonthOrderByAppointmentsId(Doctor doctor, AppointmentStatus status,int day);

}