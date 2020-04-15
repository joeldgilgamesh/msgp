package com.sprint.minfi.msgp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class JustificatifPaiementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JustificatifPaiementDTO.class);
        JustificatifPaiementDTO justificatifPaiementDTO1 = new JustificatifPaiementDTO();
        justificatifPaiementDTO1.setId(1L);
        JustificatifPaiementDTO justificatifPaiementDTO2 = new JustificatifPaiementDTO();
        assertThat(justificatifPaiementDTO1).isNotEqualTo(justificatifPaiementDTO2);
        justificatifPaiementDTO2.setId(justificatifPaiementDTO1.getId());
        assertThat(justificatifPaiementDTO1).isEqualTo(justificatifPaiementDTO2);
        justificatifPaiementDTO2.setId(2L);
        assertThat(justificatifPaiementDTO1).isNotEqualTo(justificatifPaiementDTO2);
        justificatifPaiementDTO1.setId(null);
        assertThat(justificatifPaiementDTO1).isNotEqualTo(justificatifPaiementDTO2);
    }
}
