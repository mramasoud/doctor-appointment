package com.blubank.doctorappointment.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientReserveAppointmentDTO extends AbaseDto{
    private String doctorName;
    private int dayOfMonth;
    private int appointmentDigit;
    private String name;
    private String phoneNumber;

    public PatientReserveAppointmentDTO(String doctorName , int dayOfMonth , int appointmentDigit){
        this.doctorName = doctorName;
        this.dayOfMonth = dayOfMonth;
        this.appointmentDigit = appointmentDigit;
    }
}

