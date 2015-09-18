package org.ibp.api2.web.rest.mapper;

import org.ibp.api2.domain.*;
import org.ibp.api2.web.rest.dto.TraitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Trait and its DTO TraitDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TraitMapper {

    TraitDTO traitToTraitDTO(Trait trait);

    @Mapping(target = "studiess", ignore = true)
    Trait traitDTOToTrait(TraitDTO traitDTO);
}
