package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.service.PatientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController{
    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }


}