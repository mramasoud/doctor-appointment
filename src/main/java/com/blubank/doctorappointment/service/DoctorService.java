package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.DoctorAppointmentViewDTO;
import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.ordinal.AppointmentStatus;
import com.blubank.doctorappointment.ordinal.DoctorCodeProjectEnum;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.response.DoctorAppointmentViewResponse;
import com.blubank.doctorappointment.response.Response;
import com.blubank.doctorappointment.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService{
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    AppointmentService appointmentService;
    private static final int timePeriods_Min = 30;

    public ResponseEntity<String> addDoctor(DoctorDTO dto){
        Doctor doctor = doctorRepository.save(new Doctor(dto.getName()));
        if(doctor.getDoctors_Id() != null){
            return new ResponseEntity<>("appointment has saved." , HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Time in day not valid, please enter time between 00:00 and 23:59" , HttpStatus.BAD_REQUEST);
        }
    }

    public List<Response> addDoctorAvailableTimes(DoctorAvailabilityDTO dto , List<Response> responses){
        Doctor doctor = doctorRepository.findByName(dto.getDoctorName());
        if(doctor == null){
            responses.add(new Response(DoctorCodeProjectEnum.doctorNotFound.getErrorCode() , DoctorCodeProjectEnum.doctorNotFound.getErrorDescription()));
            return responses;
        }
        try{
            List<Appointment> availableTimePeriods = getAvailableTimePeriods(dto.getDayOfMonth() , dto.getStartTime() , dto.getEndTime() , doctor);
            if(availableTimePeriods.size() == 0){
                responses.add(new Response(DoctorCodeProjectEnum.appointmentNotSaved.getErrorCode() , DoctorCodeProjectEnum.appointmentNotSaved.getErrorDescription()));
                return responses;
            }
            saveDoctorAvailableTime(availableTimePeriods);
            responses.add(new Response(DoctorCodeProjectEnum.appointmentSaved.getErrorCode() , availableTimePeriods.size() + DoctorCodeProjectEnum.appointmentSaved.getErrorDescription()));
            return responses;
        }catch(Exception exception){
            responses.add(new Response(DoctorCodeProjectEnum.serverError.getErrorCode() , DoctorCodeProjectEnum.serverError.getErrorDescription()));
            return responses;
        }
    }

    public List<DoctorAppointmentViewResponse> showDoctorOpenAppointment(DoctorAppointmentViewDTO dto){
        Doctor doctor = doctorRepository.findByName(dto.getDoctorName());
        List<Appointment> appointmentByDoctor = appointmentService.findEmptyAppointmentByDoctor(doctor , dto.getDayOfMonth());
        List<DoctorAppointmentViewResponse> responses = new ArrayList<>();
        for(Appointment appointment : appointmentByDoctor){
            responses.add(new DoctorAppointmentViewResponse(appointment.getAppointments_Id() , DateUtil.dateConvertor(appointment.getStartTime()) , DateUtil.dateConvertor(appointment.getEndTime())));
        }
        return responses;
    }


    private void saveDoctorAvailableTime(List<Appointment> availableTimePeriods){
        appointmentService.saveAppointment(availableTimePeriods);
    }

    private List<Appointment> getAvailableTimePeriods(int dayOfMonth , LocalTime startTime , LocalTime endTime , Doctor doctor){
        List<Appointment> timePeriods = new ArrayList<>();
        LocalTime current = startTime;

        while(current.isBefore(endTime)){
            LocalTime next = current.plusMinutes(timePeriods_Min);
            if(next.isAfter(endTime))
                break;
            timePeriods.add(new Appointment(DateUtil.dateConvertor(current) , DateUtil.dateConvertor(next) , dayOfMonth , AppointmentStatus.empty , doctor));
            current = next;
        }
        return timePeriods;
    }
}
