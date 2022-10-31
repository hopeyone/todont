package com.mrhopeyone.todontapp.service.mapper;

import com.mrhopeyone.todontapp.domain.TodontGroup;
import com.mrhopeyone.todontapp.service.dto.TodontGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TodontGroup} and its DTO {@link TodontGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface TodontGroupMapper extends EntityMapper<TodontGroupDTO, TodontGroup> {}
