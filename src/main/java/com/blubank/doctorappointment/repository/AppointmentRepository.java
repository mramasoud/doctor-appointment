package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
    List<Appointment> findByDoctorAndDayOfMonthOrderByAppointmentsId(Doctor doctor , int day);

    List<Appointment> findByDoctorAndDayOfMonthAndStatusOrderByAppointmentsId(Doctor doctor , int day , AppointmentStatus status);

    List<Appointment> findByPatientAndStatus(Patient patient , AppointmentStatus status);
}