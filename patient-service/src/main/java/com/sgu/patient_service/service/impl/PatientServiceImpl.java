package com.sgu.patient_service.service.impl;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;
import com.sgu.patient_service.exception.PatientNotFoundException;
import com.sgu.patient_service.mapper.PatientMapper;
import com.sgu.patient_service.model.Patient;
import com.sgu.patient_service.repository.PatientRepository;
import com.sgu.patient_service.security.PatientPermissionValidator;
import com.sgu.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientPermissionValidator patientPermissionValidator;

    @Override
    public PatientResponseDto createPatient(PatientCreateRequest patientCreateRequest) {
        Patient patient = PatientMapper.toEntity(patientCreateRequest);
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(savedPatient);
    }

    @Override
    public PatientResponseDto getPatientById(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        return PatientMapper.toDto(patient);
    }

    @Override
    public Page<PatientResponseDto> getAllPatients(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(PatientMapper::toDto);
    }

    @Override
    public PatientResponseDto updatePatient(
            UUID patientId,
            PatientUpdateRequest patientUpdateRequest,
            UUID userId,
            String role
    ) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        patientPermissionValidator.validateUpdatePatientPermission(existingPatient, userId, role);

        PatientMapper.updateEntity(existingPatient, patientUpdateRequest);

        Patient updatedPatient = patientRepository.save(existingPatient);
        return PatientMapper.toDto(updatedPatient);
    }

    @Override
    public void deletePatient(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(patientId);
        }
        patientRepository.deleteById(patientId);
    }

    @Override
    public Page<PatientResponseDto> searchPatients(String name, String phone, Pageable pageable) {
        Page<Patient> patients;

        if (name != null && !name.isBlank() && phone != null && !phone.isBlank()) {
            patients = patientRepository.findByNameOrPhone(name.trim(), phone.trim(), pageable);
        } else if (name != null && !name.isBlank()) {
            patients = patientRepository.findByName(name.trim(), pageable);
        } else if (phone != null && !phone.isBlank()) {
            patients = patientRepository.findByPhone(phone.trim(), pageable);
        } else {
            patients = patientRepository.findAll(pageable);
        }

        return patients.map(PatientMapper::toDto);
    }
}
