package com.sgu.patient_service.controller;

import java.util.List;
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
import com.sgu.patient_service.dto.response.common.ApiResponse;
import com.sgu.patient_service.util.PaginationMetaUtils;
import com.sgu.patient_service.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponseDto>> createPatient(
            @Valid @RequestBody PatientCreateRequest patientCreateRequest) {
        PatientResponseDto createdPatient = patientService.createPatient(patientCreateRequest);
        ApiResponse<PatientResponseDto> body = ApiResponse.<PatientResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Patient created successfully")
                .data(createdPatient)
                .build();
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDto>> getPatientById(@PathVariable UUID id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        ApiResponse<PatientResponseDto> body = ApiResponse.<PatientResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Patient fetched successfully")
                .data(patient)
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getAllPatients(Pageable pageable) {
        Page<PatientResponseDto> patients = patientService.getAllPatients(pageable);
        ApiResponse<List<PatientResponseDto>> body = ApiResponse
                .<List<PatientResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Patients fetched successfully")
                .data(patients.getContent())
                .meta(PaginationMetaUtils.from(patients))
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDto>> updatePatient(@PathVariable UUID id,
            @Valid @RequestBody PatientUpdateRequest patientUpdateRequest) {
        PatientResponseDto updatedPatient = patientService.updatePatient(id, patientUpdateRequest);
        ApiResponse<PatientResponseDto> body = ApiResponse.<PatientResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Patient updated successfully")
                .data(updatedPatient)
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .success(true)
                .message("Patient deleted successfully")
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> searchPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            Pageable pageable) {
        Page<PatientResponseDto> patients = patientService.searchPatients(name, phone, pageable);

        ApiResponse<List<PatientResponseDto>> body = ApiResponse
                .<List<PatientResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Patients fetched successfully")
                .data(patients.getContent())
                .meta(PaginationMetaUtils.from(patients))
                .build();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
