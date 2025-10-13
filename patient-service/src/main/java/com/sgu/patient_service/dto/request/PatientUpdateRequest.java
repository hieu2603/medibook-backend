package com.sgu.patient_service.dto.request;

import java.util.Date;
import java.util.UUID;

import com.sgu.patient_service.enums.PatientGender;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatientUpdateRequest {
    private String full_name;

    private Date dob;

    private PatientGender gender;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone number must be 10 digits and start with 0")
    private String phone;

    private String address;

    private UUID user_id;
}