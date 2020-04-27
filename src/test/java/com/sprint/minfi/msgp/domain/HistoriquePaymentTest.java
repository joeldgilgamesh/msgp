package com.sprint.minfi.msgp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;
import com.sprintpay.minfi.msgp.domain.HistoriquePayment;

public class HistoriquePaymentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriquePayment.class);
        HistoriquePayment historiquePayment1 = new HistoriquePayment();
        historiquePayment1.setId(1L);
        HistoriquePayment historiquePayment2 = new HistoriquePayment();
        historiquePayment2.setId(historiquePayment1.getId());
        assertThat(historiquePayment1).isEqualTo(historiquePayment2);
        historiquePayment2.setId(2L);
        assertThat(historiquePayment1).isNotEqualTo(historiquePayment2);
        historiquePayment1.setId(null);
        assertThat(historiquePayment1).isNotEqualTo(historiquePayment2);
    }
}
