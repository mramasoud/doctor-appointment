package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.response.Response;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface DoctorService{
    @Transactional
    Response saveDoctor(DoctorDTO dto);

    @Transactional
    DoctorDailyScheduleResponse setDoctorDailyWorkSchedule(DoctorAvailabilityDTO dto);

    List<DoctorAppointmentViewResponse> showDoctorFreeAppointments(LocalDate day);

    @Transactional
    void saveDoctorAvailableTime(List<Appointment> availableTimePeriods);

    @Transactional
    DeleteAppointmentResponse deleteAppointmentByDoctor(int appointmentNumber , LocalDate day);
}
