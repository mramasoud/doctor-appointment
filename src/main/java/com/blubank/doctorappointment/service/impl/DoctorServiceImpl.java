package com.blubank.doctorappointment.service.impl;

import com.blubank.doctorappointment.model.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.model.dto.DoctorDTO;
import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.entity.Doctor;
import com.blubank.doctorappointment.model.entity.Patient;
import com.blubank.doctorappointment.model.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.model.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.model.dto.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.model.dto.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.model.dto.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.model.dto.response.Response;
import com.blubank.doctorappointment.service.AppointmentService;
import com.blubank.doctorappointment.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    AppointmentService appointmentServiceImpl;
    @Autowired
    CacheService cacheService;

    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");
    private static final int timePeriods_Min = 30;

    @Override
    @Transactional
    public Response saveDoctor(DoctorDTO dto) {
        log.info("start add new doctor" + dto.getName());
        if (doctorRepository.findByName(dto.getName())!=null) {
            log.info("duplicate item." + dto.getName());
            return new Response(CodeProjectEnum.duplicate.getCode(), messages.getString("duplicate"));
        }
        Doctor doctor = doctorRepository.save(new Doctor(dto.getName()));
        log.info("doctor have been create." + dto.getName() +"doctor id: "+  doctor.getDoctorsId());
        if (doctor.getName() != null) {
            cacheService.PutToDoctorMap( 1L,doctor.getName()+","+doctor.getDoctorsId());
            return new Response(CodeProjectEnum.savedItem.getCode(), messages.getString("doctorSaved"));
        } else {
            log.info("can not save doctor." + dto.getName());
            return new Response(CodeProjectEnum.serverError.getCode(), messages.getString("serverError"));
        }
    }
    @Override
    @Transactional
    public DoctorDailyScheduleResponse setDoctorDailyWorkSchedule(DoctorAvailabilityDTO dto) {
        Doctor doctor = cacheService.findDoctor(1L);
        if (doctor.getName()==null) {
            return new DoctorDailyScheduleResponse(CodeProjectEnum.notFound.getCode() , messages.getString("doctorNotFound"));
        }
        log.info("start setDoctorDailyWorkSchedule :" +"doctor name : " + doctor.getName() + " date: "+dto.getDayOfMonth() +" start time : "+ dto.getStartTime() +" end time: "+ dto.getEndTime());
        if (appointmentServiceImpl.findFreeAppointmentByDoctor(doctor, dto.getDayOfMonth()).size() != 0) {
            log.info(" setDoctorDailyWorkSchedule fail because item duplicate :" +"doctor name : " + doctor.getName() + " date: "+dto.getDayOfMonth() +" start time : "+ dto.getStartTime() +" end time: "+ dto.getEndTime());
            return new DoctorDailyScheduleResponse(CodeProjectEnum.duplicate.getCode(), messages.getString("duplicateTime"));
        }
        try {
            List<Appointment> availableTimePeriods = getAvailableTimePeriods(dto.getDayOfMonth(), dto.getStartTime(), dto.getEndTime(), doctor);
            log.info(" setDoctorDailyWorkSchedule :" +"doctor name : " + doctor.getName() + " date: "+dto.getDayOfMonth() +" start time : "+ dto.getStartTime() +" end time: "+ dto.getEndTime() + " availableTimePeriods: " +availableTimePeriods.size());

            if (availableTimePeriods.size() == 0) {
                log.info("setDoctorDailyWorkSchedule fail because A 30-minute time period was not found on a working day. :" +"doctor name : " + doctor.getName() + " date: "+dto.getDayOfMonth() +" start time : "+ dto.getStartTime() +" end time: "+ dto.getEndTime() + " availableTimePeriods: " +availableTimePeriods.size());

                return new DoctorDailyScheduleResponse(CodeProjectEnum.appointmentNotSaved.getCode(), messages.getString("appointmentNotSaved"));

            }
            saveDoctorAvailableTime(availableTimePeriods);
            log.info(" setDoctorDailyWorkSchedule success full :" +"doctor name : " + doctor.getName() + " date: "+dto.getDayOfMonth() +" start time : "+ dto.getStartTime() +" end time: "+ dto.getEndTime() + " availableTimePeriods: " +availableTimePeriods.size());

            return new DoctorDailyScheduleResponse(CodeProjectEnum.savedItem.getCode(), availableTimePeriods.size() + " " + messages.getString("appointmentSaved"));

        } catch (Exception exception) {
            log.info(" setDoctorDailyWorkSchedule success full :" +"doctor name : " + doctor.getName() + " date: "+dto.getDayOfMonth() +" start time : "+ dto.getStartTime() +" end time: "+ dto.getEndTime());
            return new DoctorDailyScheduleResponse(CodeProjectEnum.serverError.getCode(), messages.getString("serverError"));
        }
    }
    @Override
    @Transactional
    public List<DoctorAppointmentViewResponse> showDoctorFreeAppointments(LocalDate day) {
        log.info("get free doctor appointment for : " + day);
        Doctor doctor = cacheService.findDoctor(1L);
        if (doctor.getName() == null) {
            return new ArrayList<>();
        }

        List<Appointment> appointments = appointmentServiceImpl.findFreeAppointmentByDoctor(doctor, day);
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
    @Override
    @Transactional
    public void saveDoctorAvailableTime(List<Appointment> availableTimePeriods) {
        appointmentServiceImpl.saveAppointment(availableTimePeriods);
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
    @Override
    @Transactional
    public DeleteAppointmentResponse deleteAppointmentByDoctor(int appointmentNumber , LocalDate day) {
        Doctor doctor = cacheService.findDoctor(1L);
        return appointmentServiceImpl.deleteAppointment(doctor, appointmentNumber, day);
    }

}
