package com.blubank.doctorappointment.config;

import com.blubank.doctorappointment.dto.AbaseDto;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.service.AppointmentService;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.service.PatientService;
import com.blubank.doctorappointment.ui.ConsoleUI;
import com.blubank.doctorappointment.validation.DoctorValidationService;
import com.blubank.doctorappointment.validation.ValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public PatientService patientService() {
        return new PatientService();
    }

    @Bean
    public DoctorService doctorService() {
        return new DoctorService();
    }
    @Bean
    public AppointmentService appointmentService() {
        return new AppointmentService();
    }
    @Bean
    public ValidationService<DoctorAvailabilityDTO> validationService() {
        return new DoctorValidationService();
    }

    @Bean
    public ConsoleUI consoleUI() {
        return new ConsoleUI(patientService(),doctorService(),validationService());
    }
}