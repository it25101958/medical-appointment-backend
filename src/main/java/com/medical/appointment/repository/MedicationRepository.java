package com.medical.appointment.repository;

import com.medical.appointment.model.Medication;
import com.medical.appointment.model.enums.MedicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    Optional<Medication> findByName(String name); // find by exact name

    List<Medication> findByNameContainingIgnoreCase(String name); // search by name

    List<Medication> findByGenericNameContainingIgnoreCase(String genericName); //search by generic name

    List<Medication> findByStatus(MedicationStatus status); // filter by status

    List<Medication> findByDosageForm(String dosageForm);  //find by dosage (tablet, syrups, injection)

    List<Medication> findByManufacturerContainingIgnoreCase(String manufacturer); // search by manufacturer's name

    boolean existsByName(String name); //check that medication still available or not
}