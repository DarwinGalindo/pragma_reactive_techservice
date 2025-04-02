package com.darwin.techservice.infrastructure.output.jpa.mapper;

import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.infrastructure.output.jpa.entity.TechnologyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TechnologyEntityMapper {
    TechnologyEntity toEntity(Technology technology);

    Technology toModel(TechnologyEntity technologyEntity);
}
