package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.enumbration.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
    List<Appointment> findByDoctorAndDay(Doctor doctor , DayOfWeek day);

    List<Appointment> findByDoctorAndDayAndStatus(Doctor doctor , DayOfWeek day , AppointmentStatus status);
}