package com.mrhopeyone.todontapp.repository;

import com.mrhopeyone.todontapp.domain.TodontGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TodontGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodontGroupRepository extends JpaRepository<TodontGroup, Long> {}
