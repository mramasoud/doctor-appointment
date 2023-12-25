package com.blubank.doctorappointment.dto;

import lombok.Data;

@Data
public class DoctorAppointmentViewDTO extends AbaseDto{

    private String DoctorName;
    private int dayOfMonth;

}
