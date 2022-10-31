package com.mrhopeyone.todontapp.web.rest;

import com.mrhopeyone.todontapp.repository.TodontGroupRepository;
import com.mrhopeyone.todontapp.service.TodontGroupService;
import com.mrhopeyone.todontapp.service.dto.TodontGroupDTO;
import com.mrhopeyone.todontapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mrhopeyone.todontapp.domain.TodontGroup}.
 */
@RestController
@RequestMapping("/api")
public class TodontGroupResource {

    private final Logger log = LoggerFactory.getLogger(TodontGroupResource.class);

    private static final String ENTITY_NAME = "todontTodontGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TodontGroupService todontGroupService;

    private final TodontGroupRepository todontGroupRepository;

    public TodontGroupResource(TodontGroupService todontGroupService, TodontGroupRepository todontGroupRepository) {
        this.todontGroupService = todontGroupService;
        this.todontGroupRepository = todontGroupRepository;
    }

    /**
     * {@code POST  /todont-groups} : Create a new todontGroup.
     *
     * @param todontGroupDTO the todontGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new todontGroupDTO, or with status {@code 400 (Bad Request)} if the todontGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/todont-groups")
    public ResponseEntity<TodontGroupDTO> createTodontGroup(@Valid @RequestBody TodontGroupDTO todontGroupDTO) throws URISyntaxException {
        log.debug("REST request to save TodontGroup : {}", todontGroupDTO);
        if (todontGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new todontGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TodontGroupDTO result = todontGroupService.save(todontGroupDTO);
        return ResponseEntity
            .created(new URI("/api/todont-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /todont-groups/:id} : Updates an existing todontGroup.
     *
     * @param id the id of the todontGroupDTO to save.
     * @param todontGroupDTO the todontGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todontGroupDTO,
     * or with status {@code 400 (Bad Request)} if the todontGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the todontGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/todont-groups/{id}")
    public ResponseEntity<TodontGroupDTO> updateTodontGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TodontGroupDTO todontGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TodontGroup : {}, {}", id, todontGroupDTO);
        if (todontGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todontGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todontGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TodontGroupDTO result = todontGroupService.update(todontGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todontGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /todont-groups/:id} : Partial updates given fields of an existing todontGroup, field will ignore if it is null
     *
     * @param id the id of the todontGroupDTO to save.
     * @param todontGroupDTO the todontGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todontGroupDTO,
     * or with status {@code 400 (Bad Request)} if the todontGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the todontGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the todontGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/todont-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TodontGroupDTO> partialUpdateTodontGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TodontGroupDTO todontGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TodontGroup partially : {}, {}", id, todontGroupDTO);
        if (todontGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todontGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todontGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TodontGroupDTO> result = todontGroupService.partialUpdate(todontGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todontGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /todont-groups} : get all the todontGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of todontGroups in body.
     */
    @GetMapping("/todont-groups")
    public List<TodontGroupDTO> getAllTodontGroups() {
        log.debug("REST request to get all TodontGroups");
        return todontGroupService.findAll();
    }

    /**
     * {@code GET  /todont-groups/:id} : get the "id" todontGroup.
     *
     * @param id the id of the todontGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the todontGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todont-groups/{id}")
    public ResponseEntity<TodontGroupDTO> getTodontGroup(@PathVariable Long id) {
        log.debug("REST request to get TodontGroup : {}", id);
        Optional<TodontGroupDTO> todontGroupDTO = todontGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(todontGroupDTO);
    }

    /**
     * {@code DELETE  /todont-groups/:id} : delete the "id" todontGroup.
     *
     * @param id the id of the todontGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/todont-groups/{id}")
    public ResponseEntity<Void> deleteTodontGroup(@PathVariable Long id) {
        log.debug("REST request to delete TodontGroup : {}", id);
        todontGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
