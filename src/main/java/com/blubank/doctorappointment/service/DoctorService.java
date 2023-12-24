package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.dto.DoctorAvailabilityDTO;
import com.blubank.doctorappointment.dto.DoctorDTO;
import com.blubank.doctorappointment.entity.Appointment;
import com.blubank.doctorappointment.entity.Doctor;
import com.blubank.doctorappointment.enumbration.AppointmentStatus;
import com.blubank.doctorappointment.enumbration.DoctorCodeProjectEnum;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
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
    private AppointmentRepository appointmentRepository;
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
            responses.add(new Response(DoctorCodeProjectEnum.appointmentSaved.getErrorCode() , availableTimePeriods.size()+DoctorCodeProjectEnum.appointmentSaved.getErrorDescription()));
            return responses;
        }catch(Exception exception){
            responses.add(new Response(DoctorCodeProjectEnum.serverError.getErrorCode() , DoctorCodeProjectEnum.serverError.getErrorDescription()));
            return responses;
        }
    }


    private void saveDoctorAvailableTime(List<Appointment> availableTimePeriods){
        appointmentRepository.saveAll(availableTimePeriods);
    }

    public static List<Appointment> getAvailableTimePeriods(int dayOfMonth , LocalTime startTime , LocalTime endTime , Doctor doctor){
        List<Appointment> timePeriods = new ArrayList<>();
        LocalTime current = startTime;

        while(current.isBefore(endTime)){
            LocalTime next = current.plusMinutes(timePeriods_Min);
            if(next.isAfter(endTime))
                break;
            timePeriods.add(new Appointment(DateUtil.dateConvertor(current) , DateUtil.dateConvertor(endTime) , dayOfMonth , AppointmentStatus.empty , doctor));
            current = next;
        }
        return timePeriods;
    }
}
