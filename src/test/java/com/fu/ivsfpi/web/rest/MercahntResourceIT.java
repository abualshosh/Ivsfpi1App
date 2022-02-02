package com.fu.ivsfpi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fu.ivsfpi.IntegrationTest;
import com.fu.ivsfpi.domain.Mercahnt;
import com.fu.ivsfpi.domain.User;
import com.fu.ivsfpi.repository.MercahntRepository;
import com.fu.ivsfpi.service.criteria.MercahntCriteria;
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
 * Integration tests for the {@link MercahntResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MercahntResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mercahnts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MercahntRepository mercahntRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMercahntMockMvc;

    private Mercahnt mercahnt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mercahnt createEntity(EntityManager em) {
        Mercahnt mercahnt = new Mercahnt().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).phoneNumber(DEFAULT_PHONE_NUMBER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        mercahnt.setUser(user);
        return mercahnt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mercahnt createUpdatedEntity(EntityManager em) {
        Mercahnt mercahnt = new Mercahnt().name(UPDATED_NAME).address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        mercahnt.setUser(user);
        return mercahnt;
    }

    @BeforeEach
    public void initTest() {
        mercahnt = createEntity(em);
    }

    @Test
    @Transactional
    void createMercahnt() throws Exception {
        int databaseSizeBeforeCreate = mercahntRepository.findAll().size();
        // Create the Mercahnt
        restMercahntMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mercahnt)))
            .andExpect(status().isCreated());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeCreate + 1);
        Mercahnt testMercahnt = mercahntList.get(mercahntList.size() - 1);
        assertThat(testMercahnt.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMercahnt.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMercahnt.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);

        // Validate the id for MapsId, the ids must be same
        assertThat(testMercahnt.getId()).isEqualTo(testMercahnt.getUser().getId());
    }

    @Test
    @Transactional
    void createMercahntWithExistingId() throws Exception {
        // Create the Mercahnt with an existing ID
        mercahnt.setId(1L);

        int databaseSizeBeforeCreate = mercahntRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMercahntMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mercahnt)))
            .andExpect(status().isBadRequest());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateMercahntMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);
        int databaseSizeBeforeCreate = mercahntRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the mercahnt
        Mercahnt updatedMercahnt = mercahntRepository.findById(mercahnt.getId()).get();
        assertThat(updatedMercahnt).isNotNull();
        // Disconnect from session so that the updates on updatedMercahnt are not directly saved in db
        em.detach(updatedMercahnt);

        // Update the User with new association value
        updatedMercahnt.setUser(user);

        // Update the entity
        restMercahntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMercahnt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMercahnt))
            )
            .andExpect(status().isOk());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeCreate);
        Mercahnt testMercahnt = mercahntList.get(mercahntList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testMercahnt.getId()).isEqualTo(testMercahnt.getUser().getId());
    }

    @Test
    @Transactional
    void getAllMercahnts() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList
        restMercahntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mercahnt.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getMercahnt() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get the mercahnt
        restMercahntMockMvc
            .perform(get(ENTITY_API_URL_ID, mercahnt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mercahnt.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getMercahntsByIdFiltering() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        Long id = mercahnt.getId();

        defaultMercahntShouldBeFound("id.equals=" + id);
        defaultMercahntShouldNotBeFound("id.notEquals=" + id);

        defaultMercahntShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMercahntShouldNotBeFound("id.greaterThan=" + id);

        defaultMercahntShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMercahntShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMercahntsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where name equals to DEFAULT_NAME
        defaultMercahntShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the mercahntList where name equals to UPDATED_NAME
        defaultMercahntShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMercahntsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where name not equals to DEFAULT_NAME
        defaultMercahntShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the mercahntList where name not equals to UPDATED_NAME
        defaultMercahntShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMercahntsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMercahntShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the mercahntList where name equals to UPDATED_NAME
        defaultMercahntShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMercahntsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where name is not null
        defaultMercahntShouldBeFound("name.specified=true");

        // Get all the mercahntList where name is null
        defaultMercahntShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMercahntsByNameContainsSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where name contains DEFAULT_NAME
        defaultMercahntShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the mercahntList where name contains UPDATED_NAME
        defaultMercahntShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMercahntsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where name does not contain DEFAULT_NAME
        defaultMercahntShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the mercahntList where name does not contain UPDATED_NAME
        defaultMercahntShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMercahntsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where address equals to DEFAULT_ADDRESS
        defaultMercahntShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the mercahntList where address equals to UPDATED_ADDRESS
        defaultMercahntShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMercahntsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where address not equals to DEFAULT_ADDRESS
        defaultMercahntShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the mercahntList where address not equals to UPDATED_ADDRESS
        defaultMercahntShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMercahntsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultMercahntShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the mercahntList where address equals to UPDATED_ADDRESS
        defaultMercahntShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMercahntsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where address is not null
        defaultMercahntShouldBeFound("address.specified=true");

        // Get all the mercahntList where address is null
        defaultMercahntShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllMercahntsByAddressContainsSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where address contains DEFAULT_ADDRESS
        defaultMercahntShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the mercahntList where address contains UPDATED_ADDRESS
        defaultMercahntShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMercahntsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where address does not contain DEFAULT_ADDRESS
        defaultMercahntShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the mercahntList where address does not contain UPDATED_ADDRESS
        defaultMercahntShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMercahntsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultMercahntShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the mercahntList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultMercahntShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMercahntsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultMercahntShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the mercahntList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultMercahntShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMercahntsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultMercahntShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the mercahntList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultMercahntShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMercahntsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where phoneNumber is not null
        defaultMercahntShouldBeFound("phoneNumber.specified=true");

        // Get all the mercahntList where phoneNumber is null
        defaultMercahntShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMercahntsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultMercahntShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the mercahntList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultMercahntShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMercahntsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        // Get all the mercahntList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultMercahntShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the mercahntList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultMercahntShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMercahntsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = mercahnt.getUser();
        mercahntRepository.saveAndFlush(mercahnt);
        Long userId = user.getId();

        // Get all the mercahntList where user equals to userId
        defaultMercahntShouldBeFound("userId.equals=" + userId);

        // Get all the mercahntList where user equals to (userId + 1)
        defaultMercahntShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMercahntShouldBeFound(String filter) throws Exception {
        restMercahntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mercahnt.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));

        // Check, that the count call also returns 1
        restMercahntMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMercahntShouldNotBeFound(String filter) throws Exception {
        restMercahntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMercahntMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMercahnt() throws Exception {
        // Get the mercahnt
        restMercahntMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMercahnt() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();

        // Update the mercahnt
        Mercahnt updatedMercahnt = mercahntRepository.findById(mercahnt.getId()).get();
        // Disconnect from session so that the updates on updatedMercahnt are not directly saved in db
        em.detach(updatedMercahnt);
        updatedMercahnt.name(UPDATED_NAME).address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER);

        restMercahntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMercahnt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMercahnt))
            )
            .andExpect(status().isOk());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
        Mercahnt testMercahnt = mercahntList.get(mercahntList.size() - 1);
        assertThat(testMercahnt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMercahnt.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMercahnt.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingMercahnt() throws Exception {
        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();
        mercahnt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMercahntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mercahnt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mercahnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMercahnt() throws Exception {
        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();
        mercahnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMercahntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mercahnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMercahnt() throws Exception {
        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();
        mercahnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMercahntMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mercahnt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMercahntWithPatch() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();

        // Update the mercahnt using partial update
        Mercahnt partialUpdatedMercahnt = new Mercahnt();
        partialUpdatedMercahnt.setId(mercahnt.getId());

        partialUpdatedMercahnt.name(UPDATED_NAME).address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER);

        restMercahntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMercahnt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMercahnt))
            )
            .andExpect(status().isOk());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
        Mercahnt testMercahnt = mercahntList.get(mercahntList.size() - 1);
        assertThat(testMercahnt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMercahnt.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMercahnt.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateMercahntWithPatch() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();

        // Update the mercahnt using partial update
        Mercahnt partialUpdatedMercahnt = new Mercahnt();
        partialUpdatedMercahnt.setId(mercahnt.getId());

        partialUpdatedMercahnt.name(UPDATED_NAME).address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER);

        restMercahntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMercahnt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMercahnt))
            )
            .andExpect(status().isOk());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
        Mercahnt testMercahnt = mercahntList.get(mercahntList.size() - 1);
        assertThat(testMercahnt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMercahnt.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMercahnt.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingMercahnt() throws Exception {
        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();
        mercahnt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMercahntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mercahnt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mercahnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMercahnt() throws Exception {
        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();
        mercahnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMercahntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mercahnt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMercahnt() throws Exception {
        int databaseSizeBeforeUpdate = mercahntRepository.findAll().size();
        mercahnt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMercahntMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mercahnt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mercahnt in the database
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMercahnt() throws Exception {
        // Initialize the database
        mercahntRepository.saveAndFlush(mercahnt);

        int databaseSizeBeforeDelete = mercahntRepository.findAll().size();

        // Delete the mercahnt
        restMercahntMockMvc
            .perform(delete(ENTITY_API_URL_ID, mercahnt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mercahnt> mercahntList = mercahntRepository.findAll();
        assertThat(mercahntList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
