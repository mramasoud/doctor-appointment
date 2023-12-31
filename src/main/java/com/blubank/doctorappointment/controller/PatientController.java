package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.model.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.model.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.dto.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.model.dto.response.Response;
import com.blubank.doctorappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/v1/patient/appointments")
public class PatientController {

    @Autowired
    PatientService patientServiceImpl;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @GetMapping("/")
    public ResponseEntity<List<DoctorAppointmentViewResponse>> getFreeAppointment() {
        List<DoctorAppointmentViewResponse> doctorAppointmentViewResponse = patientServiceImpl.showPatientFreeDoctorAppointments();
        if (doctorAppointmentViewResponse.isEmpty()) {
            return new ResponseEntity<>(doctorAppointmentViewResponse, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(doctorAppointmentViewResponse,HttpStatus.OK);

        }

    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<List<DoctorAppointmentViewResponse>> getAppointment(@PathVariable String phoneNumber) {
        List<DoctorAppointmentViewResponse> appointmentByPatient = patientServiceImpl.findAppointmentByPatient(phoneNumber);
        if (appointmentByPatient.isEmpty()) {
            return new ResponseEntity<>(appointmentByPatient, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(appointmentByPatient, HttpStatus.OK);
        }
    }


    @PostMapping("/reserving")
        public ResponseEntity<String> reservingAppointmentForPatient(@RequestBody PatientReservingAppointmentDTO dto) {

        Appointment appointment = patientServiceImpl.reservingAppointmentForPatient(dto);
        if (appointment==null) {
            return new ResponseEntity<>("Appointment reservation failed.", HttpStatus.NOT_ACCEPTABLE);
        }else {
            return new ResponseEntity<>(" appointmentNumber: "+ appointment.getAppointmentsId() + " "  + appointment.getStartTime()+" - " + appointment.getEndTime()+ "    " +messages.getString("confirmAppointmentTime"), HttpStatus.OK);
        }
    }

    @PutMapping("/reserved")
    public ResponseEntity<DoctorAppointmentViewResponse> reservedAppointmentForPatient(@RequestBody FinalPatientReserveAppointmentDTO dto) {
        if(dto.getName() == null && dto.getPhoneNumber() == null){
            throw  new ValidationException("phone or number is not valid");
        }
        DoctorAppointmentViewResponse doctorAppointmentViewResponse = patientServiceImpl.reserveAppointment(dto);
        if (doctorAppointmentViewResponse==null) {
            return new ResponseEntity<>(doctorAppointmentViewResponse, HttpStatus.NOT_ACCEPTABLE);
        }else {
            return new ResponseEntity<>(doctorAppointmentViewResponse, HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/unReserved/{digit}")
    public ResponseEntity<String> unReservingAppointmentForPatient(@PathVariable Long digit) {

        Response unreserved = patientServiceImpl.unreserved(digit);
        if (unreserved.getMessage()=="success") {
            return new ResponseEntity<>("Appointment unReservation success.", HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>("Appointment unReservation fail.",HttpStatus.NOT_ACCEPTABLE);
        }
    }

}