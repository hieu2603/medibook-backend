package com.sgu.appointment_service.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sgu.appointment_service.client.ClinicServiceClient;
import com.sgu.appointment_service.client.PatientServiceClient;
import com.sgu.appointment_service.client.UserServiceClient;
import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.request.RescheduleRequest;
import com.sgu.appointment_service.dto.request.TransferRequestDto;
import com.sgu.appointment_service.dto.response.AppointmentResponseDto;
import com.sgu.appointment_service.dto.response.ClinicResponseDto;
import com.sgu.appointment_service.dto.response.PatientResponseDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import com.sgu.appointment_service.enums.AppointmentStatus;
import com.sgu.appointment_service.enums.TransferType;
import com.sgu.appointment_service.exception.InsufficientBalanceException;
import com.sgu.appointment_service.exception.ResourceNotFoundException;
import com.sgu.appointment_service.mapper.AppointmentMapper;
import com.sgu.appointment_service.model.Appointment;
import com.sgu.appointment_service.repository.AppointmentRepository;
import com.sgu.appointment_service.security.AppointmentPermissionValidator;
import com.sgu.appointment_service.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserServiceClient userServiceClient;
    private final PatientServiceClient patientServiceClient;
    private final ClinicServiceClient clinicServiceClient;
    private final AppointmentPermissionValidator permissionValidator;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentCreateRequest request, UUID userId, String role) {
        validateTimeRange(request.getStart_time(), request.getEnd_time());
        boolean conflict = appointmentRepository.existsOverlappingConfirmed(request.getClinic_id(),
                request.getDoctor_id(),
                request.getStart_time(), request.getEnd_time());
        if (conflict) {
            throw new IllegalArgumentException("Time slot conflicts with an existing confirmed appointment");
        }

        ResponseEntity<ApiResponse<PatientResponseDto>> patientResponseEntity = patientServiceClient
                .getPatientById(request.getPatient_id());
        ApiResponse<PatientResponseDto> patientResponse = patientResponseEntity.getBody();
        if (patientResponse == null || patientResponse.getData() == null) {
            throw new ResourceNotFoundException("Patient", request.getPatient_id());
        }
        UUID patientUserId = patientResponse.getData().getUserId();

        permissionValidator.validateCreatePermission(patientUserId, userId, role);

        ResponseEntity<ApiResponse<ClinicResponseDto>> clinicResponseEntity = clinicServiceClient
                .getClinicById(request.getClinic_id());
        ApiResponse<ClinicResponseDto> clinicResponse = clinicResponseEntity.getBody();
        if (clinicResponse == null || clinicResponse.getData() == null) {
            throw new ResourceNotFoundException("Clinic", request.getClinic_id());
        }
        UUID clinicUserId = clinicResponse.getData().getUserId();

        TransferRequestDto transferRequest = TransferRequestDto.builder()
                .fromUserId(patientUserId)
                .toUserId(clinicUserId)
                .amount(request.getPrice())
                .type(TransferType.APPOINTMENT)
                .build();

        try {
            userServiceClient.payForAppointment(transferRequest);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("balance")) {
                throw new InsufficientBalanceException("Patient does not have enough balance for appointment");
            }
            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }

        Appointment entity = appointmentMapper.toEntity(request);
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto getById(UUID appointmentId, UUID userId, String role) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        ResponseEntity<ApiResponse<PatientResponseDto>> patientResponseEntity = patientServiceClient
                .getPatientById(entity.getPatient_id());
        ApiResponse<PatientResponseDto> patientResponse = patientResponseEntity.getBody();
        if (patientResponse == null || patientResponse.getData() == null) {
            throw new ResourceNotFoundException("Patient", entity.getPatient_id());
        }
        UUID patientUserId = patientResponse.getData().getUserId();

        ResponseEntity<ApiResponse<ClinicResponseDto>> clinicResponseEntity = clinicServiceClient
                .getClinicById(entity.getClinic_id());
        ApiResponse<ClinicResponseDto> clinicResponse = clinicResponseEntity.getBody();
        if (clinicResponse == null || clinicResponse.getData() == null) {
            throw new ResourceNotFoundException("Clinic", entity.getClinic_id());
        }
        UUID clinicUserId = clinicResponse.getData().getUserId();

        permissionValidator.validateViewPermission(entity, patientUserId, clinicUserId, userId, role);

        return appointmentMapper.toResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponseDto> search(UUID patientId, UUID doctorId, UUID clinicId, AppointmentStatus status,
            LocalDateTime startFrom, LocalDateTime startTo, LocalDateTime endFrom, LocalDateTime endTo,
            Pageable pageable) {
        Page<Appointment> page = appointmentRepository.search(patientId, doctorId, clinicId, status, startFrom, startTo,
                endFrom, endTo, pageable);
        return page.map(appointmentMapper::toResponseDto);
    }

    @Override
    public AppointmentResponseDto updateAppointment(UUID appointmentId, AppointmentUpdateRequest request, UUID userId,
            String role) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        ResponseEntity<ApiResponse<PatientResponseDto>> patientResponseEntity = patientServiceClient
                .getPatientById(entity.getPatient_id());
        ApiResponse<PatientResponseDto> patientResponse = patientResponseEntity.getBody();
        if (patientResponse == null || patientResponse.getData() == null) {
            throw new ResourceNotFoundException("Patient", entity.getPatient_id());
        }
        UUID patientUserId = patientResponse.getData().getUserId();

        ResponseEntity<ApiResponse<ClinicResponseDto>> clinicResponseEntity = clinicServiceClient
                .getClinicById(entity.getClinic_id());
        ApiResponse<ClinicResponseDto> clinicResponse = clinicResponseEntity.getBody();
        if (clinicResponse == null || clinicResponse.getData() == null) {
            throw new ResourceNotFoundException("Clinic", entity.getClinic_id());
        }
        UUID clinicUserId = clinicResponse.getData().getUserId();

        permissionValidator.validateUpdatePermission(entity, patientUserId, clinicUserId, userId, role);

        if (request.getStart_time() != null || request.getEnd_time() != null) {
            LocalDateTime start = request.getStart_time() != null ? request.getStart_time() : entity.getStart_time();
            LocalDateTime end = request.getEnd_time() != null ? request.getEnd_time() : entity.getEnd_time();
            validateTimeRange(start, end);
        }
        appointmentMapper.updateEntityFromRequest(request, entity);
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    public void deleteAppointment(UUID appointmentId, UUID userId, String role) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        ResponseEntity<ApiResponse<PatientResponseDto>> patientResponseEntity = patientServiceClient
                .getPatientById(entity.getPatient_id());
        ApiResponse<PatientResponseDto> patientResponse = patientResponseEntity.getBody();
        if (patientResponse == null || patientResponse.getData() == null) {
            throw new ResourceNotFoundException("Patient", entity.getPatient_id());
        }
        UUID patientUserId = patientResponse.getData().getUserId();

        ResponseEntity<ApiResponse<ClinicResponseDto>> clinicResponseEntity = clinicServiceClient
                .getClinicById(entity.getClinic_id());
        ApiResponse<ClinicResponseDto> clinicResponse = clinicResponseEntity.getBody();
        if (clinicResponse == null || clinicResponse.getData() == null) {
            throw new ResourceNotFoundException("Clinic", entity.getClinic_id());
        }
        UUID clinicUserId = clinicResponse.getData().getUserId();

        permissionValidator.validateDeletePermission(entity, patientUserId, clinicUserId, userId, role);

        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public AppointmentResponseDto updateStatus(UUID appointmentId, AppointmentStatus status, UUID userId, String role) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        ResponseEntity<ApiResponse<PatientResponseDto>> patientResponseEntity = patientServiceClient
                .getPatientById(entity.getPatient_id());
        ApiResponse<PatientResponseDto> patientResponse = patientResponseEntity.getBody();
        if (patientResponse == null || patientResponse.getData() == null) {
            throw new ResourceNotFoundException("Patient", entity.getPatient_id());
        }
        UUID patientUserId = patientResponse.getData().getUserId();

        ResponseEntity<ApiResponse<ClinicResponseDto>> clinicResponseEntity = clinicServiceClient
                .getClinicById(entity.getClinic_id());
        ApiResponse<ClinicResponseDto> clinicResponse = clinicResponseEntity.getBody();
        if (clinicResponse == null || clinicResponse.getData() == null) {
            throw new ResourceNotFoundException("Clinic", entity.getClinic_id());
        }
        UUID clinicUserId = clinicResponse.getData().getUserId();

        if (status == AppointmentStatus.CANCELLED && entity.getStatus() != AppointmentStatus.CANCELLED) {
            permissionValidator.validateCancelPermission(entity, patientUserId, clinicUserId, userId, role);

            TransferRequestDto refundRequest = TransferRequestDto.builder()
                    .fromUserId(clinicUserId)
                    .toUserId(patientUserId)
                    .amount(entity.getPrice())
                    .type(TransferType.REFUND)
                    .build();

            try {
                userServiceClient.refundPayment(refundRequest);
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("balance")) {
                    throw new InsufficientBalanceException("Clinic does not have enough pending balance to refund");
                }
                throw new RuntimeException("Failed to process refund: " + e.getMessage(), e);
            }
        } else {
            permissionValidator.validateUpdatePermission(entity, patientUserId, clinicUserId, userId, role);
        }

        if (status == AppointmentStatus.CONFIRMED) {
            boolean conflict = appointmentRepository.existsOverlappingConfirmed(entity.getClinic_id(),
                    entity.getDoctor_id(),
                    entity.getStart_time(), entity.getEnd_time());
            if (conflict) {
                throw new IllegalArgumentException("Time slot conflicts with an existing confirmed appointment");
            }
        }

        entity.setStatus(status);
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    public AppointmentResponseDto reschedule(UUID appointmentId, RescheduleRequest request, UUID userId, String role) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        ResponseEntity<ApiResponse<PatientResponseDto>> patientResponseEntity = patientServiceClient
                .getPatientById(entity.getPatient_id());
        ApiResponse<PatientResponseDto> patientResponse = patientResponseEntity.getBody();
        if (patientResponse == null || patientResponse.getData() == null) {
            throw new ResourceNotFoundException("Patient", entity.getPatient_id());
        }
        UUID patientUserId = patientResponse.getData().getUserId();

        ResponseEntity<ApiResponse<ClinicResponseDto>> clinicResponseEntity = clinicServiceClient
                .getClinicById(entity.getClinic_id());
        ApiResponse<ClinicResponseDto> clinicResponse = clinicResponseEntity.getBody();
        if (clinicResponse == null || clinicResponse.getData() == null) {
            throw new ResourceNotFoundException("Clinic", entity.getClinic_id());
        }
        UUID clinicUserId = clinicResponse.getData().getUserId();

        permissionValidator.validateUpdatePermission(entity, patientUserId, clinicUserId, userId, role);

        UUID doctor = request.getDoctor_id() != null ? request.getDoctor_id() : entity.getDoctor_id();
        validateTimeRange(request.getStart_time(), request.getEnd_time());
        boolean conflict = appointmentRepository.existsOverlappingConfirmed(entity.getClinic_id(), doctor,
                request.getStart_time(), request.getEnd_time());
        if (conflict) {
            throw new IllegalArgumentException("Time slot conflicts with an existing confirmed appointment");
        }
        entity.setDoctor_id(doctor);
        entity.setStart_time(request.getStart_time());
        entity.setEnd_time(request.getEnd_time());
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(UUID clinicId, UUID doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        validateTimeRange(startTime, endTime);
        boolean conflict = appointmentRepository.existsOverlappingConfirmed(clinicId, doctorId, startTime, endTime);
        return !conflict;
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("start_time must be before end_time");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("start_time must be in the future");
        }
    }
}
