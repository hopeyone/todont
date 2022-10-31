package com.mrhopeyone.todontapp.repository;

import com.mrhopeyone.todontapp.domain.Todont;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Todont entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodontRepository extends JpaRepository<Todont, Long> {}
