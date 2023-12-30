package com.blubank.doctorappointment.console;

import com.blubank.doctorappointment.service.impl.CacheService;
import com.blubank.doctorappointment.model.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.model.dto.DoctorDTO;
import com.blubank.doctorappointment.model.dto.FinalPatientReserveAppointmentDTO;
import com.blubank.doctorappointment.model.dto.PatientReservingAppointmentDTO;
import com.blubank.doctorappointment.model.entity.Appointment;
import com.blubank.doctorappointment.model.dto.response.DeleteAppointmentResponse;
import com.blubank.doctorappointment.model.dto.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.model.dto.response.DoctorDailyScheduleResponse;
import com.blubank.doctorappointment.model.dto.response.Response;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.service.PatientService;
import com.blubank.doctorappointment.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

@Component
public class Console {

    @Autowired
    private PatientService patientServiceImpl;
    @Autowired
    private DoctorService doctorServiceImpl;
    @Autowired
    private ValidationService<DoctorAvailabilityDTO, DoctorDailyScheduleResponse> doctorValidationService;
    @Autowired
    private CacheService cacheService;
    ResourceBundle messages = ResourceBundle.getBundle("HospitalMessages");

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(messages.getString("welcome"));
        System.out.println(messages.getString("userPrompt"));
        int userType = scanner.nextInt();
        if (userType == 1) {
            handleDoctorActions(scanner);
        } else if (userType == 2) {
            handlePatientActions(scanner);
        }

    }

    private void handleDoctorActions(Scanner scanner) {
        do {
            // Doctor Interface
            System.out.println(messages.getString("doctorMenu"));
            System.out.println("1. " + messages.getString("setDoctorSchedule"));
            System.out.println("2. " + messages.getString("viewDoctorAppointments"));
            System.out.println("3. " + messages.getString("deleteDoctorAppointment"));
            System.out.println("4. " + "add doctor");

            System.out.println(messages.getString("enterChoice"));
            int doctorChoice = scanner.nextInt();
            switch (doctorChoice) {
                case 1:
                    setDoctorSchedule(scanner);
                    break;
                case 2:
                    viewDoctorAppointments(scanner);
                    break;
                case 3:
                    deleteDoctorAppointment(scanner);
                    break;
                case 4:
                    addDoctor(scanner);
                    break;
                default:
                    System.out.println(messages.getString("invalidChoice"));
            }
        } while (performAnotherAction(scanner));
    }

    private void handlePatientActions(Scanner scanner) {
        do {
            // Patient Interface
            System.out.println(messages.getString("patientMenu"));
            System.out.println("1. " + messages.getString("viewDoctorFreeAppointments"));
            System.out.println("2. " + messages.getString("reserveAppointment"));
            System.out.println("3. " + messages.getString("viewPatientAppointments"));
            System.out.println(messages.getString("enterChoice"));
            int patientChoice = scanner.nextInt();
            switch (patientChoice) {
                case 1:
                    viewDoctorFreeAppointments(scanner);
                    break;
                case 2:
                    reserveAppointment(scanner);
                    break;
                case 3:
                    viewPatientAppointments(scanner);
                    break;
                default:
                    System.out.println(messages.getString("invalidChoice"));
            }
        } while (performAnotherAction(scanner));
    }

    private void addDoctor(Scanner scanner) {
        DoctorDTO dto = new DoctorDTO();
        System.out.println(messages.getString("enterDoctorName"));
        dto.setName(scanner.next());
        Response response = doctorServiceImpl.saveDoctor(dto);
        System.out.println(response.getMessage());
        System.out.println(messages.getString("setDoctorWorkTime"));
        int reserveChoice = scanner.nextInt();
        if (reserveChoice == 1) {
            setDoctorSchedule(dto.getName(), scanner);
        }

    }

    private void setDoctorSchedule(Scanner scanner) {
        DoctorDailyScheduleResponse doctorDailyScheduleResponse = new DoctorDailyScheduleResponse();
        DoctorAvailabilityDTO doctorAvailabilityDTO = new DoctorAvailabilityDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println(messages.getString("enterDayOfMonth"));
        String dayOfMonth = scanner.next();
        doctorAvailabilityDTO.setDayOfMonth(LocalDate.parse(dayOfMonth));
        System.out.println(messages.getString("enterStartTime"));
        String start = scanner.next();
        LocalTime startTime = LocalTime.parse(start, formatter);
        doctorAvailabilityDTO.setStartTime(startTime);
        System.out.println(messages.getString("enterEndTime"));
        String end = scanner.next();
        LocalTime endTime = LocalTime.parse(end, formatter);
        doctorAvailabilityDTO.setEndTime(endTime);
        if (doctorValidationService.validate(doctorAvailabilityDTO, doctorDailyScheduleResponse)) {
            DoctorDailyScheduleResponse doctorDailySchedule = doctorServiceImpl.setDoctorDailyWorkSchedule(doctorAvailabilityDTO);
            System.out.println(doctorDailySchedule.getMessage());
        } else {
            System.out.println(doctorDailyScheduleResponse.getMessage());
        }
    }

    private void setDoctorSchedule(String doctorName, Scanner scanner) {
        DoctorDailyScheduleResponse doctorDailyScheduleResponse = new DoctorDailyScheduleResponse();
        DoctorAvailabilityDTO doctorAvailabilityDTO = new DoctorAvailabilityDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        doctorAvailabilityDTO.setDoctorName(doctorName);
        System.out.println(messages.getString("dayOfMonthPrompt"));
        String dayOfMonth = scanner.next();
        doctorAvailabilityDTO.setDayOfMonth(LocalDate.parse(dayOfMonth));
        System.out.println(messages.getString("enterStartTime"));
        String start = scanner.next();
        LocalTime startTime = LocalTime.parse(start, formatter);
        doctorAvailabilityDTO.setStartTime(startTime);
        System.out.println(messages.getString("enterEndTime"));
        String end = scanner.next();
        LocalTime endTime = LocalTime.parse(end, formatter);
        doctorAvailabilityDTO.setEndTime(endTime);
        if (doctorValidationService.validate(doctorAvailabilityDTO, doctorDailyScheduleResponse)) {
            DoctorDailyScheduleResponse doctorDailySchedule = doctorServiceImpl.setDoctorDailyWorkSchedule(doctorAvailabilityDTO);
            System.out.println(doctorDailySchedule.getMessage());
        } else {
            System.out.println(doctorDailyScheduleResponse.getMessage());
        }
    }

    private void viewDoctorAppointments(Scanner scanner) {
        System.out.println(messages.getString("enterDayOfMonth"));
        String day = scanner.next();
        List<DoctorAppointmentViewResponse> availableAppointments = doctorServiceImpl.showDoctorFreeAppointments(LocalDate.parse(day));
        if (availableAppointments.isEmpty()) {
            System.out.println(messages.getString("noAppointmentsFound"));
            System.out.println(availableAppointments);
        } else {
            System.out.println(messages.getString("availableAppointments"));
            for (DoctorAppointmentViewResponse appointment : availableAppointments) {
                if (appointment.getPatientName() != null) {
                    System.out.println(messages.getString("appointmentStartTime") + " " + appointment.getStartTime() + " " + messages.getString("appointmentEndTime") + appointment.getEndTime() + messages.getString("appointmentStatus") + " " + appointment.getStatus() + " " + "patient name : " + appointment.getPatientName() + " patient phoneNumber : " + appointment.getPatientPhoneNumber());
                } else {
                    System.out.println(messages.getString("appointmentStartTime") + " " + appointment.getStartTime() + " " + messages.getString("appointmentEndTime") + appointment.getEndTime() + messages.getString("appointmentStatus") + " " + appointment.getStatus());
                }

            }
        }
    }

    private void deleteDoctorAppointment(Scanner scanner) {
        System.out.println(messages.getString("enterDayOfMonth"));
        String dayNumber = scanner.next();
        System.out.println(messages.getString("enterAppointmentDigit"));
        int digit = scanner.nextInt();
        DeleteAppointmentResponse response1 = doctorServiceImpl.deleteAppointmentByDoctor(digit, LocalDate.parse(dayNumber));
        System.out.println(response1.getMessage());
    }

    private void viewDoctorFreeAppointments(Scanner scanner) {
        List<DoctorAppointmentViewResponse> availableAppointments = patientServiceImpl.showPatientFreeDoctorAppointments();
        if (availableAppointments.isEmpty()) {
            System.out.println(messages.getString("noAppointmentsFound"));
        } else {
            System.out.println(messages.getString("availableAppointments"));
            for (DoctorAppointmentViewResponse appointment : availableAppointments) {
                System.out.println("appointment digit: " + appointment.getDigit() + " " + messages.getString("startTime") + " " + appointment.getStartTime() + " " + messages.getString("endTime") + " " + appointment.getEndTime() + " " + messages.getString("status") + " " + appointment.getStatus());
            }
        }
        System.out.println(messages.getString("reserveAppointmentPrompt"));
        int reserveChoice = scanner.nextInt();
        if (reserveChoice == 1) {
            reserveAppointment(scanner);
        }
    }

    private void viewPatientAppointments(Scanner scanner) {
        System.out.println("Enter your phoneNumber:");
        String phoneNumber = scanner.next();
        List<DoctorAppointmentViewResponse> patientAppointments = patientServiceImpl.findAppointmentByPatient(phoneNumber);
        if (patientAppointments.isEmpty()) {
            System.out.println("No appointments found for the specified patient.");
        } else {
            System.out.println("your appointment  Time is :");
            for (DoctorAppointmentViewResponse appointment : patientAppointments) {
                System.out.println("startTime : " + appointment.getStartTime() + " endTime: " + appointment.getEndTime() + " status: " + appointment.getStatus());
            }
        }
    }

    private void reserveAppointment(Scanner scanner) {
        PatientReservingAppointmentDTO appointmentRequest = new PatientReservingAppointmentDTO();
        System.out.println(messages.getString("dayOfMonthPrompt"));
        String dayofmonth = scanner.next();
        appointmentRequest.setDayOfMonth(LocalDate.parse(dayofmonth));
        System.out.println(messages.getString("enterAppointmentDigit"));
        int digit = scanner.nextInt();
        appointmentRequest.setAppointmentDigit(digit);
        Appointment reservingAppointmentForView = patientServiceImpl.reservingAppointmentForPatient(appointmentRequest);
        System.out.println(messages.getString("confirmAppointmentTime") + " " + reservingAppointmentForView.getStartTime() + " " + messages.getString("endTime") + " " + reservingAppointmentForView.getEndTime() + " " + messages.getString("status") + " " + reservingAppointmentForView.getStatus());
        System.out.println(messages.getString("confirmAppointment"));
        int continueChoice = scanner.nextInt();
        if (continueChoice == 0) {
            patientServiceImpl.unreserved(reservingAppointmentForView.getAppointmentsId());
        }
        FinalPatientReserveAppointmentDTO finalPatient = new FinalPatientReserveAppointmentDTO();

        System.out.println(messages.getString("patientNamePrompt"));
        String patientName = scanner.next();
        finalPatient.setName(patientName);
        System.out.println(messages.getString("patientPhoneNumberPrompt"));
        String patientPhoneNumber = scanner.next();
        finalPatient.setPhoneNumber(patientPhoneNumber);
        finalPatient.setAppointmentDigit(reservingAppointmentForView.getAppointmentsId());
        DoctorAppointmentViewResponse response = patientServiceImpl.reserveAppointment(finalPatient);
        if (response == null) {
            System.out.println(messages.getString("appointmentReservationFailed"));
        } else {
            System.out.println(messages.getString("appointmentReservationSuccess") + " " + response.getStartTime() + " " + messages.getString("endTime") + " " + response.getEndTime() + " " + messages.getString("status") + " " + response.getStatus());
        }
    }

    private boolean performAnotherAction(Scanner scanner) {
        System.out.println(messages.getString("yesOrNo"));
        int continueChoice = scanner.nextInt();
        return continueChoice != 0;
    }
}
