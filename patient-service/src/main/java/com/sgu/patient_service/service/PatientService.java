package com.sgu.patient_service.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;

public interface PatientService {

    PatientResponseDto createPatient(PatientCreateRequest patientCreateRequest);

    PatientResponseDto getPatientById(UUID patientId);

    Page<PatientResponseDto> getAllPatients(Pageable pageable);

    PatientResponseDto updatePatient(UUID patientId, PatientUpdateRequest patientUpdateRequest);

    void deletePatient(UUID patientId);

    Page<PatientResponseDto> searchPatients(String name, String phone, Pageable pageable);
}
