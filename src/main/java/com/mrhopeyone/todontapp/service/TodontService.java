package com.mrhopeyone.todontapp.service;

import com.mrhopeyone.todontapp.domain.Todont;
import com.mrhopeyone.todontapp.repository.TodontRepository;
import com.mrhopeyone.todontapp.service.dto.TodontDTO;
import com.mrhopeyone.todontapp.service.mapper.TodontMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Todont}.
 */
@Service
@Transactional
public class TodontService {

    private final Logger log = LoggerFactory.getLogger(TodontService.class);

    private final TodontRepository todontRepository;

    private final TodontMapper todontMapper;

    public TodontService(TodontRepository todontRepository, TodontMapper todontMapper) {
        this.todontRepository = todontRepository;
        this.todontMapper = todontMapper;
    }

    /**
     * Save a todont.
     *
     * @param todontDTO the entity to save.
     * @return the persisted entity.
     */
    public TodontDTO save(TodontDTO todontDTO) {
        log.debug("Request to save Todont : {}", todontDTO);
        Todont todont = todontMapper.toEntity(todontDTO);
        todont = todontRepository.save(todont);
        return todontMapper.toDto(todont);
    }

    /**
     * Update a todont.
     *
     * @param todontDTO the entity to save.
     * @return the persisted entity.
     */
    public TodontDTO update(TodontDTO todontDTO) {
        log.debug("Request to update Todont : {}", todontDTO);
        Todont todont = todontMapper.toEntity(todontDTO);
        todont = todontRepository.save(todont);
        return todontMapper.toDto(todont);
    }

    /**
     * Partially update a todont.
     *
     * @param todontDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TodontDTO> partialUpdate(TodontDTO todontDTO) {
        log.debug("Request to partially update Todont : {}", todontDTO);

        return todontRepository
            .findById(todontDTO.getId())
            .map(existingTodont -> {
                todontMapper.partialUpdate(existingTodont, todontDTO);

                return existingTodont;
            })
            .map(todontRepository::save)
            .map(todontMapper::toDto);
    }

    /**
     * Get all the todonts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TodontDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Todonts");
        return todontRepository.findAll(pageable).map(todontMapper::toDto);
    }

    /**
     * Get one todont by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TodontDTO> findOne(Long id) {
        log.debug("Request to get Todont : {}", id);
        return todontRepository.findById(id).map(todontMapper::toDto);
    }

    /**
     * Delete the todont by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Todont : {}", id);
        todontRepository.deleteById(id);
    }
}
