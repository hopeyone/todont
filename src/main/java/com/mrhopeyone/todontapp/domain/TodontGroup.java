package com.mrhopeyone.todontapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TodontGroup.
 */
@Entity
@Table(name = "todont_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TodontGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "todontGroup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "todontGroup" }, allowSetters = true)
    private Set<Todont> todonts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TodontGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TodontGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Todont> getTodonts() {
        return this.todonts;
    }

    public void setTodonts(Set<Todont> todonts) {
        if (this.todonts != null) {
            this.todonts.forEach(i -> i.setTodontGroup(null));
        }
        if (todonts != null) {
            todonts.forEach(i -> i.setTodontGroup(this));
        }
        this.todonts = todonts;
    }

    public TodontGroup todonts(Set<Todont> todonts) {
        this.setTodonts(todonts);
        return this;
    }

    public TodontGroup addTodont(Todont todont) {
        this.todonts.add(todont);
        todont.setTodontGroup(this);
        return this;
    }

    public TodontGroup removeTodont(Todont todont) {
        this.todonts.remove(todont);
        todont.setTodontGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TodontGroup)) {
            return false;
        }
        return id != null && id.equals(((TodontGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TodontGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
