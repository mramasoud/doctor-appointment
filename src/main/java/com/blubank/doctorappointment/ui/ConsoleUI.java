package com.blubank.doctorappointment.ui;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.dto.PatientReserveAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.service.PatientService;
import com.blubank.doctorappointment.util.DateUtil;
import com.blubank.doctorappointment.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

@Component
public class ConsoleUI {

    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ValidationService<DoctorAvailabilityDTO> doctorValidationService;
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
            System.out.println(messages.getString("addDoctor"));
            System.out.println(messages.getString("setDoctorSchedule"));
            System.out.println(messages.getString("viewDoctorAppointments"));
            System.out.println(messages.getString("deleteDoctorAppointment"));
            System.out.println(messages.getString("enterChoice"));
            int doctorChoice = scanner.nextInt();
            switch (doctorChoice) {
                case 1:
                    addDoctor(scanner);
                    break;
                case 2:
                    setDoctorSchedule(scanner);
                    break;
                case 3:
                    viewDoctorAppointments(scanner);
                    break;
                case 4:
                    deleteDoctorAppointment(scanner);
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
            System.out.println(messages.getString("viewDoctorFreeAppointments"));
            System.out.println(messages.getString("reserveAppointment"));
            System.out.println(messages.getString("viewPatientAppointments"));
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
        Response response = doctorService.saveDoctor(dto);
        System.out.println(response.getMessage());
        System.out.println(messages.getString("setDoctorWorkTime"));
        int reserveChoice = scanner.nextInt();
        if (reserveChoice == 1) {
            setDoctorSchedule(dto.getName(),scanner);
        }

    }
    private void setDoctorSchedule(Scanner scanner) {
        List<Response> responseList = new ArrayList<>();
        DoctorAvailabilityDTO doctorAvailabilityDTO = new DoctorAvailabilityDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println(messages.getString("enterDoctorName"));
        String doctorName = scanner.next();
        doctorAvailabilityDTO.setDoctorName(doctorName);
        System.out.println(messages.getString("enterDayOfMonth"));
        int dayOfMonth = scanner.nextInt();
        doctorAvailabilityDTO.setDayOfMonth(dayOfMonth);
        System.out.println(messages.getString("enterStartTime"));
        String start = scanner.next();
        LocalTime startTime = LocalTime.parse(start, formatter);
        doctorAvailabilityDTO.setStartTime(startTime);
        System.out.println(messages.getString("enterEndTime"));
        String end = scanner.next();
        LocalTime endTime = LocalTime.parse(end, formatter);
        doctorAvailabilityDTO.setEndTime(endTime);
        if (doctorValidationService.validate(doctorAvailabilityDTO, responseList)) {
            doctorService.setDoctorDailyWorkSchedule(doctorAvailabilityDTO, responseList);
        }
        for (Response res : responseList) {
            System.out.println(res.getMessage());
        }
    }
    private void setDoctorSchedule( String doctorName,Scanner scanner) {
        List<Response> responseList = new ArrayList<>();
        DoctorAvailabilityDTO doctorAvailabilityDTO = new DoctorAvailabilityDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        doctorAvailabilityDTO.setDoctorName(doctorName);
        System.out.println(messages.getString("dayOfMonthPrompt"));
        int dayOfMonth = scanner.nextInt();
        doctorAvailabilityDTO.setDayOfMonth(dayOfMonth);
        System.out.println(messages.getString("enterStartTime"));
        String start = scanner.next();
        LocalTime startTime = LocalTime.parse(start, formatter);
        doctorAvailabilityDTO.setStartTime(startTime);
        System.out.println(messages.getString("enterEndTime"));
        String end = scanner.next();
        LocalTime endTime = LocalTime.parse(end, formatter);
        doctorAvailabilityDTO.setEndTime(endTime);
        if (doctorValidationService.validate(doctorAvailabilityDTO, responseList)) {
            doctorService.setDoctorDailyWorkSchedule(doctorAvailabilityDTO, responseList);
        }
        for (Response res : responseList) {
            System.out.println(res.getMessage());
        }
    }
    private void viewDoctorAppointments(Scanner scanner) {
        System.out.println(messages.getString("enterDoctorName"));
        String name = scanner.next();
        System.out.println(messages.getString("enterDayOfMonth"));
        int day = scanner.nextInt();
        List<DoctorAppointmentViewResponse> availableAppointments = doctorService.showDoctorFreeAppointments(name, day);
        if (availableAppointments.isEmpty()) {
            System.out.println(messages.getString("noAppointmentsFound"));
        } else {
            System.out.println(messages.getString("availableAppointments"));
            for (DoctorAppointmentViewResponse appointment : availableAppointments) {
                System.out.println(messages.getString("appointmentStartTime") + appointment.getStartTime() + messages.getString("appointmentEndTime") + appointment.getEndTime() + messages.getString("appointmentStatus") + appointment.getStatus());
            }
        }
    }
    private void deleteDoctorAppointment(Scanner scanner) {
        System.out.println(messages.getString("enterDoctorName"));
        String docName = scanner.next();
        System.out.println(messages.getString("enterDayOfMonth"));
        int dayNumber = scanner.nextInt();
        System.out.println(messages.getString("enterAppointmentDigit"));
        int digit = scanner.nextInt();
        Response response1 = doctorService.deleteAppointmentByDoctor(digit, docName, dayNumber);
        System.out.println(response1.getMessage());
    }
    private void viewDoctorFreeAppointments(Scanner scanner) {
        System.out.println(messages.getString("doctorNamePrompt"));
        String doctorName = scanner.next();
        System.out.println(messages.getString("dayOfMonthPrompt"));
        int day = scanner.nextInt();
        List<DoctorAppointmentViewResponse> availableAppointments = patientService.showDoctorFreeAppointments(doctorName, day);
        if (availableAppointments.isEmpty()) {
            System.out.println(messages.getString("noAppointmentsFound"));
        } else {
            System.out.println(messages.getString("availableAppointments"));
            for (DoctorAppointmentViewResponse appointment : availableAppointments) {
                System.out.println(messages.getString("startTime") + " " + DateUtil.dateConvertor(appointment.getStartTime()) + " " + messages.getString("endTime") + " " + DateUtil.dateConvertor(appointment.getEndTime()) + " " + messages.getString("status") + " " + appointment.getStatus());
            }
        }
        System.out.println(messages.getString("reserveAppointmentPrompt"));
        int reserveChoice = scanner.nextInt();
        if (reserveChoice == 1) {
            reserveAppointment(doctorName, day, scanner);
        }
    }
    private void viewPatientAppointments(Scanner scanner) {
        System.out.println("Enter your phoneNumber:");
        String phoneNumber = scanner.next();
        List<DoctorAppointmentViewResponse> patientAppointments = patientService.findAppointmentByPatient(phoneNumber);
        if(patientAppointments.isEmpty()){
            System.out.println("No appointments found for the specified patient.");
        }else{
            System.out.println("your appointment  Time is :");
            for(DoctorAppointmentViewResponse appointment : patientAppointments){
                System.out.println("startTime : "+DateUtil.dateConvertor(appointment.getStartTime() )+" endTime: "+DateUtil.dateConvertor( appointment.getEndTime()) +" status: "+ appointment.getStatus()  );
            }
        }
    }
    private void reserveAppointment(Scanner scanner) {
        PatientReserveAppointmentDTO appointmentRequest = new PatientReserveAppointmentDTO();
        System.out.println(messages.getString("doctorNamePrompt"));
        String name = scanner.next();
        appointmentRequest.setDoctorName(name);
        System.out.println(messages.getString("dayOfMonthPrompt"));
        int dayofmonth = scanner.nextInt();
        appointmentRequest.setDayOfMonth(dayofmonth);
        System.out.println(messages.getString("enterAppointmentDigit"));
        int digit = scanner.nextInt();
        appointmentRequest.setAppointmentDigit(digit);
        Appointment reservingAppointmentForView = patientService.getReservingAppointmentForView(appointmentRequest);
        System.out.println(messages.getString("confirmAppointmentTime") + " " + DateUtil.dateConvertor(reservingAppointmentForView.getStartTime()) + " " + messages.getString("endTime") + " " + DateUtil.dateConvertor(reservingAppointmentForView.getEndTime()) + " " + messages.getString("status") + " " + reservingAppointmentForView.getStatus());
        System.out.println(messages.getString("confirmAppointment"));
        int continueChoice = scanner.nextInt();
        if (continueChoice == 0) {
            return;
        }
        Long appointmentId = patientService.reservingAppointment(reservingAppointmentForView.getAppointmentsId());
        System.out.println(messages.getString("patientNamePrompt"));
        String patientName = scanner.next();
        appointmentRequest.setName(patientName);
        System.out.println(messages.getString("patientPhoneNumberPrompt"));
        String patientPhoneNumber = scanner.next();
        appointmentRequest.setPhoneNumber(patientPhoneNumber);
        DoctorAppointmentViewResponse response = patientService.reserveAppointment(appointmentRequest, appointmentId);
        if (response == null) {
            System.out.println(messages.getString("appointmentReservationFailed"));
        } else {
            System.out.println(messages.getString("appointmentReservationSuccess") + " " + DateUtil.dateConvertor(response.getStartTime()) + " " + messages.getString("endTime") + " " + DateUtil.dateConvertor(response.getEndTime()) + " " + messages.getString("status") + " " + response.getStatus());
        }
        return;
    }
    private  void reserveAppointment(String doctorName,int day,Scanner scanner) {
        PatientReserveAppointmentDTO appointmentRequest = new PatientReserveAppointmentDTO();
        appointmentRequest.setDoctorName(doctorName);
        appointmentRequest.setDayOfMonth(day);
        System.out.println(messages.getString("enterAppointmentDigit"));
        int digit = scanner.nextInt();
        appointmentRequest.setAppointmentDigit(digit);
        Appointment reservingAppointmentForView = patientService.getReservingAppointmentForView(appointmentRequest);
        System.out.println(messages.getString("confirmAppointmentTime") + " " + DateUtil.dateConvertor(reservingAppointmentForView.getStartTime()) + " " + messages.getString("endTime") + " " + DateUtil.dateConvertor(reservingAppointmentForView.getEndTime()) + " " + messages.getString("status") + " " + reservingAppointmentForView.getStatus());
        System.out.println(messages.getString("confirmAppointment"));
        int continueChoice = scanner.nextInt();
        if (continueChoice == 0) {
            return;
        }
        Long appointmentId = patientService.reservingAppointment(reservingAppointmentForView.getAppointmentsId());
        System.out.println(messages.getString("patientNamePrompt"));
        String patientName = scanner.next();
        appointmentRequest.setName(patientName);
        System.out.println(messages.getString("patientPhoneNumberPrompt"));
        String patientPhoneNumber = scanner.next();
        appointmentRequest.setPhoneNumber(patientPhoneNumber);
        DoctorAppointmentViewResponse response = patientService.reserveAppointment(appointmentRequest, appointmentId);
        if (response == null) {
            System.out.println(messages.getString("appointmentReservationFailed"));
        } else {
            System.out.println(messages.getString("appointmentReservationSuccess") + " " + DateUtil.dateConvertor(response.getStartTime()) + " " + messages.getString("endTime") + " " + DateUtil.dateConvertor(response.getEndTime()) + " " + messages.getString("status") + " " + response.getStatus());
        }
        return;
    }
    private boolean performAnotherAction(Scanner scanner) {
        System.out.println(messages.getString("yesOrNo"));
        int continueChoice = scanner.nextInt();
        return continueChoice != 0;
    }
}
