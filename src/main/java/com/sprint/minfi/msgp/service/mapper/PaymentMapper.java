package com.sprint.minfi.msgp.service.mapper;


import com.sprint.minfi.msgp.domain.*;
import com.sprint.minfi.msgp.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {TransactionMapper.class, DetailVersementIntermediaireMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "idTransaction.id", target = "idTransactionId")
    @Mapping(source = "idDetVers.id", target = "idDetVersId")
    PaymentDTO toDto(Payment payment);

    @Mapping(target = "idHistPays", ignore = true)
    @Mapping(target = "removeIdHistPay", ignore = true)
    @Mapping(source = "idTransactionId", target = "idTransaction")
    @Mapping(source = "idDetVersId", target = "idDetVers")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
