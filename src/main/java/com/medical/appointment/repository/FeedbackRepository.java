package com.medical.appointment.repository;

import com.medical.appointment.model.Feedback;
import com.medical.appointment.model.enums.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    // All feedbacks for a doctor (list)
    List<Feedback> findByDoctorDoctorId(int doctorId);
    List<Feedback> findByPatientPatientId(int patientId);

    // Single feedbacks
    Optional<Feedback> findFirstByPatientPatientId(int patientId);
    Optional<Feedback> findFirstByDoctorDoctorId(int doctorId);

    // All feedbacks from a appointment
    List<Feedback> findByAppointmentAppointmentId(int appointmentId);

    // find the current status of feedback
    List<Feedback> findByStatus(FeedbackStatus status);

    // Check that patient was already gave feedback for a specific appointment
    boolean existsByPatientPatientIdAndAppointmentAppointmentId(int patientId, int appointmentId);
}