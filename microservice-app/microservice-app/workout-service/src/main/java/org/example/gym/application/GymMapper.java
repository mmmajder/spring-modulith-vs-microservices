package org.example.gym.application;

import org.example.gym.domain.Gym;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GymMapper {

    GymDto toDto(Gym gym);
    Gym toEntity(GymDto gym);
}
