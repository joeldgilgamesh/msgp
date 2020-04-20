package com.sprint.minfi.msgp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmissionHistoriqueMapperTest {

    private EmissionHistoriqueMapper emissionHistoriqueMapper;

    @BeforeEach
    public void setUp() {
        emissionHistoriqueMapper = new EmissionHistoriqueMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(emissionHistoriqueMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(emissionHistoriqueMapper.fromId(null)).isNull();
    }
}
