package org.ibp.api2.web.rest.mapper;

import org.ibp.api2.domain.*;
import org.ibp.api2.web.rest.dto.StudyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Study and its DTO StudyDTO.
 */
@Mapper(componentModel = "spring", uses = {TraitMapper.class, })
public interface StudyMapper {

    StudyDTO studyToStudyDTO(Study study);

    Study studyDTOToStudy(StudyDTO studyDTO);

    default Trait traitFromId(Long id) {
        if (id == null) {
            return null;
        }
        Trait trait = new Trait();
        trait.setId(id);
        return trait;
    }
}
