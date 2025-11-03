package com.sgu.clinic_service.repository;

import com.sgu.clinic_service.model.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
    // SELECT * FROM clinics WHERE LOWER(clinic_name) LIKE LOWER('%clinic%')
    Page<Clinic> findByClinicNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Clinic> findByUserId(UUID userId);

    @Query(
            value = """
                    SELECT *
                    FROM (
                        SELECT *,
                            (6371 * ACOS(
                                COS(RADIANS(:latitude)) * COS(RADIANS(latitude)) *
                                COS(RADIANS(longitude) - RADIANS(:longitude)) +
                                SIN(RADIANS(:latitude)) * SIN(RADIANS(latitude))
                            )) AS distance_km
                        FROM clinics
                        WHERE (:name IS NULL OR LOWER(clinic_name) LIKE LOWER(CONCAT('%', :name, '%')))
                    ) AS sub
                    WHERE distance_km <= :radius
                    ORDER BY distance_km, sub.clinic_name ASC
                    """,
            countQuery = """
                    SELECT COUNT(*) FROM (
                        SELECT *,
                            (6371 * ACOS(
                                COS(RADIANS(:latitude)) * COS(RADIANS(latitude)) *
                                COS(RADIANS(longitude) - RADIANS(:longitude)) +
                                SIN(RADIANS(:latitude)) * SIN(RADIANS(latitude))
                            )) AS distance_km
                        FROM clinics
                        WHERE (:name IS NULL OR LOWER(clinic_name) LIKE LOWER(CONCAT('%', :name, '%')))
                    ) AS sub
                    WHERE distance_km <= :radius
                    """,
            nativeQuery = true
    )
    Page<Clinic> findByNameAndNearby(
            @Param("name") String name,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius,
            Pageable pageable
    );
}
