package com.blubank.doctorappointment.ui;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.dto.PatientReserveAppointmentDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.service.AppointmentService;
import com.blubank.doctorappointment.service.DoctorService;
import com.blubank.doctorappointment.service.PatientService;
import com.blubank.doctorappointment.util.DateUtil;
import com.blubank.doctorappointment.validation.ValidationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ConsoleUI{
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ValidationService<DoctorAvailabilityDTO> doctorValidationService;

    public ConsoleUI(PatientService patientService  , DoctorService doctorService , ValidationService<DoctorAvailabilityDTO> doctorValidationService){
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.doctorValidationService = doctorValidationService;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Hospital Management System");
        System.out.println("Are you a doctor or a patient? (1 for doctor, 2 for patient):");
        int userType = scanner.nextInt();
        if(userType == 1){
            handleDoctorActions(scanner);
        }else if(userType == 2){
            handlePatientActions(scanner);
        }
    }

    private void handleDoctorActions(Scanner scanner){
        do{
            // Doctor Interface
            System.out.println("Doctor Menu:");
            System.out.println("1. Add a New Doctor");
            System.out.println("2. Set Doctor Daily Work Schedule");
            System.out.println("3. View Doctor's Free Appointments");
            System.out.println("4. Delete Doctor's Appointment");
            System.out.println("Enter your choice: ");
            int doctorChoice = scanner.nextInt();
            switch(doctorChoice){

                case 1:
                    DoctorDTO dto = new DoctorDTO();
                    System.out.println("Enter doctor name:");
                    dto.setName(scanner.next());
                    Response response = doctorService.saveDoctor(dto);
                    System.out.println(response.getMessage());
                    break;
                case 2:
                    List<Response> responseList = new ArrayList<>();
                    DoctorAvailabilityDTO doctorAvailabilityDTO = new DoctorAvailabilityDTO();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    System.out.println("Enter doctor name:");
                    String doctorName = scanner.next();
                    doctorAvailabilityDTO.setDoctorName(doctorName);
                    System.out.println("Enter DayOfMonth:");
                    int dayOfMonth = scanner.nextInt();
                    doctorAvailabilityDTO.setDayOfMonth(dayOfMonth);
                    System.out.println("Enter daily StartTime Work:");
                    String start = scanner.next();
                    LocalTime startTime = LocalTime.parse(start , formatter);
                    doctorAvailabilityDTO.setStartTime(startTime);
                    System.out.println("Enter daily EndTime Work:");
                    String end = scanner.next();
                    LocalTime endTime = LocalTime.parse(end , formatter);
                    doctorAvailabilityDTO.setEndTime(endTime);
                    if(doctorValidationService.validate(doctorAvailabilityDTO , responseList)){
                        doctorService.setDoctorDailyWorkSchedule(doctorAvailabilityDTO , responseList);
                    }
                    for(Response res : responseList){
                        System.out.println(res.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Enter doctor name:");
                    String name = scanner.next();
                    System.out.println("Enter DayOfMonth:");
                    int day = scanner.nextInt();
                    List<DoctorAppointmentViewResponse> availableAppointments = doctorService.showDoctorFreeAppointments(name , day);
                    if(availableAppointments.isEmpty()){
                        System.out.println("No available appointments found for the specified doctor and day.");
                    }else{
                        System.out.println("Available appointments:");
                        for(DoctorAppointmentViewResponse appointment : availableAppointments){
                            System.out.println("Available appointment  Time is : " + DateUtil.dateConvertor(appointment.getStartTime()) +" endTime: "+ DateUtil.dateConvertor(appointment.getEndTime()) +" status: "+ appointment.getStatus()  );
                        }
                    }
                    break;
                case 4:
                    System.out.println("Enter doctor name:");
                    String docName = scanner.next();
                    System.out.println("Enter DayOfMonth:");
                    int dayNumber = scanner.nextInt();
                    System.out.println("Enter appointment digit:");
                    int digit = scanner.nextInt();
                    Response response1 = doctorService.deleteAppointmentByDoctor(digit , docName , dayNumber);
                    System.out.println(response1.getMessage());
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println("Do you want to perform another action? (1 for yes, 0 for no):");
            int continueChoice = scanner.nextInt();
            if(continueChoice == 0){
                break; // Exit the loop and go back to the main doctor menu
            }
        }while(true);
    }

    private void handlePatientActions(Scanner scanner){
        do{
            // Patient Interface
            System.out.println("Patient Menu:");
            System.out.println("1. View Doctor's Free Appointments");
            System.out.println("2. Reserve an Appointment");
            System.out.println("3. View Patient's Appointments");

            System.out.println("Enter your choice: ");
            int patientChoice = scanner.nextInt();

            switch(patientChoice){
                case 1:

                    System.out.println("Enter doctor name:");
                    String doctorName = scanner.next();
                    System.out.println("Enter dayOfMonth :");
                    int day = scanner.nextInt();
                    List<DoctorAppointmentViewResponse> availableAppointments = patientService.showDoctorFreeAppointments(doctorName, day);
                    if(availableAppointments.isEmpty()){
                        System.out.println("No available appointments found for the specified doctor and day.");
                    }else{
                        System.out.println("Available appointments:");
                        for(DoctorAppointmentViewResponse appointment : availableAppointments){
                            System.out.println("startTime : "+DateUtil.dateConvertor(appointment.getStartTime() )+" endTime: "+DateUtil.dateConvertor( appointment.getEndTime()) +" status: "+ appointment.getStatus()  );
                        }
                    }
                    break;

                case 2:
                    PatientReserveAppointmentDTO appointmentRequest = new PatientReserveAppointmentDTO();
                    System.out.println("Enter doctor name:");
                    String name = scanner.next();
                    appointmentRequest.setDoctorName(name);
                    System.out.println("Enter appointment date:");
                    int doctorPhoneNumber = scanner.nextInt();
                    appointmentRequest.setDayOfMonth(doctorPhoneNumber);
                    System.out.println("Enter appointment digit:");
                    int digit = scanner.nextInt();
                    appointmentRequest.setAppointmentDigit(digit);
                    Appointment reservingAppointmentForView = patientService.getReservingAppointmentForView(appointmentRequest);
                    System.out.println("if you appointment time is  : " + DateUtil.dateConvertor(reservingAppointmentForView.getStartTime()) +" endTime: "+ DateUtil.dateConvertor(reservingAppointmentForView.getEndTime()) +" status: "+ reservingAppointmentForView.getStatus());
                    System.out.println("Do you want to perform? (1 for yes, 0 for no):");
                    int continueChoice = scanner.nextInt();
                    if(continueChoice == 0){
                        break;
                    }
                    Long appointmentId = patientService.reservingAppointment(reservingAppointmentForView.getAppointmentsId());
                    System.out.println("Enter patient name:");
                    String patientName = scanner.next();
                    appointmentRequest.setName(patientName);
                    System.out.println("Enter patient phone number:");
                    String patientPhoneNumber = scanner.next();
                    appointmentRequest.setPhoneNumber(patientPhoneNumber);
                    DoctorAppointmentViewResponse response = patientService.reserveAppointment(appointmentRequest,appointmentId);
                    if(response == null){
                        System.out.println("Appointment reservation failed.");
                    }else{
                        System.out.println("your appointment reservation Time is success full and your appointment is : " + DateUtil.dateConvertor(response.getStartTime()) +" endTime: "+ DateUtil.dateConvertor(response.getEndTime())+" status: "+ response.getStatus()  );
                    }
                    break;
                case 3:
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
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println("Do you want to perform another action? (1 for yes, 0 for no):");
            int continueChoice = scanner.nextInt();
            if(continueChoice == 0){
                break; // Exit the loop and go back to the main doctor menu
            }
        }while(true);
    }
}
