package com.sprint.minfi.msgp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class DetailVersementIntermediaireTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailVersementIntermediaire.class);
        DetailVersementIntermediaire detailVersementIntermediaire1 = new DetailVersementIntermediaire();
        detailVersementIntermediaire1.setId(1L);
        DetailVersementIntermediaire detailVersementIntermediaire2 = new DetailVersementIntermediaire();
        detailVersementIntermediaire2.setId(detailVersementIntermediaire1.getId());
        assertThat(detailVersementIntermediaire1).isEqualTo(detailVersementIntermediaire2);
        detailVersementIntermediaire2.setId(2L);
        assertThat(detailVersementIntermediaire1).isNotEqualTo(detailVersementIntermediaire2);
        detailVersementIntermediaire1.setId(null);
        assertThat(detailVersementIntermediaire1).isNotEqualTo(detailVersementIntermediaire2);
    }
}
