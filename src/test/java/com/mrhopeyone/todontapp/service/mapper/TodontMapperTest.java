package com.mrhopeyone.todontapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TodontMapperTest {

    private TodontMapper todontMapper;

    @BeforeEach
    public void setUp() {
        todontMapper = new TodontMapperImpl();
    }
}
