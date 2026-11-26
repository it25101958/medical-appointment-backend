package com.medical.appointment.service;

import com.medical.appointment.model.Appointment;
import com.medical.appointment.model.enums.AppointmentStatus;
import com.medical.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentStatusSchedulerTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    void marksOnlyAppointmentsWhoseTimeWindowHasEndedAsNoShow() {
        Appointment expired = appointment(
                LocalDate.now().minusDays(1),
                LocalTime.NOON,
                AppointmentStatus.SCHEDULED
        );
        Appointment stillActive = appointment(
                LocalDate.now(),
                LocalTime.now().plusHours(1),
                AppointmentStatus.PENDING
        );

        when(appointmentRepository.findByStatusInAndAppointmentDateLessThanEqual(anyList(), any(LocalDate.class)))
                .thenReturn(List.of(expired, stillActive));

        new AppointmentStatusScheduler(appointmentRepository)
                .markExpiredAppointmentsAsNoShow();

        ArgumentCaptor<List<Appointment>> captor = ArgumentCaptor.forClass(List.class);
        verify(appointmentRepository).saveAll(captor.capture());

        assertEquals(List.of(expired), captor.getValue());
        assertEquals(AppointmentStatus.NO_SHOW, expired.getStatus());
        assertEquals(AppointmentStatus.PENDING, stillActive.getStatus());
    }

    @Test
    void doesNotSaveWhenNoAppointmentsHaveExpired() {
        Appointment future = appointment(
                LocalDate.now().plusDays(1),
                LocalTime.NOON,
                AppointmentStatus.SCHEDULED
        );

        when(appointmentRepository.findByStatusInAndAppointmentDateLessThanEqual(anyList(), any(LocalDate.class)))
                .thenReturn(List.of(future));

        new AppointmentStatusScheduler(appointmentRepository)
                .markExpiredAppointmentsAsNoShow();

        verify(appointmentRepository, never()).saveAll(any());
        assertEquals(AppointmentStatus.SCHEDULED, future.getStatus());
    }

    private Appointment appointment(
            LocalDate date,
            LocalTime time,
            AppointmentStatus status
    ) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setDurationMinutes(30);
        appointment.setStatus(status);
        return appointment;
    }
}
