package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.controller.PatientController;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.service.PatientService;
import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerTest{

    @Autowired
    private PatientController patientController;

    @Autowired
    PatientService patientService;
    @Autowired
    DoctorController doctorController;

    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");


    @Transactional
    @Rollback()
    @Test
    void patientsCanViewADoctorOpenAppointment(){
        doctorController.addNewDoctor(new DoctorDTO("akbari"));
        ResponseEntity<List<DoctorAppointmentViewResponse>> freeAppointment = patientController.getFreeAppointment();
        Assertions.assertEquals(HttpStatus.NOT_FOUND , freeAppointment.getStatusCode());
        Assertions.assertTrue(freeAppointment.getBody().isEmpty());
    }

    @Transactional
    @Rollback
    @Test
    void PatientsCanTakeAnOpenAppointment_PhoneNumberOrNameIsNotGiven(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        dto.setDayOfMonth(day);
        dto.setStartTime(LocalTime.of(12 , 0));
        dto.setEndTime(LocalTime.of(12 , 35));
        doctorController.doctorDailySchedule(dto);
        FinalPatientReserveAppointmentDTO reservd = new FinalPatientReserveAppointmentDTO(1L , null , null);
        Assertions.assertThrows(ValidationException.class , () -> patientController.reservedAppointmentForPatient(reservd));

    }


    @Transactional
    @Rollback
    @Test
    void PatientsCanTakeAnOpenAppointment_IfTheAppointmentIsAlreadyTakenOrDeleted_error(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        dto.setDayOfMonth(day);
        dto.setStartTime(LocalTime.of(9 , 0));
        dto.setEndTime(LocalTime.of(10 , 35));
        doctorController.doctorDailySchedule(dto);
        patientService.reservingAppointment(1L);
        Assertions.assertThrows(DuplicateRequestException.class , () -> patientService.reservingAppointment(1L) , "Appointment is reserved.");
    }


/*
    @Test
    void testGetAppointmentByPatient() {
        // Arrange
        String patientPhoneNumber = "1234567890";
        List<DoctorAppointmentViewResponse> appointmentByPatient = new ArrayList<>();
        DoctorAppointmentViewResponse appointment = new DoctorAppointmentViewResponse();
        appointment.setDoctorId(1L);
        appointment.setStartTime(LocalTime.now());
        appointment.setEndTime(LocalTime.now().plusMinutes(30));
        appointmentByPatient.add(appointment);
        when(patientService.findAppointmentByPatient(patientPhoneNumber)).thenReturn(appointmentByPatient);

        // Act
        ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> result = patientController.getAppointment(@PathVariable String phoneNumber);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(messages.getString("availableAppointments"), result.getBody().getMessage());
        assertEquals(appointmentByPatient, result.getBody().getBody());
    }

    @Test
    void testReservingAppointmentForPatient() {
        // Arrange
        PatientReservingAppointmentDTO dto = new PatientReservingAppointmentDTO();
        dto.setPatientPhoneNumber("1234567890");
        dto.setDoctorId(1L);
        dto.setAppointmentDate(LocalDate.now());
        dto.setAppointmentStartTime(LocalTime.now().minusMinutes(30));
        dto.setAppointmentEndTime(LocalTime.now().plusMinutes(30));

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setDoctorId(1L);
        appointment.setStartTime(dto.getAppointmentStartTime());
        appointment.setEndTime(dto.getAppointmentEndTime());
        appointment.setPatientId(1L);

        when(patientService.getAppointmentForPatient(dto)).thenReturn(appointment);

        // Act
        ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> result = patientController.reservingAppointmentForPatient(dto);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals(" appointmentNumber: " + appointment.getAppointmentsId() + "  " + appointment.getStartTime() + " - " + appointment.getEndTime() + "    " + messages.getString("confirmAppointmentTime"), result.getBody().getMessage());
    }

    @Test
    void testReservedAppointmentForPatient() {
        // Arrange
        FinalPatientReserveAppointmentDTO dto = new FinalPatientReserveAppointmentDTO();
        dto.setPatientPhoneNumber("1234567890");
        dto.setDoctorId(1L);
        dto.setAppointmentDate(LocalDate.now());
        dto.setAppointmentStartTime(LocalTime.now().minusMinutes(30));
        dto.setAppointmentEndTime(LocalTime.now().plusMinutes(30));

        DoctorAppointmentViewResponse appointment = new DoctorAppointmentViewResponse();
        appointment.setDoctorId(1L);
        appointment.setStartTime(dto.getAppointmentStartTime());
        appointment.setEndTime(dto.getAppointmentEndTime());
        appointment.setPatientId(1L);

        when(patientService.reserveAppointment(dto)).thenReturn(appointment);

        // Act
        ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> result = patientController.reservedAppointmentForPatient(dto);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals(messages.getString("reserveAppointment"), result.getBody().getMessage());
    }*/
}
