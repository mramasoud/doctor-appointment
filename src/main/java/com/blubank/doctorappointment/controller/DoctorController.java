package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.model.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.model.dto.DoctorDTO;
import com.blubank.doctorappointment.model.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.model.dto.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.model.dto.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.model.dto.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.model.dto.response.Response;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.util.DateUtil;
import com.blubank.doctorappointment.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor/appointments")
public class DoctorController {
    @Autowired
    private DoctorService doctorServiceImpl;
    @Autowired
    private ValidationService<DoctorAvailabilityDTO,DoctorDailyScheduleResponse> doctorValidationService;

    @PostMapping("/time/schedule")
    public ResponseEntity<DoctorDailyScheduleResponse> doctorDailySchedule(@RequestBody DoctorAvailabilityDTO dto){
        DoctorDailyScheduleResponse doctorDailyScheduleResponse = new DoctorDailyScheduleResponse();
        if(doctorValidationService.validate(dto , doctorDailyScheduleResponse)){
            DoctorDailyScheduleResponse response = doctorServiceImpl.setDoctorDailyWorkSchedule(dto);
            if(response.getCode() == CodeProjectEnum.appointmentNotSaved.getCode())
                return new ResponseEntity<>(response , HttpStatus.NOT_ACCEPTABLE);
            if(response.getCode() == CodeProjectEnum.serverError.getCode())
                return new ResponseEntity<>(response , HttpStatus.INTERNAL_SERVER_ERROR);
            if(response.getCode() == CodeProjectEnum.notFound.getCode())
                return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(doctorDailyScheduleResponse , HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{day}")
    public ResponseEntity<List<DoctorAppointmentViewResponse>> getFreeDoctorAppointmentTime(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day){
        List<DoctorAppointmentViewResponse> responses;
        if(DateUtil.dayOfMonthValidation(day)){
            responses= doctorServiceImpl.showDoctorFreeAppointments(day);
            if(responses.isEmpty())
                return new ResponseEntity<>(responses , HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(responses , HttpStatus.OK);
        }
        return new ResponseEntity<>( new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/")
    public ResponseEntity<Response> addNewDoctor(@RequestBody DoctorDTO dto){
        return new ResponseEntity<>(doctorServiceImpl.saveDoctor(dto) , HttpStatus.OK);
    }

    @DeleteMapping("/{number}/{day}")
    public ResponseEntity<DeleteAppointmentResponse> deleteAppointment(@PathVariable int number , @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day){
        DeleteAppointmentResponse response;
        if(DateUtil.dayOfMonthValidation(day)){
             response = doctorServiceImpl.deleteAppointmentByDoctor(number , day);
            if(response.getCode() == CodeProjectEnum.notFound.getCode())
                return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
            if(response.getCode() == CodeProjectEnum.appointmentReserved.getCode())
                return new ResponseEntity<>(response , HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new DeleteAppointmentResponse(400,"date or number is not valid") , HttpStatus.BAD_REQUEST);
        }
    }

}