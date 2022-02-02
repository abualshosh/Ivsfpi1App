package com.fu.ivsfpi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fu.ivsfpi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComplainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Complain.class);
        Complain complain1 = new Complain();
        complain1.setId(1L);
        Complain complain2 = new Complain();
        complain2.setId(complain1.getId());
        assertThat(complain1).isEqualTo(complain2);
        complain2.setId(2L);
        assertThat(complain1).isNotEqualTo(complain2);
        complain1.setId(null);
        assertThat(complain1).isNotEqualTo(complain2);
    }
}
