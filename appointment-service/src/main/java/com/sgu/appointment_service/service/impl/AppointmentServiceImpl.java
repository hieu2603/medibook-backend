package com.sgu.appointment_service.service.impl;

import com.sgu.appointment_service.constant.AppointmentStatus;
import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.response.appointment.AppointmentResponseDto;
import com.sgu.appointment_service.dto.response.common.PaginationMeta;
import com.sgu.appointment_service.dto.response.common.PaginationResponse;
import com.sgu.appointment_service.dto.response.doctor.DoctorAvailableResponse;
import com.sgu.appointment_service.dto.response.doctor.TimeRangeDto;
import com.sgu.appointment_service.exception.AppointmentConflictException;
import com.sgu.appointment_service.exception.InvalidTimeRangeException;
import com.sgu.appointment_service.exception.ResourceNotFoundException;
import com.sgu.appointment_service.mapper.AppointmentMapper;
import com.sgu.appointment_service.model.Appointment;
import com.sgu.appointment_service.repository.AppointmentRepository;
import com.sgu.appointment_service.service.AppointmentService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public PaginationResponse<AppointmentResponseDto> getAppointments(
            UUID patientId, UUID clinicId,
            LocalDateTime startTime, LocalDateTime endTime,
            AppointmentStatus status,
            int page, int size
    ) {
        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("startTime").descending());

        Specification<Appointment> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (patientId != null) {
                predicates.add(criteriaBuilder.equal(root.get("patientId"), patientId));
            }
            if (clinicId != null) {
                predicates.add(criteriaBuilder.equal(root.get("clinicId"), clinicId));
            }
            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime));
            }
            // Trả về các kết quả đến endTime - 1 ngày
            if (endTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), endTime));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        Page<Appointment> appointmentPage = appointmentRepository.findAll(specification, pageable);

        List<AppointmentResponseDto> data = appointmentPage
                .map(AppointmentMapper::toDto)
                .getContent();

        long totalItems = appointmentPage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : appointmentPage.getNumber() + 1)
                .pageSize(appointmentPage.getSize())
                .totalPages(appointmentPage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<AppointmentResponseDto>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    @Override
    @Transactional
    public AppointmentResponseDto createAppointment(AppointmentCreateRequest dto) {
        UUID doctorId = dto.getDoctorId();
        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new InvalidTimeRangeException("Start time must be before end time");
        }

        // Kiểm tra lịch trùng của Doctor
        checkDoctorAvailability(doctorId, startTime, endTime, null);

        Appointment newAppointment = AppointmentMapper.toEntity(dto);
        appointmentRepository.save(newAppointment);

        return AppointmentMapper.toDto(newAppointment);
    }

    @Override
    public void confirmAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        if (!appointment.getStatus().equals(AppointmentStatus.PENDING)) {
            throw new IllegalArgumentException("Only appointment with PENDING status can be confirmed");
        }

        UUID doctorId = appointment.getDoctorId();

        checkDoctorAvailability(
                doctorId,
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointmentId
        );

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        return AppointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponseDto updateAppointment(UUID appointmentId, AppointmentUpdateRequest dto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        // Chỉ cho phép update appointment khi status là PENDING
        if (!appointment.getStatus().equals(AppointmentStatus.PENDING)) {
            throw new IllegalArgumentException("Only PENDING appointment can be updated");
        }

        UUID newDoctorId = dto.getDoctorId();
        LocalDateTime newStart = dto.getStartTime();
        LocalDateTime newEnd = dto.getEndTime();
        String newDescription = dto.getDescription();

        // Validate time nếu có thay đổi
        if (newStart != null && newEnd != null) {
            if (newStart.isAfter(newEnd) || newStart.isEqual(newEnd)) {
                throw new InvalidTimeRangeException("Start time must be before end time");
            }
        }

        // Nếu thay đổi thời gian hoặc bác sĩ -> check conflict
        boolean timeChanged = newStart != null || newEnd != null;
        boolean doctorChanged = newDoctorId != null && !newDoctorId.equals(appointment.getDoctorId());

        if (timeChanged || doctorChanged) {
            UUID doctorToCheck = doctorChanged ? newDoctorId : appointment.getDoctorId();
            LocalDateTime startToCheck = newStart != null ? newStart : appointment.getStartTime();
            LocalDateTime endToCheck = newEnd != null ? newEnd : appointment.getEndTime();

            checkDoctorAvailability(doctorToCheck, startToCheck, endToCheck, appointmentId);
        }

        // Update lại appointment
        if (newDoctorId != null) appointment.setDoctorId(newDoctorId);
        if (newStart != null) appointment.setStartTime(newStart);
        if (newEnd != null) appointment.setEndTime(newEnd);
        if (newDescription != null) appointment.setDescription(newDescription);

        appointmentRepository.save(appointment);

        return AppointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        AppointmentStatus status = appointment.getStatus();

        if (!(status.equals(AppointmentStatus.PENDING) || status.equals(AppointmentStatus.CONFIRMED))) {
            throw new IllegalArgumentException("Only PENDING or CONFIRMED appointment can be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public DoctorAvailableResponse getDoctorAvailableSlots(UUID doctorId, LocalDate date) {
        // Lấy tất cả lịch của bác sĩ trong ngày
        List<Appointment> appointments = appointmentRepository
                .findAppointmentsByDoctorAndDate(doctorId, date);

        // Chuyển thành các khoảng thời gian bận
        List<TimeRangeDto> busyRanges = appointments.stream()
                .map(a -> new TimeRangeDto(a.getStartTime().toLocalTime(), a.getEndTime().toLocalTime()))
                .toList();

        // Khởi tạo slot từ 8:00 -> 17:00, mỗi slot 1h
        List<TimeRangeDto> availableRanges = new ArrayList<>();
        LocalTime slotStart = LocalTime.of(8, 0);
        LocalTime slotEnd = LocalTime.of(17, 0);

        while (slotStart.isBefore(slotEnd)) {
            LocalTime slotFinish = slotStart.plusHours(1);

            LocalTime finalSlotStart = slotStart;
            boolean isConflict = busyRanges.stream().anyMatch(br ->
                    finalSlotStart.isBefore(br.getEndTime()) && slotFinish.isAfter(br.getStartTime())
            );

            if (!isConflict) {
                availableRanges.add(new TimeRangeDto(slotStart, slotFinish));
            }

            slotStart = slotStart.plusHours(1);
        }

        return new DoctorAvailableResponse(doctorId, date, availableRanges);
    }

    private void checkDoctorAvailability(
            UUID doctorId, LocalDateTime startTime, LocalDateTime endTime, UUID appointmentId
    ) {
        boolean hasConflict = appointmentRepository
                .findConflictingAppointmentByDoctor(doctorId, startTime, endTime, appointmentId)
                .isPresent();

        if (hasConflict) {
            throw new AppointmentConflictException("Doctor is not available in this time");
        }
    }
}
