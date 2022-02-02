package com.fu.ivsfpi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fu.ivsfpi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phone.class);
        Phone phone1 = new Phone();
        phone1.setId(1L);
        Phone phone2 = new Phone();
        phone2.setId(phone1.getId());
        assertThat(phone1).isEqualTo(phone2);
        phone2.setId(2L);
        assertThat(phone1).isNotEqualTo(phone2);
        phone1.setId(null);
        assertThat(phone1).isNotEqualTo(phone2);
    }
}
