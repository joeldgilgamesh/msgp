package com.sprintpay.minfi.msgp.service.mapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sprintpay.minfi.msgp.service.mapper.impl.DetailVersementIntermediaireMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class DetailVersementIntermediaireMapperTest {

    private DetailVersementIntermediaireMapper detailVersementIntermediaireMapper;

    @BeforeEach
    public void setUp() {
        detailVersementIntermediaireMapper = new DetailVersementIntermediaireMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(detailVersementIntermediaireMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(detailVersementIntermediaireMapper.fromId(null)).isNull();
    }
}
