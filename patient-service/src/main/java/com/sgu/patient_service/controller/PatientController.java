package com.sgu.patient_service.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;
import com.sgu.patient_service.service.impl.PatientServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientServiceImpl patientService;

    public PatientController(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @Valid @RequestBody PatientCreateRequest patientCreateRequest) {
        PatientResponseDto createdPatient = patientService.createPatient(patientCreateRequest);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable UUID id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponseDto>> getAllPatients(Pageable pageable) {
        Page<PatientResponseDto> patients = patientService.getAllPatients(pageable);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id,
            @Valid @RequestBody PatientUpdateRequest patientUpdateRequest) {
        PatientResponseDto updatedPatient = patientService.updatePatient(id, patientUpdateRequest);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PatientResponseDto>> searchPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            Pageable pageable) {
        Page<PatientResponseDto> patients = patientService.searchPatients(name, phone, pageable);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
}
