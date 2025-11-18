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
    @Column(name = "img_id")
    private UUID imgId;

    @Column(name = "img_url", nullable = false)
    private String url;

    @Column(name = "clinic_id", nullable = false)
    private UUID clinicId;
}
