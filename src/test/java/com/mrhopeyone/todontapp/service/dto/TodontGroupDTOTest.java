package com.mrhopeyone.todontapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mrhopeyone.todontapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TodontGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TodontGroupDTO.class);
        TodontGroupDTO todontGroupDTO1 = new TodontGroupDTO();
        todontGroupDTO1.setId(1L);
        TodontGroupDTO todontGroupDTO2 = new TodontGroupDTO();
        assertThat(todontGroupDTO1).isNotEqualTo(todontGroupDTO2);
        todontGroupDTO2.setId(todontGroupDTO1.getId());
        assertThat(todontGroupDTO1).isEqualTo(todontGroupDTO2);
        todontGroupDTO2.setId(2L);
        assertThat(todontGroupDTO1).isNotEqualTo(todontGroupDTO2);
        todontGroupDTO1.setId(null);
        assertThat(todontGroupDTO1).isNotEqualTo(todontGroupDTO2);
    }
}
