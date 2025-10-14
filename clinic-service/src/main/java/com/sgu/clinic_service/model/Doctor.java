package com.sgu.clinic_service.model;

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
    private UUID doctor_id;

    @Column(nullable = false)
    private UUID clinic_id;

    @Column(nullable = false)
    private String full_name;

    @Column(nullable = false)
    private UUID specialty_id;

    @Column(nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;
}
