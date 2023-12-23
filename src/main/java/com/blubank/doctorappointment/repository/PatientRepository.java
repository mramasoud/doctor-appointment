package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>{
    // Implementation goes here
}