package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByAppointmentId(UUID appointmentId);

    Page<Review> findByClinicId(UUID clinicId, Pageable pageable);

    Page<Review> findByPatientId(UUID patientId, Pageable pageable);

    @Query(value = """
            SELECT COALESCE(AVG(r.rating), 0)
            FROM reviews r
            WHERE r.clinic_id = :clinicId
            """, nativeQuery = true)
    Double getAverageRating(@Param("clinicId") UUID clinicId);
}
