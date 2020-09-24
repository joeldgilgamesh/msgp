package com.sprintpay.minfi.msgp.service.mapper;


import org.mapstruct.*;

import com.sprintpay.minfi.msgp.domain.*;
import com.sprintpay.minfi.msgp.service.dto.HistoriquePaymentDTO;

/**
 * Mapper for the entity {@link HistoriquePayment} and its DTO {@link HistoriquePaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface HistoriquePaymentMapper extends EntityMapper<HistoriquePaymentDTO, HistoriquePayment> {

    @Mapping(source = "payment.id", target = "paymentId")
    HistoriquePaymentDTO toDto(HistoriquePayment historiquePayment);

    @Mapping(source = "paymentId", target = "payment")
    HistoriquePayment toEntity(HistoriquePaymentDTO historiquePaymentDTO);

    default HistoriquePayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        HistoriquePayment historiquePayment = new HistoriquePayment();
        historiquePayment.setId(id);
        return historiquePayment;
    }
}
