package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.service.AppointmentService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController{
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }


}