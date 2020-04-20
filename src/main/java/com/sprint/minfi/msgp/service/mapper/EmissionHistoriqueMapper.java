package com.sprint.minfi.msgp.service.mapper;


import com.sprint.minfi.msgp.domain.*;
import com.sprint.minfi.msgp.service.dto.EmissionHistoriqueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmissionHistorique} and its DTO {@link EmissionHistoriqueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmissionHistoriqueMapper extends EntityMapper<EmissionHistoriqueDTO, EmissionHistorique> {



    default EmissionHistorique fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmissionHistorique emissionHistorique = new EmissionHistorique();
        emissionHistorique.setId(id);
        return emissionHistorique;
    }
}
