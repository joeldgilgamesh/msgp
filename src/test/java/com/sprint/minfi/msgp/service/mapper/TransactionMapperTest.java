package com.sprint.minfi.msgp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sprint.minfi.msgp.service.mapper.impl.TransactionMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    public void setUp() {
        transactionMapper = new TransactionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(transactionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(transactionMapper.fromId(null)).isNull();
    }
}
