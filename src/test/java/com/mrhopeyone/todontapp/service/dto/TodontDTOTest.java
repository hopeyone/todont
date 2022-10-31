package com.mrhopeyone.todontapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrhopeyone.todontapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodontDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodontDTO.class);
        TodontDTO todontDTO1 = new TodontDTO();
        todontDTO1.setId(1L);
        TodontDTO todontDTO2 = new TodontDTO();
        assertThat(todontDTO1).isNotEqualTo(todontDTO2);
        todontDTO2.setId(todontDTO1.getId());
        assertThat(todontDTO1).isEqualTo(todontDTO2);
        todontDTO2.setId(2L);
        assertThat(todontDTO1).isNotEqualTo(todontDTO2);
        todontDTO1.setId(null);
        assertThat(todontDTO1).isNotEqualTo(todontDTO2);
    }
}
