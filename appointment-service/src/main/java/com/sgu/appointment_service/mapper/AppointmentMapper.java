package com.sgu.appointment_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.response.AppointmentResponseDto;
import com.sgu.appointment_service.enums.AppointmentStatus;
import com.sgu.appointment_service.model.Appointment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = {
        AppointmentStatus.class })
public interface AppointmentMapper {

    @Mapping(target = "appointment_id", ignore = true)
    @Mapping(target = "status", expression = "java(AppointmentStatus.PENDING)")
    Appointment toEntity(AppointmentCreateRequest request);

    AppointmentResponseDto toResponseDto(Appointment entity);

    void updateEntityFromRequest(AppointmentUpdateRequest request, @MappingTarget Appointment entity);
}
