package com.blubank.doctorappointment.model.dto.response;

import com.blubank.doctorappointment.model.ordinal.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorAppointmentViewResponse extends AbaseResponse{
    private long digit;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private AppointmentStatus status;
    private String patientName;
    private String patientPhoneNumber;



    public DoctorAppointmentViewResponse(long digit , LocalTime startTime , LocalTime endTime ,AppointmentStatus status, String patientName , String patientPhoneNumber){
        this.digit = digit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.patientName = patientName;
        this.patientPhoneNumber = patientPhoneNumber;
        this.status = status;
    }

    public DoctorAppointmentViewResponse(long digit , LocalTime startTime , LocalTime endTime,AppointmentStatus status){
        this.digit = digit;
        this.endTime = endTime;
        this.startTime = startTime;
        this.status = status;
    }

    public DoctorAppointmentViewResponse(){

    }
}
