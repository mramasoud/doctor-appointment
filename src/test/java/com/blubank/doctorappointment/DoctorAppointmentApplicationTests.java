/*
package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.controller.PatientController;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.dto.PatientReserveAppointmentDTO;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoctorAppointmentApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    DoctorController doctorController;
    @Autowired
    PatientController patientController;


    @Before("testDoctorDailySchedule_EndDateBeforeStartDate_ErrorShown()")
    void init() {
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
    }

    @Test
    public void testDoctorDailySchedule_EndDateBeforeStartDate_ErrorShown() {
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setStartTime(LocalTime.of(9, 00));
        dto.setEndTime(LocalTime.of(7, 00));
        ResponseEntity<List<Response>> response = doctorController.doctorDailySchedule(dto);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals(response.getBody().get(0).getMessage(), "endTime is before startTime");
    }

    @Test
    public void testDoctorDailySchedule_PeriodLessThan30Minutes_NoTimeAdded() {
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setDoctorName("DrShokohiFard");
        dto.setDayOfMonth(1);
        dto.setStartTime(LocalTime.of(9, 00));
        dto.setEndTime(LocalTime.of(9, 20));//set the end time less than 30 minutes apart from the start time
        ResponseEntity<List<Response>> response = doctorController.doctorDailySchedule(dto);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(response.getBody().get(0).getMessage(), "A 30-minute time period was not found on a working day.");
    }

    @Test
    public void testGetEmptyAppointmentTime_NoAppointments_EmptyListShown() {
        String doctorName = "DrShokohiFard";
        int day = 1;  // Assuming the day is valid for the test
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getEmptyAppointmentTime(doctorName, day);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
    @BeforeEach
    public void addTimeAndAddPatientForRunTest(){
        doctorController.addNewDoctor(new DoctorDTO("DrHassani"));
        doctorController.doctorDailySchedule(new DoctorAvailabilityDTO("DrHassani",1,LocalTime.of(9,0),LocalTime.of(18,0)));
        patientController.reserveAppointmentForPatient(new PatientReserveAppointmentDTO("DrHassani",1,1,"amir","09376710620"));
    }

    @Test
    public void testGetEmptyAppointmentTime_SomeAppointments_PatientInfoShown() {
        String doctorName = "DrHassani";
        int day = 1;
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getEmptyAppointmentTime(doctorName, day);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody().get(0).getPatientPhoneNumber());
    }


    @Test
    public void testDeleteAppointment_NoOpenAppointment_404ErrorShown() {

        int number = 1;
        String name = "DrShokohiFard";
        int day = 1;
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mockMvc.perform(delete("/api/v1/doctor/delete/{number}/{name}/{day}", number, name, day))
                    .andExpect(status().isNotFound());
        });
    }
    @Test
    public void testDeleteAppointment_AppointmentTaken_406ErrorShown() {

        int number = 1;
        String name = "DrShokohiFard";
        int day = 1;
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mockMvc.perform(delete("/api/v1/doctor/delete/{number}/{name}/{day}", number, name, day))
                    .andExpect(status().isNotAcceptable());
        });
    }

    @Test
    public void testDeleteAppointment_ConcurrencyCheck_DoctorDeletingSameAppointment_404ErrorShown() {

        int number = 1;
        String name = "DrShokohiFard";
        int day = 1;
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mockMvc.perform(delete("/api/v1/doctor/delete/{number}/{name}/{day}", number, name, day))
                    .andExpect(status().isNotFound());
        });
    }
}
*/
