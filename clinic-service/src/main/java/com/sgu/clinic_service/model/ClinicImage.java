package com.sgu.clinic_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "clinic_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID img_id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private UUID clinic_id;
}
