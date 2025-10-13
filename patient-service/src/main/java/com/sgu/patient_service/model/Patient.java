package com.sgu.patient_service.model;

import java.util.Date;
import java.util.UUID;

import com.sgu.patient_service.enums.PatientGender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID patient_id;

    @NotNull
    private String full_name;

    @NotNull
    private Date dob;

    @NotNull
    private PatientGender gender;

    @NotNull
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private UUID user_id;
}
