package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.controller.PatientController;
import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.entity.Patient;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.service.PatientService;
import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PatientControllerTest {


    PatientController patientController;
    PatientService patientService;
    DoctorController doctorController;
    PatientRepository patientRepository;
    AppointmentRepository appointmentRepository;
    DoctorRepository doctorRepository;

    @Autowired
    public PatientControllerTest(PatientController patientController, PatientService patientService, DoctorController doctorController, PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.patientController = patientController;
        this.patientService = patientService;
        this.doctorController = doctorController;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }
    LocalDate date = LocalDate.now();
    @BeforeEach
    void init() {
        appointmentRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.save(new Doctor("DrShokohiFard"));
    }

    @Test
    @Transactional
    void patients_can_view_a_doctor_open_appointment() {
        ResponseEntity<List<DoctorAppointmentViewResponse>> freeAppointment = patientController.getFreeAppointment();
        assertEquals(HttpStatus.NOT_FOUND, freeAppointment.getStatusCode());
        assertTrue(freeAppointment.getBody().isEmpty());
    }

    @Test
    @Transactional
    void patients_can_take_an_open_appointment_phone_number_or_name_is_not_given() {
        FinalPatientReserveAppointmentDTO reservd = new FinalPatientReserveAppointmentDTO(1L, null, null);
        Assertions.assertThrows(ValidationException.class, () -> patientController.reservedAppointmentForPatient(reservd));

    }


    @Test
    @Transactional
    void patients_can_take_an_open_appointment_if_the_appointment_is_already_taken_or_deleted_error() {
        Patient patient = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor=doctorRepository.findByName("DrShokohiFard");
        Appointment appointment = appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30),date, AppointmentStatus.reserved, doctor, patient));
        Assertions.assertThrows(DuplicateRequestException.class, () -> patientService.reservingAppointment(appointment.getAppointmentsId()), "Appointment is reserved.");
    }


    @Test
    @Transactional
    void patients_can_view_their_own_appointments_if_no_appointment_empty_list_should_be_shown() {
        ResponseEntity<List<DoctorAppointmentViewResponse>> result = patientController.getAppointment("09376710620");
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }


    @Test
    @Transactional
    void patients_can_view_their_own_appointments_if_more_than_one_appointment_list_should_be_shown() {
        Patient amir = patientRepository.save(new Patient("amir", "09376710620"));
        Doctor doctor=doctorRepository.findByName("DrShokohiFard");
        System.out.println(doctor.getDoctorsId());
        appointmentRepository.save(new Appointment(LocalTime.of(9, 0), LocalTime.of(9, 30),date, AppointmentStatus.reserved,doctor,amir));
        appointmentRepository.save(new Appointment(LocalTime.of(9, 30), LocalTime.of(10, 0),date, AppointmentStatus.reserved,doctor,amir));
        List<DoctorAppointmentViewResponse> doctorappointmentList = new ArrayList<>();
        doctorappointmentList.add(new DoctorAppointmentViewResponse(1L, LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.reserved, "amir", "09376710620"));
        ResponseEntity<List<DoctorAppointmentViewResponse>> result = patientController.getAppointment("09376710620");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
        assertEquals(result.getBody().get(0), doctorappointmentList.get(0));
    }


}


