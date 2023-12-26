package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.ResponseData;
import com.blubank.doctorappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/v1/patient/appointments")
public class PatientController {

    @Autowired
    PatientService patientService;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @GetMapping("/all")
    public ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> getFreeAppointment() {
        List<DoctorAppointmentViewResponse> doctorAppointmentViewResponse = patientService.showPatientFreeDoctorAppointments();
        ResponseData<DoctorAppointmentViewResponse>   responseData =new ResponseData<>();
        if (doctorAppointmentViewResponse.isEmpty()) {
            responseData.setBody(doctorAppointmentViewResponse);
            responseData.setStatusCode(HttpStatus.NOT_FOUND);
            responseData.setMessage(messages.getString("appointmentFreeNotFound"));
        }else{
            responseData.setBody(doctorAppointmentViewResponse);
            responseData.setStatusCode(HttpStatus.OK);
            responseData.setMessage(messages.getString("availableAppointments"));
        }
            return new ResponseEntity<>(responseData, responseData.getStatusCode());
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> getAppointment(@PathVariable String phoneNumber) {
        List<DoctorAppointmentViewResponse> appointmentByPatient = patientService.findAppointmentByPatient(phoneNumber);
        ResponseData<DoctorAppointmentViewResponse> responseData = new ResponseData<>();
        if (appointmentByPatient.isEmpty()) {
            responseData.setBody(appointmentByPatient);
            responseData.setStatusCode(HttpStatus.NOT_FOUND);
            responseData.setMessage(messages.getString("noAppointmentsFoundForPatient"));

        }else {
            responseData.setBody(appointmentByPatient);
            responseData.setStatusCode(HttpStatus.OK);
            responseData.setMessage(messages.getString("availableAppointments"));
        }
        return new ResponseEntity<>(responseData, responseData.getStatusCode());

    }


    @PostMapping("/reserve")
    public ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> reservingAppointmentForPatient(@RequestBody PatientReservingAppointmentDTO dto) {
        Appointment appointment = patientService.getAppointmentForPatient(dto);
        ResponseData<DoctorAppointmentViewResponse> responseData = new ResponseData<>();
        if (appointment==null) {
            responseData.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            responseData.setMessage(messages.getString("appointmentReservationFailed"));
        }else {
            responseData.setStatusCode(HttpStatus.ACCEPTED);
            responseData.setMessage(" appointmentNumber: "+ appointment.getAppointmentsId() + " "  + appointment.getStartTime()+" - " + appointment.getEndTime()+ "    " +messages.getString("confirmAppointmentTime"));
        }
        return new ResponseEntity<>(responseData, responseData.getStatusCode());
    }

    @PostMapping("/final")
    public ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> reservedAppointmentForPatient(@RequestBody FinalPatientReserveAppointmentDTO dto) {
        DoctorAppointmentViewResponse response = patientService.reserveAppointment(dto);
        ResponseData<DoctorAppointmentViewResponse> responseData = new ResponseData<>();
        if (response==null) {
            responseData.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            responseData.setMessage(messages.getString("appointmentReservationFailed"));
        }else {
            responseData.setStatusCode(HttpStatus.ACCEPTED);
            responseData.setMessage(messages.getString("reserveAppointment"));
        }
        return new ResponseEntity<>(responseData, responseData.getStatusCode());
    }
}