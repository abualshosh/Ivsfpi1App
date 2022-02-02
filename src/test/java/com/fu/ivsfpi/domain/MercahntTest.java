package com.fu.ivsfpi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fu.ivsfpi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MercahntTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mercahnt.class);
        Mercahnt mercahnt1 = new Mercahnt();
        mercahnt1.setId(1L);
        Mercahnt mercahnt2 = new Mercahnt();
        mercahnt2.setId(mercahnt1.getId());
        assertThat(mercahnt1).isEqualTo(mercahnt2);
        mercahnt2.setId(2L);
        assertThat(mercahnt1).isNotEqualTo(mercahnt2);
        mercahnt1.setId(null);
        assertThat(mercahnt1).isNotEqualTo(mercahnt2);
    }
}
