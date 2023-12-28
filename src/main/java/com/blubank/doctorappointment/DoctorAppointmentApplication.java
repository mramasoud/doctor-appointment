package com.blubank.doctorappointment;

import com.blubank.doctorappointment.ui.ConsoleUI;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAdminServer
@SpringBootApplication
@EnableScheduling
public class DoctorAppointmentApplication {
    @Autowired
    ConsoleUI console;

    public static void main(String[] args) {
        SpringApplication.run(DoctorAppointmentApplication.class, args);
    }

    @Scheduled(fixedDelay = 1L)
     void run() {
        console.run();
    }

}
