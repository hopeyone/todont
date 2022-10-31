package com.mrhopeyone.todontapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mrhopeyone.todontapp.domain.TodontGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TodontGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodontGroupDTO)) {
            return false;
        }

        TodontGroupDTO todontGroupDTO = (TodontGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todontGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodontGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
