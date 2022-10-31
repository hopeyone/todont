package com.mrhopeyone.todontapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TodontGroupMapperTest {

    private TodontGroupMapper todontGroupMapper;

    @BeforeEach
    public void setUp() {
        todontGroupMapper = new TodontGroupMapperImpl();
    }
}
