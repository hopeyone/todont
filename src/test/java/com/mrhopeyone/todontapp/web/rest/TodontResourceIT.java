package com.mrhopeyone.todontapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mrhopeyone.todontapp.IntegrationTest;
import com.mrhopeyone.todontapp.domain.Todont;
import com.mrhopeyone.todontapp.repository.TodontRepository;
import com.mrhopeyone.todontapp.service.dto.TodontDTO;
import com.mrhopeyone.todontapp.service.mapper.TodontMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TodontResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TodontResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/todonts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TodontRepository todontRepository;

    @Autowired
    private TodontMapper todontMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTodontMockMvc;

    private Todont todont;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todont createEntity(EntityManager em) {
        Todont todont = new Todont().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).due(DEFAULT_DUE);
        return todont;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todont createUpdatedEntity(EntityManager em) {
        Todont todont = new Todont().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).due(UPDATED_DUE);
        return todont;
    }

    @BeforeEach
    public void initTest() {
        todont = createEntity(em);
    }

    @Test
    @Transactional
    void createTodont() throws Exception {
        int databaseSizeBeforeCreate = todontRepository.findAll().size();
        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);
        restTodontMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontDTO)))
            .andExpect(status().isCreated());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeCreate + 1);
        Todont testTodont = todontList.get(todontList.size() - 1);
        assertThat(testTodont.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTodont.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTodont.getDue()).isEqualTo(DEFAULT_DUE);
    }

    @Test
    @Transactional
    void createTodontWithExistingId() throws Exception {
        // Create the Todont with an existing ID
        todont.setId(1L);
        TodontDTO todontDTO = todontMapper.toDto(todont);

        int databaseSizeBeforeCreate = todontRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodontMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = todontRepository.findAll().size();
        // set the field null
        todont.setTitle(null);

        // Create the Todont, which fails.
        TodontDTO todontDTO = todontMapper.toDto(todont);

        restTodontMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontDTO)))
            .andExpect(status().isBadRequest());

        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTodonts() throws Exception {
        // Initialize the database
        todontRepository.saveAndFlush(todont);

        // Get all the todontList
        restTodontMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todont.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE.toString())));
    }

    @Test
    @Transactional
    void getTodont() throws Exception {
        // Initialize the database
        todontRepository.saveAndFlush(todont);

        // Get the todont
        restTodontMockMvc
            .perform(get(ENTITY_API_URL_ID, todont.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(todont.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.due").value(DEFAULT_DUE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTodont() throws Exception {
        // Get the todont
        restTodontMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTodont() throws Exception {
        // Initialize the database
        todontRepository.saveAndFlush(todont);

        int databaseSizeBeforeUpdate = todontRepository.findAll().size();

        // Update the todont
        Todont updatedTodont = todontRepository.findById(todont.getId()).get();
        // Disconnect from session so that the updates on updatedTodont are not directly saved in db
        em.detach(updatedTodont);
        updatedTodont.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).due(UPDATED_DUE);
        TodontDTO todontDTO = todontMapper.toDto(updatedTodont);

        restTodontMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todontDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todontDTO))
            )
            .andExpect(status().isOk());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
        Todont testTodont = todontList.get(todontList.size() - 1);
        assertThat(testTodont.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTodont.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodont.getDue()).isEqualTo(UPDATED_DUE);
    }

    @Test
    @Transactional
    void putNonExistingTodont() throws Exception {
        int databaseSizeBeforeUpdate = todontRepository.findAll().size();
        todont.setId(count.incrementAndGet());

        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodontMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todontDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todontDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTodont() throws Exception {
        int databaseSizeBeforeUpdate = todontRepository.findAll().size();
        todont.setId(count.incrementAndGet());

        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todontDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTodont() throws Exception {
        int databaseSizeBeforeUpdate = todontRepository.findAll().size();
        todont.setId(count.incrementAndGet());

        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTodontWithPatch() throws Exception {
        // Initialize the database
        todontRepository.saveAndFlush(todont);

        int databaseSizeBeforeUpdate = todontRepository.findAll().size();

        // Update the todont using partial update
        Todont partialUpdatedTodont = new Todont();
        partialUpdatedTodont.setId(todont.getId());

        partialUpdatedTodont.description(UPDATED_DESCRIPTION);

        restTodontMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodont.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodont))
            )
            .andExpect(status().isOk());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
        Todont testTodont = todontList.get(todontList.size() - 1);
        assertThat(testTodont.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTodont.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodont.getDue()).isEqualTo(DEFAULT_DUE);
    }

    @Test
    @Transactional
    void fullUpdateTodontWithPatch() throws Exception {
        // Initialize the database
        todontRepository.saveAndFlush(todont);

        int databaseSizeBeforeUpdate = todontRepository.findAll().size();

        // Update the todont using partial update
        Todont partialUpdatedTodont = new Todont();
        partialUpdatedTodont.setId(todont.getId());

        partialUpdatedTodont.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).due(UPDATED_DUE);

        restTodontMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodont.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodont))
            )
            .andExpect(status().isOk());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
        Todont testTodont = todontList.get(todontList.size() - 1);
        assertThat(testTodont.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTodont.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTodont.getDue()).isEqualTo(UPDATED_DUE);
    }

    @Test
    @Transactional
    void patchNonExistingTodont() throws Exception {
        int databaseSizeBeforeUpdate = todontRepository.findAll().size();
        todont.setId(count.incrementAndGet());

        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodontMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, todontDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todontDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTodont() throws Exception {
        int databaseSizeBeforeUpdate = todontRepository.findAll().size();
        todont.setId(count.incrementAndGet());

        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todontDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTodont() throws Exception {
        int databaseSizeBeforeUpdate = todontRepository.findAll().size();
        todont.setId(count.incrementAndGet());

        // Create the Todont
        TodontDTO todontDTO = todontMapper.toDto(todont);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(todontDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Todont in the database
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTodont() throws Exception {
        // Initialize the database
        todontRepository.saveAndFlush(todont);

        int databaseSizeBeforeDelete = todontRepository.findAll().size();

        // Delete the todont
        restTodontMockMvc
            .perform(delete(ENTITY_API_URL_ID, todont.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Todont> todontList = todontRepository.findAll();
        assertThat(todontList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
