package com.sgu.appointment_service.repository;

import com.sgu.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {
    // Tìm lịch hẹn bị trùng của Doctor
    @Query(value = """
            SELECT * FROM appointments a
            WHERE a.doctor_id = :doctorId
                AND a.status NOT IN ('CANCELLED', 'COMPLETED')
                AND a.start_time < :endTime
                AND a.end_time > :startTime
                AND (:appointmentId IS NULL OR a.appointment_id <> :appointmentId)
            LIMIT 1
            """, nativeQuery = true)
    Optional<Appointment> findConflictingAppointmentByDoctor(
            @Param("doctorId") UUID doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("appointmentId") UUID appointmentId
    );

    @Query("""
            SELECT a FROM Appointment a
            WHERE a.doctorId = :doctorId
                AND FUNCTION('DATE', a.startTime) = :date
                AND a.status IN ('PENDING', 'CONFIRMED')
            """)
    List<Appointment> findAppointmentsByDoctorAndDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date
    );
}
