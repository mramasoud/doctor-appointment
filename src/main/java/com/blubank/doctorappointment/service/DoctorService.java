package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.CacheService;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    CacheService cacheService;

    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");
    private static final int timePeriods_Min = 30;
    @Transactional
    public Response saveDoctor(DoctorDTO dto) {
        if (cacheService.findDoctor(1L).getName()!= null) {
            return new Response(CodeProjectEnum.duplicate.getErrorCode(), messages.getString("duplicate"));
        }
        Doctor doctor = doctorRepository.save(new Doctor(dto.getName()));
        if (doctor.getDoctorsId() != null) {
            cacheService.PutToDoctorMap( doctor.getDoctorsId(),doctor.getName());
            return new Response(CodeProjectEnum.doctorSaved.getErrorCode(), messages.getString("doctorSaved"));
        } else {
            return new Response(CodeProjectEnum.serverError.getErrorCode(), messages.getString("serverError"));
        }
    }
    @Transactional
    public Response setDoctorDailyWorkSchedule(DoctorAvailabilityDTO dto) {
        Response  response;
        Doctor doctor = cacheService.findDoctor(1L);
        if (doctor.getDoctorsId() == null) {
            return new Response(CodeProjectEnum.serverError.getErrorCode(), messages.getString("doctorNotFound"));

        }
        if (appointmentService.findFreeAppointmentByDoctor(doctor, dto.getDayOfMonth()).size() != 0) {
            return new Response(CodeProjectEnum.serverError.getErrorCode(), messages.getString("duplicateTime"));

        }
        try {
            List<Appointment> availableTimePeriods = getAvailableTimePeriods(dto.getDayOfMonth(), dto.getStartTime(), dto.getEndTime(), doctor);
            if (availableTimePeriods.size() == 0) {
                return new Response(CodeProjectEnum.appointmentNotSaved.getErrorCode(), messages.getString("appointmentNotSaved"));

            }
            saveDoctorAvailableTime(availableTimePeriods);
            return new Response(CodeProjectEnum.appointmentSaved.getErrorCode(), availableTimePeriods.size() + messages.getString("appointmentSaved"));

        } catch (Exception exception) {
            return new Response(CodeProjectEnum.serverError.getErrorCode(), messages.getString("serverError"));

        }
    }
    public List<DoctorAppointmentViewResponse> showDoctorFreeAppointments(LocalDate day) {
        Doctor doctor = cacheService.findDoctor(1L);
        if (doctor.getDoctorsId() == null) {
            return new ArrayList<>();
        }

        List<Appointment> appointments = appointmentService.findFreeAppointmentByDoctor(doctor, day);
        return appointments.stream()
                .map(appointment -> {
                    DoctorAppointmentViewResponse response = new DoctorAppointmentViewResponse();
                    response.setDigit(appointments.indexOf(appointment) + 1L);
                    response.setStartTime(appointment.getStartTime());
                    response.setEndTime(appointment.getEndTime());
                    response.setStatus(appointment.getStatus());
                    Optional<Patient> patient = Optional.ofNullable(appointment.getPatient());
                    patient.ifPresent(p -> {
                        response.setPatientName(p.getName());
                        response.setPatientPhoneNumber(p.getPhoneNumber());
                    });
                    return response;
                })
                .collect(Collectors.toList());
    }
    @Transactional
     void saveDoctorAvailableTime(List<Appointment> availableTimePeriods) {
        appointmentService.saveAppointment(availableTimePeriods);
    }

    private List<Appointment> getAvailableTimePeriods(LocalDate dayOfMonth, LocalTime startTime, LocalTime endTime, Doctor doctor) {
        List<Appointment> timePeriods = new ArrayList<>();
        LocalTime current = startTime;

        while (current.isBefore(endTime)) {
            LocalTime next = current.plusMinutes(timePeriods_Min);
            if (next.isAfter(endTime))
                break;
            timePeriods.add(new Appointment(current, next, dayOfMonth, AppointmentStatus.empty, doctor));
            current = next;
        }
        return timePeriods;
    }
    @Transactional
    public Response deleteAppointmentByDoctor(int appointmentNumber, LocalDate day) {
        Doctor doctor = cacheService.findDoctor(1L);
        return appointmentService.deleteAppointment(doctor, appointmentNumber, day);
    }

}
