package com.sprintpay.minfi.msgp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprintpay.minfi.msgp.web.rest.TestUtil;

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
