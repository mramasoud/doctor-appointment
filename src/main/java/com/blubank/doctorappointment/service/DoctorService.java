package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.enumbration.AppointmentStatus;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private  DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;


    public ResponseEntity<String> addDoctor(DoctorDTO dto){
        Doctor doctor = doctorRepository.save(new Doctor(dto.getName()));
        if(doctor.getDoctors_Id()!=null){
            return new ResponseEntity<>("appointment has saved.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Time in day not valid, please enter time between 00:00 and 23:59", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<String> addDoctorAvailabilityTime(DoctorAvailabilityDTO dto) {
        if (dateTimeIsValid(dto.getStartTime(), dto.getEndTime())) {
            List<DoctorAvailabilityDTO> availableTimePeriods = getAvailableTimePeriods(dto.getDayOfMonth(),dto.getStartTime(), dto.getEndTime());
            System.out.println("time in activeTime is" + availableTimePeriods.size());
            if (availableTimePeriods.size() != 0) {
                try {
                    for (DoctorAvailabilityDTO time: availableTimePeriods) {
                        saveDoctorAvailability(time);
                    }
                    return new ResponseEntity<>("appointment has saved.", HttpStatus.OK);
                } catch (RuntimeException exception) {
                    return new ResponseEntity<>("Error server ", HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (Exception exception) {
                    return new ResponseEntity<>("Error saving availability", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Time in day not valid, please enter time between 00:00 and 23:59", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Time in day not valid, please enter time between 00:00 and 23:59", HttpStatus.BAD_REQUEST);
        }
    }
    private void saveDoctorAvailability(DoctorAvailabilityDTO dto) {
        appointmentRepository.save(dataProvider(dto));
    }
    public Appointment dataProvider(DoctorAvailabilityDTO dto) {
        Appointment appointment = new Appointment();
        ZonedDateTime startDateTime = ZonedDateTime.now(ZoneId.systemDefault()).with(dto.getStartTime());
        ZonedDateTime endDateTime = ZonedDateTime.now(ZoneId.systemDefault()).with(dto.getEndTime());
        appointment.setStartTime(Date.from(startDateTime.toInstant()));
        appointment.setEndTime(Date.from(endDateTime.toInstant()));
        appointment.setStatus(AppointmentStatus.empty);
        appointment.setDayOfMonth(dto.getDayOfMonth());
        appointment.setDoctor(doctorRepository.findByName(dto.getDoctorName()));
        return appointment;
    }
    public static int calculateAvailableSlots(LocalTime startTime, LocalTime endTime) {
       return (int) ChronoUnit.MINUTES.between(startTime, endTime)/30;
    }

    public static boolean dateTimeIsValid(LocalTime startTime, LocalTime endTime) {
        return startTime.isAfter(LocalTime.of(0, 0)) && endTime.isBefore(LocalTime.of(23, 59));
    }
    public static List<DoctorAvailabilityDTO> getAvailableTimePeriods(int dayOfMonth,LocalTime startTime, LocalTime endTime) {
        List<DoctorAvailabilityDTO> timePeriods = new ArrayList<>();
        LocalTime current = startTime;

        while (current.isBefore(endTime)) {

            LocalTime next = current.plusMinutes(30);
            if (next.isAfter(endTime) || next.equals(endTime)) {
               break;
            } else {
                timePeriods.add(new DoctorAvailabilityDTO(dayOfMonth,current,next));
            }
            current = next;
        }
        return timePeriods;
    }
}
