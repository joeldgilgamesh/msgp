package com.sprintpay.minfi.msgp.service.mapper;


import org.mapstruct.*;

import com.sprintpay.minfi.msgp.domain.*;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;

/**
 * Mapper for the entity {@link DetailVersementIntermediaire} and its DTO {@link DetailVersementIntermediaireDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetailVersementIntermediaireMapper extends EntityMapper<DetailVersementIntermediaireDTO, DetailVersementIntermediaire> {



    default DetailVersementIntermediaire fromId(Long id) {
        if (id == null) {
            return null;
        }
        DetailVersementIntermediaire detailVersementIntermediaire = new DetailVersementIntermediaire();
        detailVersementIntermediaire.setId(id);
        return detailVersementIntermediaire;
    }
}
