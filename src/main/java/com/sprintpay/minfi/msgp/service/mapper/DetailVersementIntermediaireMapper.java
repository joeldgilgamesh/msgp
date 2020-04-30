package com.sprintpay.minfi.msgp.service.mapper;


import org.mapstruct.*;

import com.sprintpay.minfi.msgp.domain.*;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;

import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link DetailVersementIntermediaire} and its DTO {@link DetailVersementIntermediaireDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetailVersementIntermediaireMapper extends EntityMapper<DetailVersementIntermediaireDTO, DetailVersementIntermediaire> {

    @Mapping(target = "payments", ignore = true)
    DetailVersementIntermediaire toEntity(DetailVersementIntermediaireDTO detailVersementIntermediaireDTO);

    @Mapping(target = "paymentRefs", expression = "java(detailVersementIntermediaire.getPayments().stream().map(p -> p.getRefTransaction()).collect(java.util.stream.Collectors.toSet()))")
    DetailVersementIntermediaireDTO toDto(DetailVersementIntermediaire detailVersementIntermediaire);

    default DetailVersementIntermediaire fromId(Long id) {
        if (id == null) {
            return null;
        }
        DetailVersementIntermediaire detailVersementIntermediaire = new DetailVersementIntermediaire();
        detailVersementIntermediaire.setId(id);
        return detailVersementIntermediaire;
    }
}
