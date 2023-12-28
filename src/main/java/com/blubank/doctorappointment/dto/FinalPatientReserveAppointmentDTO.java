package com.blubank.doctorappointment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class FinalPatientReserveAppointmentDTO extends AbaseDto{

    private Long appointmentDigit;
    private String name;
    private String phoneNumber;

}

