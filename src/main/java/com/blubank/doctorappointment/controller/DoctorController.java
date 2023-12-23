package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.mapper.DoctorMapper;
import com.blubank.doctorappointment.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
public class DoctorController{
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id){
        return new ResponseEntity<>(doctorService.getDoctorById(id) , HttpStatus.OK);
    }
}