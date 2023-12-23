package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.AppointmentDTO;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController{
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @PostMapping("/time")
    public ResponseEntity<String> DoctorAvailabilityTime (@RequestBody DoctorAvailabilityDTO dto) {
        return doctorService.addDoctorAvailabilityTime(dto);
    } @PostMapping("/addDoctor")
    public ResponseEntity<String> addNewDoctor (@RequestBody DoctorDTO dto) {
        return doctorService.addDoctor(dto);
    }
}