package com.sprint.minfi.msgp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sprint.minfi.msgp.web.rest.TestUtil;
import com.sprintpay.minfi.msgp.service.dto.DetailVersementIntermediaireDTO;

public class DetailVersementIntermediaireDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailVersementIntermediaireDTO.class);
        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO1 = new DetailVersementIntermediaireDTO();
        detailVersementIntermediaireDTO1.setId(1L);
        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO2 = new DetailVersementIntermediaireDTO();
        assertThat(detailVersementIntermediaireDTO1).isNotEqualTo(detailVersementIntermediaireDTO2);
        detailVersementIntermediaireDTO2.setId(detailVersementIntermediaireDTO1.getId());
        assertThat(detailVersementIntermediaireDTO1).isEqualTo(detailVersementIntermediaireDTO2);
        detailVersementIntermediaireDTO2.setId(2L);
        assertThat(detailVersementIntermediaireDTO1).isNotEqualTo(detailVersementIntermediaireDTO2);
        detailVersementIntermediaireDTO1.setId(null);
        assertThat(detailVersementIntermediaireDTO1).isNotEqualTo(detailVersementIntermediaireDTO2);
    }
}
