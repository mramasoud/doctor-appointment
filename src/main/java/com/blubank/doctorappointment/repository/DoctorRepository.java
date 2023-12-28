package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long>{


    Doctor findByName(String name);
}