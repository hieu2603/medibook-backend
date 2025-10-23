package com.sgu.appointment_service.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.request.RescheduleRequest;
import com.sgu.appointment_service.dto.response.AppointmentResponseDto;
import com.sgu.appointment_service.enums.AppointmentStatus;
import com.sgu.appointment_service.exception.ResourceNotFoundException;
import com.sgu.appointment_service.mapper.AppointmentMapper;
import com.sgu.appointment_service.model.Appointment;
import com.sgu.appointment_service.repository.AppointmentRepository;
import com.sgu.appointment_service.service.AppointmentService;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentCreateRequest request) {
        validateTimeRange(request.getStart_time(), request.getEnd_time());
        boolean conflict = appointmentRepository.existsOverlappingConfirmed(request.getClinic_id(),
                request.getDoctor_id(),
                request.getStart_time(), request.getEnd_time());
        if (conflict) {
            throw new IllegalArgumentException("Time slot conflicts with an existing confirmed appointment");
        }
        Appointment entity = appointmentMapper.toEntity(request);
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto getById(UUID appointmentId) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));
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
    public AppointmentResponseDto updateAppointment(UUID appointmentId, AppointmentUpdateRequest request) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));
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
    public void deleteAppointment(UUID appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException("Appointment", appointmentId);
        }
        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public AppointmentResponseDto updateStatus(UUID appointmentId, AppointmentStatus status) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));
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
    public AppointmentResponseDto reschedule(UUID appointmentId, RescheduleRequest request) {
        Appointment entity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));
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
