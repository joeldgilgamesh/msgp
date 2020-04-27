package com.sprint.minfi.msgp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sprint.minfi.msgp.service.mapper.impl.HistoriquePaymentMapperImpl;
import com.sprintpay.minfi.msgp.service.mapper.HistoriquePaymentMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoriquePaymentMapperTest {

    private HistoriquePaymentMapper historiquePaymentMapper;

    @BeforeEach
    public void setUp() {
        historiquePaymentMapper = new HistoriquePaymentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(historiquePaymentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(historiquePaymentMapper.fromId(null)).isNull();
    }
}
