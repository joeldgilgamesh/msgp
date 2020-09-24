package com.sprintpay.minfi.msgp.service.mapper;


import org.mapstruct.*;

import com.sprintpay.minfi.msgp.domain.*;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {DetailVersementIntermediaireMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "idDetVers.id", target = "idDetVersId")
    PaymentDTO toDto(Payment payment);

    @Mapping(target = "idHistPays", ignore = true)
    @Mapping(target = "removeIdHistPay", ignore = true)
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
