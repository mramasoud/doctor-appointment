package com.blubank.doctorappointment.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientReservingAppointmentDTO extends AbaseDto{

    private String doctorName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dayOfMonth;
    private int appointmentDigit;

    public PatientReservingAppointmentDTO(LocalDate dayOfMonth, int appointmentDigit) {
        this.dayOfMonth = dayOfMonth;
        this.appointmentDigit = appointmentDigit;
    }
}

