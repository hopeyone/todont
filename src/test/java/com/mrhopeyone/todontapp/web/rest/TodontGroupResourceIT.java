package com.mrhopeyone.todontapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mrhopeyone.todontapp.IntegrationTest;
import com.mrhopeyone.todontapp.domain.TodontGroup;
import com.mrhopeyone.todontapp.repository.TodontGroupRepository;
import com.mrhopeyone.todontapp.service.dto.TodontGroupDTO;
import com.mrhopeyone.todontapp.service.mapper.TodontGroupMapper;
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
 * Integration tests for the {@link TodontGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TodontGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/todont-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TodontGroupRepository todontGroupRepository;

    @Autowired
    private TodontGroupMapper todontGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTodontGroupMockMvc;

    private TodontGroup todontGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodontGroup createEntity(EntityManager em) {
        TodontGroup todontGroup = new TodontGroup().name(DEFAULT_NAME);
        return todontGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TodontGroup createUpdatedEntity(EntityManager em) {
        TodontGroup todontGroup = new TodontGroup().name(UPDATED_NAME);
        return todontGroup;
    }

    @BeforeEach
    public void initTest() {
        todontGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createTodontGroup() throws Exception {
        int databaseSizeBeforeCreate = todontGroupRepository.findAll().size();
        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);
        restTodontGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TodontGroup testTodontGroup = todontGroupList.get(todontGroupList.size() - 1);
        assertThat(testTodontGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTodontGroupWithExistingId() throws Exception {
        // Create the TodontGroup with an existing ID
        todontGroup.setId(1L);
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        int databaseSizeBeforeCreate = todontGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTodontGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = todontGroupRepository.findAll().size();
        // set the field null
        todontGroup.setName(null);

        // Create the TodontGroup, which fails.
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        restTodontGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTodontGroups() throws Exception {
        // Initialize the database
        todontGroupRepository.saveAndFlush(todontGroup);

        // Get all the todontGroupList
        restTodontGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(todontGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTodontGroup() throws Exception {
        // Initialize the database
        todontGroupRepository.saveAndFlush(todontGroup);

        // Get the todontGroup
        restTodontGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, todontGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(todontGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTodontGroup() throws Exception {
        // Get the todontGroup
        restTodontGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTodontGroup() throws Exception {
        // Initialize the database
        todontGroupRepository.saveAndFlush(todontGroup);

        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();

        // Update the todontGroup
        TodontGroup updatedTodontGroup = todontGroupRepository.findById(todontGroup.getId()).get();
        // Disconnect from session so that the updates on updatedTodontGroup are not directly saved in db
        em.detach(updatedTodontGroup);
        updatedTodontGroup.name(UPDATED_NAME);
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(updatedTodontGroup);

        restTodontGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todontGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
        TodontGroup testTodontGroup = todontGroupList.get(todontGroupList.size() - 1);
        assertThat(testTodontGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTodontGroup() throws Exception {
        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();
        todontGroup.setId(count.incrementAndGet());

        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodontGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, todontGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTodontGroup() throws Exception {
        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();
        todontGroup.setId(count.incrementAndGet());

        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTodontGroup() throws Exception {
        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();
        todontGroup.setId(count.incrementAndGet());

        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(todontGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTodontGroupWithPatch() throws Exception {
        // Initialize the database
        todontGroupRepository.saveAndFlush(todontGroup);

        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();

        // Update the todontGroup using partial update
        TodontGroup partialUpdatedTodontGroup = new TodontGroup();
        partialUpdatedTodontGroup.setId(todontGroup.getId());

        partialUpdatedTodontGroup.name(UPDATED_NAME);

        restTodontGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodontGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodontGroup))
            )
            .andExpect(status().isOk());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
        TodontGroup testTodontGroup = todontGroupList.get(todontGroupList.size() - 1);
        assertThat(testTodontGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTodontGroupWithPatch() throws Exception {
        // Initialize the database
        todontGroupRepository.saveAndFlush(todontGroup);

        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();

        // Update the todontGroup using partial update
        TodontGroup partialUpdatedTodontGroup = new TodontGroup();
        partialUpdatedTodontGroup.setId(todontGroup.getId());

        partialUpdatedTodontGroup.name(UPDATED_NAME);

        restTodontGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTodontGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTodontGroup))
            )
            .andExpect(status().isOk());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
        TodontGroup testTodontGroup = todontGroupList.get(todontGroupList.size() - 1);
        assertThat(testTodontGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTodontGroup() throws Exception {
        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();
        todontGroup.setId(count.incrementAndGet());

        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTodontGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, todontGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTodontGroup() throws Exception {
        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();
        todontGroup.setId(count.incrementAndGet());

        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTodontGroup() throws Exception {
        int databaseSizeBeforeUpdate = todontGroupRepository.findAll().size();
        todontGroup.setId(count.incrementAndGet());

        // Create the TodontGroup
        TodontGroupDTO todontGroupDTO = todontGroupMapper.toDto(todontGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTodontGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(todontGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TodontGroup in the database
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTodontGroup() throws Exception {
        // Initialize the database
        todontGroupRepository.saveAndFlush(todontGroup);

        int databaseSizeBeforeDelete = todontGroupRepository.findAll().size();

        // Delete the todontGroup
        restTodontGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, todontGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TodontGroup> todontGroupList = todontGroupRepository.findAll();
        assertThat(todontGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
