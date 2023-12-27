package com.blubank.doctorappointment;

import com.blubank.doctorappointment.controller.DoctorController;
import com.blubank.doctorappointment.controller.PatientController;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.service.DoctorService;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorControllerTest{

    @Autowired
    private DoctorController doctorController;
    @Autowired
    private PatientController patientController;

    @Autowired
    private DoctorService doctorService;


    @Test
    @Rollback
    @Transactional
    public void doctorAddsOpenTimes_EndDateBeforeStartDate_ErrorShown(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setDayOfMonth(LocalDate.of(2023 , 12 , 27));
        dto.setStartTime(LocalTime.of(9 , 00));
        dto.setEndTime(LocalTime.of(7 , 00));
        ResponseEntity<DoctorDailyScheduleResponse> response = doctorController.doctorDailySchedule(dto);
        assertEquals(HttpStatus.NOT_ACCEPTABLE , response.getStatusCode());
        assertEquals(response.getBody().getMessage() , "endTime is before startTime.");
    }

    @Test
    @Rollback
    @Transactional
    public void doctorAddsOpenTimes_PeriodLessThan30Minutes_NoTimeAdded(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        dto.setDayOfMonth(LocalDate.of(2023 , 12 , 27));
        dto.setStartTime(LocalTime.of(9 , 00));
        dto.setEndTime(LocalTime.of(9 , 20));//set the end time less than 30 minutes apart from the start time
        ResponseEntity<DoctorDailyScheduleResponse> response = doctorController.doctorDailySchedule(dto);
        assertEquals(HttpStatus.NOT_ACCEPTABLE , response.getStatusCode());
        assertEquals(response.getBody().getMessage() , "A 30-minute time period was not found on a working day.");
    }


    @Test
    @Rollback
    @Transactional
    public void doctorCanView30MinutesAppointments_NoAppointments_EmptyListShown(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getEmptyAppointmentTime(day);
        assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }


    @Test
    @Rollback
    @Transactional
    public void doctorCanView30MinutesAppointments_SomeAppointments_PatientInfoShown(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        dto.setDayOfMonth(day);
        dto.setStartTime(LocalTime.of(9 , 0));
        dto.setEndTime(LocalTime.of(10 , 25));
        doctorController.doctorDailySchedule(dto);
        FinalPatientReserveAppointmentDTO reservd = new FinalPatientReserveAppointmentDTO(1L , "amir" , "09376710620");
        patientController.reservedAppointmentForPatient(reservd);
        ResponseEntity<List<DoctorAppointmentViewResponse>> response = doctorController.getEmptyAppointmentTime(day);
        assertEquals(HttpStatus.OK , response.getStatusCode());
        assertNotNull(response.getBody().get(0).getPatientPhoneNumber());
    }


    @Test
    @Rollback
    @Transactional
    public void testDeleteAppointment_NoOpenAppointment_404ErrorShown(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1 , day);
        assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());
        assertEquals(response.getBody().getMessage() , "Appointment Not Found.");
    }

    @Test
    @Rollback
    @Transactional
    public void testDeleteAppointment_AppointmentTaken_406ErrorShown(){
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        dto.setDayOfMonth(day);
        dto.setStartTime(LocalTime.of(9 , 0));
        dto.setEndTime(LocalTime.of(10 , 25));
        doctorController.doctorDailySchedule(dto);
        FinalPatientReserveAppointmentDTO reservd = new FinalPatientReserveAppointmentDTO(1L , "amir" , "09376710620");
        patientController.reservedAppointmentForPatient(reservd);
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1 , day);
        assertEquals(HttpStatus.NOT_ACCEPTABLE , response.getStatusCode());
        assertEquals(response.getBody().getMessage() , "cannot delete appointment because appointment is reserved.");
    }

    @Test
    @Rollback
    @Transactional
    public void testDeleteAppointment_ConcurrencyCheck_DoctorDeletingSameAppointment_404ErrorShown() {
        doctorController.addNewDoctor(new DoctorDTO("DrShokohiFard"));
        DoctorAvailabilityDTO dto = new DoctorAvailabilityDTO();
        LocalDate day = LocalDate.of(2023 , 12 , 27);
        dto.setDayOfMonth(day);
        dto.setStartTime(LocalTime.of(9 , 0));
        dto.setEndTime(LocalTime.of(10 , 25));
        doctorController.doctorDailySchedule(dto);
        PatientReservingAppointmentDTO reservingAppointmentDTO = new PatientReservingAppointmentDTO(day,1);
        patientController.reservingAppointmentForPatient(reservingAppointmentDTO);
        ResponseEntity<DeleteAppointmentResponse> response = doctorController.deleteAppointment(1 , day);
        assertEquals(HttpStatus.NOT_ACCEPTABLE , response.getStatusCode());
        assertEquals(response.getBody().getMessage() , "cannot delete appointment because appointment is reserved.");
    }
}