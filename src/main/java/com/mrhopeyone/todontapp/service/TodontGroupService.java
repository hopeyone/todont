package com.mrhopeyone.todontapp.service;

import com.mrhopeyone.todontapp.domain.TodontGroup;
import com.mrhopeyone.todontapp.repository.TodontGroupRepository;
import com.mrhopeyone.todontapp.service.dto.TodontGroupDTO;
import com.mrhopeyone.todontapp.service.mapper.TodontGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TodontGroup}.
 */
@Service
@Transactional
public class TodontGroupService {

    private final Logger log = LoggerFactory.getLogger(TodontGroupService.class);

    private final TodontGroupRepository todontGroupRepository;

    private final TodontGroupMapper todontGroupMapper;

    public TodontGroupService(TodontGroupRepository todontGroupRepository, TodontGroupMapper todontGroupMapper) {
        this.todontGroupRepository = todontGroupRepository;
        this.todontGroupMapper = todontGroupMapper;
    }

    /**
     * Save a todontGroup.
     *
     * @param todontGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public TodontGroupDTO save(TodontGroupDTO todontGroupDTO) {
        log.debug("Request to save TodontGroup : {}", todontGroupDTO);
        TodontGroup todontGroup = todontGroupMapper.toEntity(todontGroupDTO);
        todontGroup = todontGroupRepository.save(todontGroup);
        return todontGroupMapper.toDto(todontGroup);
    }

    /**
     * Update a todontGroup.
     *
     * @param todontGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public TodontGroupDTO update(TodontGroupDTO todontGroupDTO) {
        log.debug("Request to update TodontGroup : {}", todontGroupDTO);
        TodontGroup todontGroup = todontGroupMapper.toEntity(todontGroupDTO);
        todontGroup = todontGroupRepository.save(todontGroup);
        return todontGroupMapper.toDto(todontGroup);
    }

    /**
     * Partially update a todontGroup.
     *
     * @param todontGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TodontGroupDTO> partialUpdate(TodontGroupDTO todontGroupDTO) {
        log.debug("Request to partially update TodontGroup : {}", todontGroupDTO);

        return todontGroupRepository
            .findById(todontGroupDTO.getId())
            .map(existingTodontGroup -> {
                todontGroupMapper.partialUpdate(existingTodontGroup, todontGroupDTO);

                return existingTodontGroup;
            })
            .map(todontGroupRepository::save)
            .map(todontGroupMapper::toDto);
    }

    /**
     * Get all the todontGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TodontGroupDTO> findAll() {
        log.debug("Request to get all TodontGroups");
        return todontGroupRepository.findAll().stream().map(todontGroupMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one todontGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TodontGroupDTO> findOne(Long id) {
        log.debug("Request to get TodontGroup : {}", id);
        return todontGroupRepository.findById(id).map(todontGroupMapper::toDto);
    }

    /**
     * Delete the todontGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TodontGroup : {}", id);
        todontGroupRepository.deleteById(id);
    }
}
