package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.ordinal.CodeProjectEnum;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.response.ResponseData;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ValidationService<DoctorAvailabilityDTO> doctorValidationService;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    @PostMapping("/workTime")
    public ResponseEntity<ResponseData<Response>> doctorDailySchedule(@RequestBody DoctorAvailabilityDTO dto) {
        List<Response> responses = new ArrayList<>();
        ResponseData<Response> responseData = new ResponseData<>();
        if (!doctorValidationService.validate(dto, responses)) {
            responseData.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            responseData.setBody(responses);
            responseData.setMessage("validation fail");
            return new ResponseEntity<>(responseData, responseData.getStatusCode());
        }
        Response response = doctorService.setDoctorDailyWorkSchedule(dto);
        if(response.getCode() == CodeProjectEnum.appointmentNotSaved.getCode()){
            responseData.setResult(response);
            responseData.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            responseData.setMessage(messages.getString("appointmentNotSaved"));
        }else if(response.getCode() == CodeProjectEnum.serverError.getCode()){
            responseData.setResult(response);
            responseData.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            responseData.setMessage(messages.getString("serverError"));
        }else if(response.getCode() == CodeProjectEnum.notFound.getCode()){
            responseData.setResult(response);
            responseData.setStatusCode(HttpStatus.NOT_FOUND);
            responseData.setMessage(messages.getString("doctorNotFound"));
        }else{
            responseData.setResult(response);
            responseData.setStatusCode(HttpStatus.OK);
            responseData.setMessage(messages.getString("appointmentSaved"));
        }
            return new ResponseEntity<>(responseData, responseData.getStatusCode());
    }

    @GetMapping("/appointments/{day}")
    public ResponseEntity<ResponseData<DoctorAppointmentViewResponse>> getEmptyAppointmentTime( @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        ResponseData<DoctorAppointmentViewResponse>  responseData =new ResponseData<>();
        List<DoctorAppointmentViewResponse> responses = doctorService.showDoctorFreeAppointments(day);
        if(responses.isEmpty()){
            responseData.setBody(responses);
            responseData.setStatusCode(HttpStatus.NOT_FOUND);
            responseData.setMessage(messages.getString("noAppointmentsFound"));
        }else {
            responseData.setBody(responses);
            responseData.setStatusCode(HttpStatus.OK);
            responseData.setMessage(messages.getString("availableAppointments"));
        }
        return new ResponseEntity<>(responseData, responseData.getStatusCode());

    }

    @PostMapping("/add")
    public ResponseEntity<Response> addNewDoctor(@RequestBody DoctorDTO dto) {
        return new ResponseEntity<>(doctorService.saveDoctor(dto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{number}/{day}")
    public ResponseEntity<ResponseData<Response>> deleteAppointment(@PathVariable int number, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate day) {
        ResponseData<Response>  responseData =new ResponseData<>();
        Response response = doctorService.deleteAppointmentByDoctor(number, day);
        if(response.getCode() == CodeProjectEnum.notFound.getCode()){
            responseData.setBody(null);
            responseData.setStatusCode(HttpStatus.NOT_FOUND);
            responseData.setMessage(messages.getString("noAppointmentsFound"));
        }else if(response.getCode() == CodeProjectEnum.appointmentReserved.getCode()){
            responseData.setBody(null);
            responseData.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            responseData.setMessage(messages.getString("appointmentReserved"));
        }else{
            responseData.setBody(null);
            responseData.setStatusCode(HttpStatus.OK);
            responseData.setMessage(messages.getString("appointmentDeleted"));
        }
        return new ResponseEntity<>(responseData, responseData.getStatusCode());
    }

}