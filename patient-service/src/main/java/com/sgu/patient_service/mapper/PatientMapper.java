package com.sgu.patient_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sgu.patient_service.dto.request.PatientCreateRequest;
import com.sgu.patient_service.dto.request.PatientUpdateRequest;
import com.sgu.patient_service.dto.response.PatientResponseDto;
import com.sgu.patient_service.model.Patient;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PatientMapper {

    /**
     * Maps PatientCreateRequest to Patient entity
     * 
     * @param request the create request DTO
     * @return Patient entity
     */
    @Mapping(source = "full_name", target = "full_name")
    @Mapping(source = "user_id", target = "user_id")
    Patient toEntity(PatientCreateRequest request);

    /**
     * Maps Patient entity to PatientResponseDto
     * 
     * @param entity the patient entity
     * @return PatientResponseDto
     */
    @Mapping(source = "patient_id", target = "patient_id")
    @Mapping(source = "full_name", target = "full_name")
    @Mapping(source = "user_id", target = "user_id")
    PatientResponseDto toResponseDto(Patient entity);

    /**
     * Maps list of Patient entities to list of PatientResponseDto
     * 
     * @param entities list of patient entities
     * @return list of PatientResponseDto
     */
    List<PatientResponseDto> toResponseDtoList(List<Patient> entities);

    /**
     * Updates existing Patient entity with values from PatientUpdateRequest
     * Only non-null values from the request will be applied
     * 
     * @param request the update request DTO
     * @param entity  the existing patient entity to update
     */
    @Mapping(source = "full_name", target = "full_name")
    @Mapping(source = "user_id", target = "user_id")
    void updateEntityFromRequest(PatientUpdateRequest request, @MappingTarget Patient entity);
}
