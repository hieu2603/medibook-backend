package com.sgu.patient_service.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;
import com.sgu.patient_service.exception.PatientNotFoundException;
import com.sgu.patient_service.mapper.PatientMapper;
import com.sgu.patient_service.model.Patient;
import com.sgu.patient_service.repository.PatientReposistory;
import com.sgu.patient_service.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientReposistory patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientReposistory patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientResponseDto createPatient(PatientCreateRequest patientCreateRequest) {
        Patient patient = patientMapper.toEntity(patientCreateRequest);
        System.err.println("Tesst");
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toResponseDto(savedPatient);
    }

    @Override
    public PatientResponseDto getPatientById(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        return patientMapper.toResponseDto(patient);
    }

    @Override
    public Page<PatientResponseDto> getAllPatients(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(patientMapper::toResponseDto);
    }

    @Override
    public PatientResponseDto updatePatient(UUID patientId, PatientUpdateRequest patientUpdateRequest) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        patientMapper.updateEntityFromRequest(patientUpdateRequest, existingPatient);

        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toResponseDto(updatedPatient);
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
        Page<Patient> patients = patientRepository.findByNameAndPhone(name, phone, pageable);
        return patients.map(patientMapper::toResponseDto);
    }
}
