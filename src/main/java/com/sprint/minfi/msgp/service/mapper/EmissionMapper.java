package com.sprint.minfi.msgp.service.mapper;


import com.sprint.minfi.msgp.domain.*;
import com.sprint.minfi.msgp.service.dto.EmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emission} and its DTO {@link EmissionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmissionMapper extends EntityMapper<EmissionDTO, Emission> {



    default Emission fromId(Long id) {
        if (id == null) {
            return null;
        }
        Emission emission = new Emission();
        emission.setId(id);
        return emission;
    }
}
