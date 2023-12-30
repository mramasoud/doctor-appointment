package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.model.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.model.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.entity.Patient;
import com.blubank.doctorappointment.model.dto.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.model.dto.response.Response;

import javax.transaction.Transactional;
import java.util.List;

public interface PatientService{
    @Transactional
    List<DoctorAppointmentViewResponse> showPatientFreeDoctorAppointments();

    List<DoctorAppointmentViewResponse> findAppointmentByPatient(String phone);

    @Transactional
    Appointment reservingAppointmentForPatient(PatientReservingAppointmentDTO dto);

    @Transactional
    Response unreserved(Long id);

    @Transactional
    void reservingAppointment(Long id);

    @Transactional
    DoctorAppointmentViewResponse reserveAppointment(FinalPatientReserveAppointmentDTO dto);

    @Transactional
    Patient addPatient(String name , String phone);
}
