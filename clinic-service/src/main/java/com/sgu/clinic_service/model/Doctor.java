package com.sgu.clinic_service.model;

import com.sgu.clinic_service.constant.DoctorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "doctor_id")
    private UUID id;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "specialty_id", nullable = false)
    private UUID specialtyId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private DoctorStatus status = DoctorStatus.ACTIVE;
}
