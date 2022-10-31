package com.mrhopeyone.todontapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrhopeyone.todontapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodontGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodontGroup.class);
        TodontGroup todontGroup1 = new TodontGroup();
        todontGroup1.setId(1L);
        TodontGroup todontGroup2 = new TodontGroup();
        todontGroup2.setId(todontGroup1.getId());
        assertThat(todontGroup1).isEqualTo(todontGroup2);
        todontGroup2.setId(2L);
        assertThat(todontGroup1).isNotEqualTo(todontGroup2);
        todontGroup1.setId(null);
        assertThat(todontGroup1).isNotEqualTo(todontGroup2);
    }
}
