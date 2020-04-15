package com.sprint.minfi.msgp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class JustificatifPaiementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JustificatifPaiement.class);
        JustificatifPaiement justificatifPaiement1 = new JustificatifPaiement();
        justificatifPaiement1.setId(1L);
        JustificatifPaiement justificatifPaiement2 = new JustificatifPaiement();
        justificatifPaiement2.setId(justificatifPaiement1.getId());
        assertThat(justificatifPaiement1).isEqualTo(justificatifPaiement2);
        justificatifPaiement2.setId(2L);
        assertThat(justificatifPaiement1).isNotEqualTo(justificatifPaiement2);
        justificatifPaiement1.setId(null);
        assertThat(justificatifPaiement1).isNotEqualTo(justificatifPaiement2);
    }
}
