package com.mrhopeyone.todontapp.web.rest;

import com.mrhopeyone.todontapp.repository.TodontRepository;
import com.mrhopeyone.todontapp.service.TodontService;
import com.mrhopeyone.todontapp.service.dto.TodontDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mrhopeyone.todontapp.domain.Todont}.
 */
@RestController
@RequestMapping("/api")
public class TodontResource {

    private final Logger log = LoggerFactory.getLogger(TodontResource.class);

    private static final String ENTITY_NAME = "todontTodont";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TodontService todontService;

    private final TodontRepository todontRepository;

    public TodontResource(TodontService todontService, TodontRepository todontRepository) {
        this.todontService = todontService;
        this.todontRepository = todontRepository;
    }

    /**
     * {@code POST  /todonts} : Create a new todont.
     *
     * @param todontDTO the todontDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new todontDTO, or with status {@code 400 (Bad Request)} if the todont has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/todonts")
    public ResponseEntity<TodontDTO> createTodont(@Valid @RequestBody TodontDTO todontDTO) throws URISyntaxException {
        log.debug("REST request to save Todont : {}", todontDTO);
        if (todontDTO.getId() != null) {
            throw new BadRequestAlertException("A new todont cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TodontDTO result = todontService.save(todontDTO);
        return ResponseEntity
            .created(new URI("/api/todonts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /todonts/:id} : Updates an existing todont.
     *
     * @param id the id of the todontDTO to save.
     * @param todontDTO the todontDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todontDTO,
     * or with status {@code 400 (Bad Request)} if the todontDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the todontDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/todonts/{id}")
    public ResponseEntity<TodontDTO> updateTodont(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TodontDTO todontDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Todont : {}, {}", id, todontDTO);
        if (todontDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todontDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todontRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TodontDTO result = todontService.update(todontDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todontDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /todonts/:id} : Partial updates given fields of an existing todont, field will ignore if it is null
     *
     * @param id the id of the todontDTO to save.
     * @param todontDTO the todontDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated todontDTO,
     * or with status {@code 400 (Bad Request)} if the todontDTO is not valid,
     * or with status {@code 404 (Not Found)} if the todontDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the todontDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/todonts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TodontDTO> partialUpdateTodont(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TodontDTO todontDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Todont partially : {}, {}", id, todontDTO);
        if (todontDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, todontDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!todontRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TodontDTO> result = todontService.partialUpdate(todontDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, todontDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /todonts} : get all the todonts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of todonts in body.
     */
    @GetMapping("/todonts")
    public ResponseEntity<List<TodontDTO>> getAllTodonts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Todonts");
        Page<TodontDTO> page = todontService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /todonts/:id} : get the "id" todont.
     *
     * @param id the id of the todontDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the todontDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todonts/{id}")
    public ResponseEntity<TodontDTO> getTodont(@PathVariable Long id) {
        log.debug("REST request to get Todont : {}", id);
        Optional<TodontDTO> todontDTO = todontService.findOne(id);
        return ResponseUtil.wrapOrNotFound(todontDTO);
    }

    /**
     * {@code DELETE  /todonts/:id} : delete the "id" todont.
     *
     * @param id the id of the todontDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/todonts/{id}")
    public ResponseEntity<Void> deleteTodont(@PathVariable Long id) {
        log.debug("REST request to delete Todont : {}", id);
        todontService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
