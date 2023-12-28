package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.controller.PatientController;
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
import com.blubank.doctorappointment.service.PatientService;
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


    PatientController patientController;
    PatientService patientService;
    DoctorController doctorController;
    PatientRepository patientRepository;
    AppointmentRepository appointmentRepository;
    DoctorRepository doctorRepository;
    CacheService cacheService;

    @Autowired
    public DoctorControllerTest(PatientController patientController, PatientService patientService, DoctorController doctorController, PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, CacheService cacheService) {
        this.patientController = patientController;
        this.patientService = patientService;
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

    @Test
    @Rollback
    @Transactional
    public void doctorAddsOpenTimes_EndDateBeforeStartDate_ErrorShown() {
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setDayOfMonth(LocalDate.of(2023, 12, 28));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(7, 0));
        ResponseEntity<DoctorDailyScheduleResponse> response = doctorController.doctorDailySchedule(dto);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "endTime is before startTime.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctorAddsOpenTimes_PeriodLessThan30Minutes_NoTimeAdded() {
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setDayOfMonth(LocalDate.of(2023, 12, 28));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(9, 20));//set the end time less than 30 minutes apart from the start time
        ResponseEntity<DoctorDailyScheduleResponse> response = doctorController.doctorDailySchedule(dto);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("endTime is before startTime.", response.getBody().getMessage());
    }


    @Test
    @Rollback
    @Transactional
    public void doctorCanView30MinutesAppointments_NoAppointments_EmptyListShown() {
        LocalDate day = LocalDate.of(2023, 12, 28);
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getFreeDoctorAppointmentTime(day);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }


    @Test
    @Rollback
    @Transactional
    public void doctorCanView30MinutesAppointments_SomeAppointments_PatientInfoShown() {
        Patient patient = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor = cacheService.findDoctor(1L);
        appointmentRepository.save(new Appointment(LocalTime.of(8, 0), LocalTime.of(8, 30), LocalDate.of(2023, 12, 29), AppointmentStatus.reserved, doctor));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30), LocalDate.of(2023, 12, 29), AppointmentStatus.reserved, doctor, patient));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 30), LocalTime.of(10, 0), LocalDate.of(2023, 12, 29), AppointmentStatus.reserved, doctor, patient));
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getFreeDoctorAppointmentTime(LocalDate.of(2023, 12, 29));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).get(1).getPatientPhoneNumber(), patient.getPhoneNumber());
    }


    @Test
    @Rollback
    @Transactional
    public void doctorCanDeleteOpenAppointment_NoOpenAppointment_404ErrorShown() {
        LocalDate day = LocalDate.of(2023, 12, 28);
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1, day);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "Appointment Not Found.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctorCanDeleteOpenAppointment_AppointmentTaken_406ErrorShown() {
        Patient amir = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor = cacheService.findDoctor(1L);
        appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30), LocalDate.of(2023, 12, 29), AppointmentStatus.reserved, doctor, amir));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 30), LocalTime.of(10, 0), LocalDate.of(2023, 12, 29), AppointmentStatus.reserved, doctor, amir));
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1, LocalDate.of(2023, 12, 29));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "cannot delete appointment because appointment is reserved.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctorCanDeleteOpenAppointment_ConcurrencyCheck_DoctorDeletingSameAppointment_404ErrorShown() {
        Patient amir = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor = cacheService.findDoctor(1L);
        Appointment appointment = appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30), LocalDate.of(2023, 12, 29), AppointmentStatus.reserving, doctor, amir));
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1, LocalDate.of(2023, 12, 29));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "cannot delete appointment because appointment is reserved.");

    }
}