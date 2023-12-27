package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;

import javax.transaction.Transactional;
import java.util.List;

public interface PatientService{
    List<DoctorAppointmentViewResponse> showPatientFreeDoctorAppointments();

    List<DoctorAppointmentViewResponse> findAppointmentByPatient(String phone);

    @Transactional
    Appointment getAppointmentForPatient(PatientReservingAppointmentDTO dto);

    @Transactional
    Response unreserved(Long id);

    @Transactional
    void reservingAppointment(Long id);

    @Transactional
    DoctorAppointmentViewResponse reserveAppointment(FinalPatientReserveAppointmentDTO dto);

    @Transactional
    Patient addPatient(String name , String phone);
}
