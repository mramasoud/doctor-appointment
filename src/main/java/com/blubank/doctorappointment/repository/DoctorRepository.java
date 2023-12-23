package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long>{


    Doctor findByName(String name);
}