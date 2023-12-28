package com.blubank.doctorappointment.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FinalPatientReserveAppointmentDTO extends AbaseDto{

    private Long appointmentDigit;
    private String name;
    private String phoneNumber;

}

