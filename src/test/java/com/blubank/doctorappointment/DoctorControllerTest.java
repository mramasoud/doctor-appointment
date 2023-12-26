/*
package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.response.ResponseData;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.validation.DoctorValidationService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorControllerTest {

    @InjectMocks
    private DoctorController doctorController;

    @Mock
    private DoctorValidationService doctorValidationService;

    @Mock
    private DoctorService doctorService;

    @Test
    public void testDoctorDailySchedule_ValidationFail() {
        // Arrange
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        List<Response> responses = new ArrayList<>();
        responses.add(new Response("error", "Validation error"));
        when(doctorValidationService.validate(dto, responses)).thenReturn(false);

        // Act
        ResponseEntity<ResponseData<Response>> result = doctorController.doctorDailySchedule(dto);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
        assertEquals("validation fail", result.getBody().getMessage());
        assertEquals(responses, result.getBody().getBody());
    }

    @Test
    public void testDoctorDailySchedule_AppointmentNotSaved() {
        // Arrange
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        List<Response> responses = new ArrayList<>();
        when(doctorValidationService.validate(dto, responses)).thenReturn(true);
        when(doctorService.setDoctorDailyWorkSchedule(dto)).thenReturn(new Response("error", "Appointment not saved"));

        // Act
        ResponseEntity<ResponseData<Response>> result = doctorController.doctorDailySchedule(dto);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
        assertEquals("appointmentNotSaved", result.getBody().getMessage());
        assertEquals(null, result.getBody().getBody());
    }

    @Test
    public void testDoctorDailySchedule_AppointmentAlreadySaved() {
        // Arrange
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        List<Response> responses = new ArrayList<>();
        when(doctorValidationService.validate(dto, responses)).thenReturn(true);
        when(doctorService.setDoctorDailyWorkSchedule(dto)).thenReturn(new Response("success", "Appointment saved"));

        // Act
        ResponseEntity<ResponseData<Response>> result = doctorController.doctorDailySchedule(dto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("appointmentSaved", result.getBody().getMessage());
        assertNotNull(result.getBody().getBody());
    }

    @Test
    public void testGetEmptyAppointmentTime_NoAppointmentsFound() {
        // Arrange
        LocalDate day = LocalDate.now();

        // Act
        ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> result = doctorController.getEmptyAppointmentTime(day);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("noAppointmentsFound", result.getBody().getMessage());
        assertNull(result.getBody().getBody());
    }

    @Test
    public void testGetEmptyAppointmentTime_AppointmentsFound() {
        // Arrange
        List<DoctorAppointmentViewResponse> appointments = new ArrayList<>();
        appointments.add(new DoctorAppointmentViewResponse("doctor1", "patient1", LocalDate.now(), "startTime", "endTime"));

        when(doctorService.showDoctorFreeAppointments(any(LocalDate.class)).thenReturn(appointments);

        // Act
        ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> result = doctorController.getEmptyAppointmentTime(LocalDate.now());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("availableAppointments", result.getBody().getMessage());
        assertNotNull(result.getBody().getBody());
        assertEquals(1, result.getBody().getBody().size());
    }

    @Test
    public void testAddNewDoctor_Success() {
        // Arrange
        DoctorDTO dto = new DoctorDTO();

        // Act
        ResponseEntity<Response> result = doctorController.addNewDoctor(dto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void testAddNewDoctor_Failure() {
        // Arrange
        DoctorDTO dto = new DoctorDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("John.Doe@example.com");
        dto.setPassword("password");

        // Act
        ResponseEntity<Response> result = doctorController.addNewDoctor(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid request", result.getBody().getMessage());
        assertNull(result.getBody().getBody());
    }

    @Test
    public void testDeleteAppointment_AppointmentNotFound() {
        // Arrange
        int number = 1;
        LocalDate day = LocalDate.now();

        // Act
        ResponseEntity<ResponseData<Response>> result = doctorController.deleteAppointment(number, day);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("noAppointmentsFound", result.getBody().getMessage());
        assertNull(result.getBody().getBody());
    }

    @Test
    public void testDeleteAppointment_AppointmentFound() {
        // Arrange
        int number = 1;
        LocalDate day = LocalDate.now();
        DoctorAppointment doctorAppointment = new DoctorAppointment();
        doctorAppointment.setDoctorId(number);
        doctorAppointment.setDate(day);

        when(doctorService.deleteAppointmentByDoctor(number, day)).thenReturn(doctorAppointment*/
