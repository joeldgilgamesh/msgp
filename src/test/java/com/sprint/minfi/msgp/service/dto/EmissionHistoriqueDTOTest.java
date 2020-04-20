package com.sprint.minfi.msgp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class EmissionHistoriqueDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmissionHistoriqueDTO.class);
        EmissionHistoriqueDTO emissionHistoriqueDTO1 = new EmissionHistoriqueDTO();
        emissionHistoriqueDTO1.setId(1L);
        EmissionHistoriqueDTO emissionHistoriqueDTO2 = new EmissionHistoriqueDTO();
        assertThat(emissionHistoriqueDTO1).isNotEqualTo(emissionHistoriqueDTO2);
        emissionHistoriqueDTO2.setId(emissionHistoriqueDTO1.getId());
        assertThat(emissionHistoriqueDTO1).isEqualTo(emissionHistoriqueDTO2);
        emissionHistoriqueDTO2.setId(2L);
        assertThat(emissionHistoriqueDTO1).isNotEqualTo(emissionHistoriqueDTO2);
        emissionHistoriqueDTO1.setId(null);
        assertThat(emissionHistoriqueDTO1).isNotEqualTo(emissionHistoriqueDTO2);
    }
}
