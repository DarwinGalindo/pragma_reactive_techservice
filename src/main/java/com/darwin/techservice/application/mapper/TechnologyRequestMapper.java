package com.darwin.techservice.application.mapper;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.dto.TechnologyResponse;
import com.darwin.techservice.domain.model.Technology;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TechnologyRequestMapper {
    Technology toModel(TechnologyRequest technologyRequest);
    TechnologyResponse toResponse(Technology technology);
}
