/*
package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.PatientController;
import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.ResponseData;
import com.blubank.doctorappointment.service.PatientService;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @Test
    void testGetFreeAppointment() {
        // Arrange
        List<DoctorAppointmentViewResponse> doctorAppointmentViewResponses = new ArrayList<>();
        DoctorAppointmentViewResponse appointment = new DoctorAppointmentViewResponse();
        appointment.setDoctorId(1L);
        appointment.setStartTime(LocalTime.now());
        appointment.setEndTime(LocalTime.now().plusMinutes(30));
        doctorAppointmentViewResponses.add(appointment);
        when(patientService.showPatientFreeDoctorAppointments()).thenReturn(doctorAppointmentViewResponses);

        // Act
        ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> result = patientController.getFreeAppointment();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(messages.getString("availableAppointments"), result.getBody().getMessage());
        assertEquals(doctorAppointmentViewResponses, result.getBody().getBody());
    }

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
    }
}
*/
