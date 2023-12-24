package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.DoctorAppointmentViewDTO;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController{
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ValidationService<DoctorAvailabilityDTO> doctorValidationService;

    @PostMapping("/time")
    public ResponseEntity<List<Response>> DoctorAvailableTime(@RequestBody DoctorAvailabilityDTO dto){
        List<Response> responses = new ArrayList<>();
        if(! doctorValidationService.validate(dto , responses)){
            return new ResponseEntity<>(responses , HttpStatus.NOT_ACCEPTABLE);
        }else{
            List<Response> responseList = doctorService.addDoctorAvailableTimes(dto , responses);
            return new ResponseEntity<>(responseList , HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/appointmentView")
    public ResponseEntity<List<DoctorAppointmentViewResponse>> getEmptyAppointmentTime(@RequestBody DoctorAppointmentViewDTO dto){
        return new ResponseEntity<>(doctorService.showDoctorOpenAppointment(dto) , HttpStatus.ACCEPTED);
    }

    @PostMapping("/addDoctor")
    public ResponseEntity<Response> addNewDoctor(@RequestBody DoctorDTO dto){
        return new ResponseEntity<>(doctorService.addDoctor(dto) , HttpStatus.OK);
    }
}