package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.DoctorDailyScheduleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DoctorControllerTest {



    DoctorController doctorController;
    PatientRepository patientRepository;
    AppointmentRepository appointmentRepository;
    DoctorRepository doctorRepository;
    CacheService cacheService;

    @Autowired
    public DoctorControllerTest( DoctorController doctorController, PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, CacheService cacheService) {

        this.doctorController = doctorController;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.cacheService = cacheService;
    }


    @BeforeEach
    void init() {
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        cacheService.clear();
        Doctor drShokohiFard = doctorRepository.save(new Doctor( "DrShokohiFard"));
        cacheService.PutToDoctorMap(1L, drShokohiFard.getName() + "," + drShokohiFard.getDoctorsId());
    }
    LocalDate date = LocalDate.now();
    @Test
    @Rollback
    @Transactional
    public void doctor_adds_open_times_end_date_before_start_date_error_shown() {
        ResponseEntity<DoctorDailyScheduleResponse> response = doctorController.doctorDailySchedule(new DoctorAvailabilityDTO(date,LocalTime.of(9, 0),LocalTime.of(7, 0)));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "endTime is before startTime.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctor_adds_open_times_period_less_than_30_minutes_no_time_added() {
        ResponseEntity<DoctorDailyScheduleResponse> response = doctorController.doctorDailySchedule(new DoctorAvailabilityDTO(date,LocalTime.of(9, 0),LocalTime.of(9, 20)));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("endTime is before startTime.", response.getBody().getMessage());
    }


    @Test
    @Rollback
    @Transactional
    public void doctor_can_view_30_minutes_appointments_no_appointments_empty_list_shown() {
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getFreeDoctorAppointmentTime(date);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }


    @Test
    @Rollback
    @Transactional
    public void doctor_can_view_30_minutes_appointments_some_appointments_patient_info_shown() {
        Patient patient = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor = cacheService.findDoctor(1L);
        appointmentRepository.save(new Appointment(LocalTime.of(8, 0), LocalTime.of(8, 30),date, AppointmentStatus.reserved, doctor));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30), date, AppointmentStatus.reserved, doctor, patient));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 30), LocalTime.of(10, 0),date, AppointmentStatus.reserved, doctor, patient));
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getFreeDoctorAppointmentTime(date);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).get(1).getPatientPhoneNumber(), patient.getPhoneNumber());
    }


    @Test
    @Rollback
    @Transactional
    public void doctor_can_delete_open_appointment_no_open_appointment_404_error_shown() {
        LocalDate day = date;
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1, day);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "Appointment Not Found.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctor_can_delete_open_appointment_appointment_taken_406_error_shown() {
        Patient amir = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor = cacheService.findDoctor(1L);
        appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30),date, AppointmentStatus.reserved, doctor, amir));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 30), LocalTime.of(10, 0),date, AppointmentStatus.reserved, doctor, amir));
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1,date);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "cannot delete appointment because appointment is reserved.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctor_can_delete_open_appointment_concurrency_check_doctor_deleting_same_appointment_404_error_shown() {
        Patient amir = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor = cacheService.findDoctor(1L);
         appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30), date, AppointmentStatus.reserving, doctor, amir));
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1,date);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "cannot delete appointment because appointment is reserved.");

    }
}