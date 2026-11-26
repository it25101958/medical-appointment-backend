package com.medical.appointment.service;

import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentStatusScheduler {

    private static final int DEFAULT_DURATION_MINUTES = 30;
    private static final List<AppointmentStatus> EXPIRABLE_STATUSES = List.of(
            AppointmentStatus.PENDING,
            AppointmentStatus.SCHEDULED
    );

    private final AppointmentRepository appointmentRepository;

    @Scheduled(fixedDelayString = "${appointments.status-update-interval-ms:60000}")
    @Transactional
    public void markExpiredAppointmentsAsNoShow() {
        LocalDateTime now = LocalDateTime.now();

        List<Appointment> candidates = appointmentRepository
                .findByStatusInAndAppointmentDateLessThanEqual(
                        EXPIRABLE_STATUSES,
                        now.toLocalDate()
                );

        List<Appointment> expiredAppointments = candidates.stream()
                .filter(appointment -> hasAppointmentEnded(appointment, now))
                .peek(appointment -> appointment.setStatus(AppointmentStatus.NO_SHOW))
                .toList();

        if (expiredAppointments.isEmpty()) {
            return;
        }

        appointmentRepository.saveAll(expiredAppointments);
        log.info("Marked {} expired appointment(s) as NO_SHOW", expiredAppointments.size());
    }

    private boolean hasAppointmentEnded(Appointment appointment, LocalDateTime now) {
        int durationMinutes = appointment.getDurationMinutes() != null
                ? appointment.getDurationMinutes()
                : DEFAULT_DURATION_MINUTES;

        LocalDateTime appointmentEnd = LocalDateTime.of(
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime()
        ).plusMinutes(durationMinutes);

        return !appointmentEnd.isAfter(now);
    }
}
