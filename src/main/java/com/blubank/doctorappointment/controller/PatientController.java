package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.PatientReserveAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController{

    @Autowired
    PatientService patientService;

    @GetMapping("/appointments/view/{doctorName}/{day}")
    public ResponseEntity<List<DoctorAppointmentViewResponse>> getEmptyAppointmentTime(@PathVariable String doctorName , @PathVariable int day){
        return new ResponseEntity<>(patientService.showDoctorFreeAppointments(doctorName , day) , HttpStatus.ACCEPTED);

    }
    @GetMapping("/appointments/view/{phoneNumber}")
    public ResponseEntity<List<DoctorAppointmentViewResponse>> getAppointment(@PathVariable String phoneNumber ){
        return new ResponseEntity<>(patientService.findAppointmentByPatient(phoneNumber) , HttpStatus.ACCEPTED);
    }

    @PostMapping("/appointments/reserve")
    public ResponseEntity<DoctorAppointmentViewResponse> reserveAppointmentForPatient(@RequestBody PatientReserveAppointmentDTO dto){
            DoctorAppointmentViewResponse responseList = patientService.reserveAppointment(dto);
            return new ResponseEntity<>(responseList , HttpStatus.ACCEPTED);
    }
}