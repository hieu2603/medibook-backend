package com.sgu.appointment_service.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sgu.appointment_service.enums.AppointmentStatus;
import com.sgu.appointment_service.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

        @Query("""
                        SELECT a FROM Appointment a
                        WHERE (:patientId IS NULL OR a.patient_id = :patientId)
                          AND (:doctorId IS NULL OR a.doctor_id = :doctorId)
                          AND (:clinicId IS NULL OR a.clinic_id = :clinicId)
                          AND (:status IS NULL OR a.status = :status)
                          AND (a.start_time >= COALESCE(:startFrom, a.start_time))
                          AND (a.start_time <= COALESCE(:startTo, a.start_time))
                          AND (a.end_time   >= COALESCE(:endFrom,  a.end_time))
                          AND (a.end_time   <= COALESCE(:endTo,    a.end_time))
                        """)
        Page<Appointment> search(
                        @Param("patientId") UUID patientId,
                        @Param("doctorId") UUID doctorId,
                        @Param("clinicId") UUID clinicId,
                        @Param("status") AppointmentStatus status,
                        @Param("startFrom") LocalDateTime startFrom,
                        @Param("startTo") LocalDateTime startTo,
                        @Param("endFrom") LocalDateTime endFrom,
                        @Param("endTo") LocalDateTime endTo,
                        Pageable pageable);

        @Query("""
                        SELECT COUNT(a) > 0 FROM Appointment a
                        WHERE a.status = 'CONFIRMED'
                          AND a.clinic_id = :clinicId
                          AND (a.doctor_id = COALESCE(:doctorId, a.doctor_id))
                          AND (
                          (:startTime < a.end_time)
                          AND (:endTime > a.start_time)
                          )
                        """)
        boolean existsOverlappingConfirmed(
                        @Param("clinicId") UUID clinicId,
                        @Param("doctorId") UUID doctorId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);
}
