package com.mrhopeyone.todontapp.service.mapper;

import com.mrhopeyone.todontapp.domain.Todont;
import com.mrhopeyone.todontapp.domain.TodontGroup;
import com.mrhopeyone.todontapp.service.dto.TodontDTO;
import com.mrhopeyone.todontapp.service.dto.TodontGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Todont} and its DTO {@link TodontDTO}.
 */
@Mapper(componentModel = "spring")
public interface TodontMapper extends EntityMapper<TodontDTO, Todont> {
    @Mapping(target = "todontGroup", source = "todontGroup", qualifiedByName = "todontGroupId")
    TodontDTO toDto(Todont s);

    @Named("todontGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TodontGroupDTO toDtoTodontGroupId(TodontGroup todontGroup);
}
