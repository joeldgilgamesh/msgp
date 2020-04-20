package com.sprint.minfi.msgp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class EmissionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmissionDTO.class);
        EmissionDTO emissionDTO1 = new EmissionDTO();
        emissionDTO1.setId(1L);
        EmissionDTO emissionDTO2 = new EmissionDTO();
        assertThat(emissionDTO1).isNotEqualTo(emissionDTO2);
        emissionDTO2.setId(emissionDTO1.getId());
        assertThat(emissionDTO1).isEqualTo(emissionDTO2);
        emissionDTO2.setId(2L);
        assertThat(emissionDTO1).isNotEqualTo(emissionDTO2);
        emissionDTO1.setId(null);
        assertThat(emissionDTO1).isNotEqualTo(emissionDTO2);
    }
}
