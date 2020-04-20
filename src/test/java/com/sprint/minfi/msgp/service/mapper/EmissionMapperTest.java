package com.sprint.minfi.msgp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmissionMapperTest {

    private EmissionMapper emissionMapper;

    @BeforeEach
    public void setUp() {
        emissionMapper = new EmissionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(emissionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(emissionMapper.fromId(null)).isNull();
    }
}
