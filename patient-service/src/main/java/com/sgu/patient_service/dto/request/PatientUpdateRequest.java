package com.sgu.patient_service.dto.request;

import java.util.Date;
import java.util.UUID;

import com.sgu.patient_service.enums.PatientGender;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
public class PatientUpdateRequest {
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String full_name;

    @Past(message = "Date of birth must be in the past")
    private Date dob;

    private PatientGender gender;

    @Pattern(regexp = "^(?:\\+84|0)(?:3[2-9]|5[25689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$|^(?:1800|1900)[0-9]{4,6}$", message = "Invalid phone number format")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    private UUID user_id;
}