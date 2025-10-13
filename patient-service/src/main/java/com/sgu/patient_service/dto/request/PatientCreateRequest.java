package com.sgu.patient_service.dto.request;

import java.util.Date;
import java.util.UUID;

import com.sgu.patient_service.enums.PatientGender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatientCreateRequest {
    @NotBlank(message = "Full name is required")
    private String full_name;

    @NotNull(message = "Date of birth is required")
    private Date dob;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "^(MALE|FEMALE)$", message = "Gender must be MALE or FEMALE")
    private PatientGender gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone number must be 10 digits and start with 0")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "User ID is required")
    private UUID user_id;
}