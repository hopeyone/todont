package com.mrhopeyone.todontapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mrhopeyone.todontapp.domain.Todont} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TodontDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    private LocalDate due;

    private TodontGroupDTO todontGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public TodontGroupDTO getTodontGroup() {
        return todontGroup;
    }

    public void setTodontGroup(TodontGroupDTO todontGroup) {
        this.todontGroup = todontGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodontDTO)) {
            return false;
        }

        TodontDTO todontDTO = (TodontDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, todontDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodontDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", due='" + getDue() + "'" +
            ", todontGroup=" + getTodontGroup() +
            "}";
    }
}
