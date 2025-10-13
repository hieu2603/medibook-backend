package com.sgu.patient_service.exception;

import java.util.UUID;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(UUID patientId) {
        super("Patient not found with id: " + patientId);
    }

    public PatientNotFoundException(String message) {
        super(message);
    }
}
