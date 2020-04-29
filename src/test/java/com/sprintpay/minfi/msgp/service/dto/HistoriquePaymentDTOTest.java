package com.sprint.minfi.msgp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;
import com.sprintpay.minfi.msgp.service.dto.HistoriquePaymentDTO;

public class HistoriquePaymentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriquePaymentDTO.class);
        HistoriquePaymentDTO historiquePaymentDTO1 = new HistoriquePaymentDTO();
        historiquePaymentDTO1.setId(1L);
        HistoriquePaymentDTO historiquePaymentDTO2 = new HistoriquePaymentDTO();
        assertThat(historiquePaymentDTO1).isNotEqualTo(historiquePaymentDTO2);
        historiquePaymentDTO2.setId(historiquePaymentDTO1.getId());
        assertThat(historiquePaymentDTO1).isEqualTo(historiquePaymentDTO2);
        historiquePaymentDTO2.setId(2L);
        assertThat(historiquePaymentDTO1).isNotEqualTo(historiquePaymentDTO2);
        historiquePaymentDTO1.setId(null);
        assertThat(historiquePaymentDTO1).isNotEqualTo(historiquePaymentDTO2);
    }
}
