package com.sprint.minfi.msgp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class EmissionHistoriqueTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmissionHistorique.class);
        EmissionHistorique emissionHistorique1 = new EmissionHistorique();
        emissionHistorique1.setId(1L);
        EmissionHistorique emissionHistorique2 = new EmissionHistorique();
        emissionHistorique2.setId(emissionHistorique1.getId());
        assertThat(emissionHistorique1).isEqualTo(emissionHistorique2);
        emissionHistorique2.setId(2L);
        assertThat(emissionHistorique1).isNotEqualTo(emissionHistorique2);
        emissionHistorique1.setId(null);
        assertThat(emissionHistorique1).isNotEqualTo(emissionHistorique2);
    }
}
