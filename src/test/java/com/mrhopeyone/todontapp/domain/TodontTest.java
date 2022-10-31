package com.mrhopeyone.todontapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrhopeyone.todontapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodontTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Todont.class);
        Todont todont1 = new Todont();
        todont1.setId(1L);
        Todont todont2 = new Todont();
        todont2.setId(todont1.getId());
        assertThat(todont1).isEqualTo(todont2);
        todont2.setId(2L);
        assertThat(todont1).isNotEqualTo(todont2);
        todont1.setId(null);
        assertThat(todont1).isNotEqualTo(todont2);
    }
}
