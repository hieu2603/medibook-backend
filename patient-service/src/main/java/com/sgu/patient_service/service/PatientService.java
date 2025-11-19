package com.sgu.patient_service.service;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PatientService {

    PatientResponseDto createPatient(PatientCreateRequest patientCreateRequest);

    PatientResponseDto getPatientById(UUID patientId);

    Page<PatientResponseDto> getAllPatients(Pageable pageable);

    PatientResponseDto updatePatient(
            UUID patientId,
            PatientUpdateRequest patientUpdateRequest,
            UUID userId,
            String role
    );

    void deletePatient(UUID patientId);

    Page<PatientResponseDto> searchPatients(String name, String phone, Pageable pageable);
}
