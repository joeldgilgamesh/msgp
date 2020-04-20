package com.sprint.minfi.msgp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;

public class EmissionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emission.class);
        Emission emission1 = new Emission();
        emission1.setId(1L);
        Emission emission2 = new Emission();
        emission2.setId(emission1.getId());
        assertThat(emission1).isEqualTo(emission2);
        emission2.setId(2L);
        assertThat(emission1).isNotEqualTo(emission2);
        emission1.setId(null);
        assertThat(emission1).isNotEqualTo(emission2);
    }
}
