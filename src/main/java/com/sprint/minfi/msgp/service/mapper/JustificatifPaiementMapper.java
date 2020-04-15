package com.sprint.minfi.msgp.service.mapper;


import com.sprint.minfi.msgp.domain.*;
import com.sprint.minfi.msgp.service.dto.JustificatifPaiementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link JustificatifPaiement} and its DTO {@link JustificatifPaiementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JustificatifPaiementMapper extends EntityMapper<JustificatifPaiementDTO, JustificatifPaiement> {



    default JustificatifPaiement fromId(Long id) {
        if (id == null) {
            return null;
        }
        JustificatifPaiement justificatifPaiement = new JustificatifPaiement();
        justificatifPaiement.setId(id);
        return justificatifPaiement;
    }
}
