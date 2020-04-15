package com.sprint.minfi.msgp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sprint.minfi.msgp.service.mapper.impl.JustificatifPaiementMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class JustificatifPaiementMapperTest {

    private JustificatifPaiementMapper justificatifPaiementMapper;

    @BeforeEach
    public void setUp() {
        justificatifPaiementMapper = new JustificatifPaiementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(justificatifPaiementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(justificatifPaiementMapper.fromId(null)).isNull();
    }
}
