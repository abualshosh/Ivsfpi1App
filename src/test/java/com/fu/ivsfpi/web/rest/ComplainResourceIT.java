package com.fu.ivsfpi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fu.ivsfpi.IntegrationTest;
import com.fu.ivsfpi.domain.Complain;
import com.fu.ivsfpi.domain.Phone;
import com.fu.ivsfpi.domain.enumeration.IdType;
import com.fu.ivsfpi.repository.ComplainRepository;
import com.fu.ivsfpi.service.criteria.ComplainCriteria;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ComplainResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComplainResourceIT {

    private static final UUID DEFAULT_COMPLAIN_NUMBER = UUID.randomUUID();
    private static final UUID UPDATED_COMPLAIN_NUMBER = UUID.randomUUID();

    private static final String DEFAULT_DESCPCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCPCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_ID = "BBBBBBBBBB";

    private static final IdType DEFAULT_ID_TYPE = IdType.DERIVER;
    private static final IdType UPDATED_ID_TYPE = IdType.NATIONAL_NUMBER;

    private static final String ENTITY_API_URL = "/api/complains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComplainRepository complainRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplainMockMvc;

    private Complain complain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complain createEntity(EntityManager em) {
        Complain complain = new Complain()
            .complainNumber(DEFAULT_COMPLAIN_NUMBER)
            .descpcription(DEFAULT_DESCPCRIPTION)
            .ownerName(DEFAULT_OWNER_NAME)
            .ownerPhone(DEFAULT_OWNER_PHONE)
            .ownerID(DEFAULT_OWNER_ID)
            .idType(DEFAULT_ID_TYPE);
        return complain;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complain createUpdatedEntity(EntityManager em) {
        Complain complain = new Complain()
            .complainNumber(UPDATED_COMPLAIN_NUMBER)
            .descpcription(UPDATED_DESCPCRIPTION)
            .ownerName(UPDATED_OWNER_NAME)
            .ownerPhone(UPDATED_OWNER_PHONE)
            .ownerID(UPDATED_OWNER_ID)
            .idType(UPDATED_ID_TYPE);
        return complain;
    }

    @BeforeEach
    public void initTest() {
        complain = createEntity(em);
    }

    @Test
    @Transactional
    void createComplain() throws Exception {
        int databaseSizeBeforeCreate = complainRepository.findAll().size();
        // Create the Complain
        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isCreated());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeCreate + 1);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getComplainNumber()).isEqualTo(DEFAULT_COMPLAIN_NUMBER);
        assertThat(testComplain.getDescpcription()).isEqualTo(DEFAULT_DESCPCRIPTION);
        assertThat(testComplain.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testComplain.getOwnerPhone()).isEqualTo(DEFAULT_OWNER_PHONE);
        assertThat(testComplain.getOwnerID()).isEqualTo(DEFAULT_OWNER_ID);
        assertThat(testComplain.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
    }

    @Test
    @Transactional
    void createComplainWithExistingId() throws Exception {
        // Create the Complain with an existing ID
        complain.setId(1L);

        int databaseSizeBeforeCreate = complainRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOwnerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = complainRepository.findAll().size();
        // set the field null
        complain.setOwnerName(null);

        // Create the Complain, which fails.

        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = complainRepository.findAll().size();
        // set the field null
        complain.setOwnerPhone(null);

        // Create the Complain, which fails.

        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = complainRepository.findAll().size();
        // set the field null
        complain.setOwnerID(null);

        // Create the Complain, which fails.

        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComplains() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complain.getId().intValue())))
            .andExpect(jsonPath("$.[*].complainNumber").value(hasItem(DEFAULT_COMPLAIN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].descpcription").value(hasItem(DEFAULT_DESCPCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].ownerPhone").value(hasItem(DEFAULT_OWNER_PHONE)))
            .andExpect(jsonPath("$.[*].ownerID").value(hasItem(DEFAULT_OWNER_ID)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())));
    }

    @Test
    @Transactional
    void getComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get the complain
        restComplainMockMvc
            .perform(get(ENTITY_API_URL_ID, complain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complain.getId().intValue()))
            .andExpect(jsonPath("$.complainNumber").value(DEFAULT_COMPLAIN_NUMBER.toString()))
            .andExpect(jsonPath("$.descpcription").value(DEFAULT_DESCPCRIPTION.toString()))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME))
            .andExpect(jsonPath("$.ownerPhone").value(DEFAULT_OWNER_PHONE))
            .andExpect(jsonPath("$.ownerID").value(DEFAULT_OWNER_ID))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE.toString()));
    }

    @Test
    @Transactional
    void getComplainsByIdFiltering() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        Long id = complain.getId();

        defaultComplainShouldBeFound("id.equals=" + id);
        defaultComplainShouldNotBeFound("id.notEquals=" + id);

        defaultComplainShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComplainShouldNotBeFound("id.greaterThan=" + id);

        defaultComplainShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComplainShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComplainsByComplainNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where complainNumber equals to DEFAULT_COMPLAIN_NUMBER
        defaultComplainShouldBeFound("complainNumber.equals=" + DEFAULT_COMPLAIN_NUMBER);

        // Get all the complainList where complainNumber equals to UPDATED_COMPLAIN_NUMBER
        defaultComplainShouldNotBeFound("complainNumber.equals=" + UPDATED_COMPLAIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplainsByComplainNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where complainNumber not equals to DEFAULT_COMPLAIN_NUMBER
        defaultComplainShouldNotBeFound("complainNumber.notEquals=" + DEFAULT_COMPLAIN_NUMBER);

        // Get all the complainList where complainNumber not equals to UPDATED_COMPLAIN_NUMBER
        defaultComplainShouldBeFound("complainNumber.notEquals=" + UPDATED_COMPLAIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplainsByComplainNumberIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where complainNumber in DEFAULT_COMPLAIN_NUMBER or UPDATED_COMPLAIN_NUMBER
        defaultComplainShouldBeFound("complainNumber.in=" + DEFAULT_COMPLAIN_NUMBER + "," + UPDATED_COMPLAIN_NUMBER);

        // Get all the complainList where complainNumber equals to UPDATED_COMPLAIN_NUMBER
        defaultComplainShouldNotBeFound("complainNumber.in=" + UPDATED_COMPLAIN_NUMBER);
    }

    @Test
    @Transactional
    void getAllComplainsByComplainNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where complainNumber is not null
        defaultComplainShouldBeFound("complainNumber.specified=true");

        // Get all the complainList where complainNumber is null
        defaultComplainShouldNotBeFound("complainNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerName equals to DEFAULT_OWNER_NAME
        defaultComplainShouldBeFound("ownerName.equals=" + DEFAULT_OWNER_NAME);

        // Get all the complainList where ownerName equals to UPDATED_OWNER_NAME
        defaultComplainShouldNotBeFound("ownerName.equals=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerName not equals to DEFAULT_OWNER_NAME
        defaultComplainShouldNotBeFound("ownerName.notEquals=" + DEFAULT_OWNER_NAME);

        // Get all the complainList where ownerName not equals to UPDATED_OWNER_NAME
        defaultComplainShouldBeFound("ownerName.notEquals=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerNameIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerName in DEFAULT_OWNER_NAME or UPDATED_OWNER_NAME
        defaultComplainShouldBeFound("ownerName.in=" + DEFAULT_OWNER_NAME + "," + UPDATED_OWNER_NAME);

        // Get all the complainList where ownerName equals to UPDATED_OWNER_NAME
        defaultComplainShouldNotBeFound("ownerName.in=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerName is not null
        defaultComplainShouldBeFound("ownerName.specified=true");

        // Get all the complainList where ownerName is null
        defaultComplainShouldNotBeFound("ownerName.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerNameContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerName contains DEFAULT_OWNER_NAME
        defaultComplainShouldBeFound("ownerName.contains=" + DEFAULT_OWNER_NAME);

        // Get all the complainList where ownerName contains UPDATED_OWNER_NAME
        defaultComplainShouldNotBeFound("ownerName.contains=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerNameNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerName does not contain DEFAULT_OWNER_NAME
        defaultComplainShouldNotBeFound("ownerName.doesNotContain=" + DEFAULT_OWNER_NAME);

        // Get all the complainList where ownerName does not contain UPDATED_OWNER_NAME
        defaultComplainShouldBeFound("ownerName.doesNotContain=" + UPDATED_OWNER_NAME);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerPhone equals to DEFAULT_OWNER_PHONE
        defaultComplainShouldBeFound("ownerPhone.equals=" + DEFAULT_OWNER_PHONE);

        // Get all the complainList where ownerPhone equals to UPDATED_OWNER_PHONE
        defaultComplainShouldNotBeFound("ownerPhone.equals=" + UPDATED_OWNER_PHONE);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerPhone not equals to DEFAULT_OWNER_PHONE
        defaultComplainShouldNotBeFound("ownerPhone.notEquals=" + DEFAULT_OWNER_PHONE);

        // Get all the complainList where ownerPhone not equals to UPDATED_OWNER_PHONE
        defaultComplainShouldBeFound("ownerPhone.notEquals=" + UPDATED_OWNER_PHONE);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerPhone in DEFAULT_OWNER_PHONE or UPDATED_OWNER_PHONE
        defaultComplainShouldBeFound("ownerPhone.in=" + DEFAULT_OWNER_PHONE + "," + UPDATED_OWNER_PHONE);

        // Get all the complainList where ownerPhone equals to UPDATED_OWNER_PHONE
        defaultComplainShouldNotBeFound("ownerPhone.in=" + UPDATED_OWNER_PHONE);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerPhone is not null
        defaultComplainShouldBeFound("ownerPhone.specified=true");

        // Get all the complainList where ownerPhone is null
        defaultComplainShouldNotBeFound("ownerPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerPhoneContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerPhone contains DEFAULT_OWNER_PHONE
        defaultComplainShouldBeFound("ownerPhone.contains=" + DEFAULT_OWNER_PHONE);

        // Get all the complainList where ownerPhone contains UPDATED_OWNER_PHONE
        defaultComplainShouldNotBeFound("ownerPhone.contains=" + UPDATED_OWNER_PHONE);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerPhone does not contain DEFAULT_OWNER_PHONE
        defaultComplainShouldNotBeFound("ownerPhone.doesNotContain=" + DEFAULT_OWNER_PHONE);

        // Get all the complainList where ownerPhone does not contain UPDATED_OWNER_PHONE
        defaultComplainShouldBeFound("ownerPhone.doesNotContain=" + UPDATED_OWNER_PHONE);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerIDIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerID equals to DEFAULT_OWNER_ID
        defaultComplainShouldBeFound("ownerID.equals=" + DEFAULT_OWNER_ID);

        // Get all the complainList where ownerID equals to UPDATED_OWNER_ID
        defaultComplainShouldNotBeFound("ownerID.equals=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerID not equals to DEFAULT_OWNER_ID
        defaultComplainShouldNotBeFound("ownerID.notEquals=" + DEFAULT_OWNER_ID);

        // Get all the complainList where ownerID not equals to UPDATED_OWNER_ID
        defaultComplainShouldBeFound("ownerID.notEquals=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerIDIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerID in DEFAULT_OWNER_ID or UPDATED_OWNER_ID
        defaultComplainShouldBeFound("ownerID.in=" + DEFAULT_OWNER_ID + "," + UPDATED_OWNER_ID);

        // Get all the complainList where ownerID equals to UPDATED_OWNER_ID
        defaultComplainShouldNotBeFound("ownerID.in=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerID is not null
        defaultComplainShouldBeFound("ownerID.specified=true");

        // Get all the complainList where ownerID is null
        defaultComplainShouldNotBeFound("ownerID.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerIDContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerID contains DEFAULT_OWNER_ID
        defaultComplainShouldBeFound("ownerID.contains=" + DEFAULT_OWNER_ID);

        // Get all the complainList where ownerID contains UPDATED_OWNER_ID
        defaultComplainShouldNotBeFound("ownerID.contains=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllComplainsByOwnerIDNotContainsSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where ownerID does not contain DEFAULT_OWNER_ID
        defaultComplainShouldNotBeFound("ownerID.doesNotContain=" + DEFAULT_OWNER_ID);

        // Get all the complainList where ownerID does not contain UPDATED_OWNER_ID
        defaultComplainShouldBeFound("ownerID.doesNotContain=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllComplainsByIdTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where idType equals to DEFAULT_ID_TYPE
        defaultComplainShouldBeFound("idType.equals=" + DEFAULT_ID_TYPE);

        // Get all the complainList where idType equals to UPDATED_ID_TYPE
        defaultComplainShouldNotBeFound("idType.equals=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    void getAllComplainsByIdTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where idType not equals to DEFAULT_ID_TYPE
        defaultComplainShouldNotBeFound("idType.notEquals=" + DEFAULT_ID_TYPE);

        // Get all the complainList where idType not equals to UPDATED_ID_TYPE
        defaultComplainShouldBeFound("idType.notEquals=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    void getAllComplainsByIdTypeIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where idType in DEFAULT_ID_TYPE or UPDATED_ID_TYPE
        defaultComplainShouldBeFound("idType.in=" + DEFAULT_ID_TYPE + "," + UPDATED_ID_TYPE);

        // Get all the complainList where idType equals to UPDATED_ID_TYPE
        defaultComplainShouldNotBeFound("idType.in=" + UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    void getAllComplainsByIdTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where idType is not null
        defaultComplainShouldBeFound("idType.specified=true");

        // Get all the complainList where idType is null
        defaultComplainShouldNotBeFound("idType.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Phone phone;
        if (TestUtil.findAll(em, Phone.class).isEmpty()) {
            phone = PhoneResourceIT.createEntity(em);
            em.persist(phone);
            em.flush();
        } else {
            phone = TestUtil.findAll(em, Phone.class).get(0);
        }
        em.persist(phone);
        em.flush();
        complain.addPhone(phone);
        complainRepository.saveAndFlush(complain);
        Long phoneId = phone.getId();

        // Get all the complainList where phone equals to phoneId
        defaultComplainShouldBeFound("phoneId.equals=" + phoneId);

        // Get all the complainList where phone equals to (phoneId + 1)
        defaultComplainShouldNotBeFound("phoneId.equals=" + (phoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComplainShouldBeFound(String filter) throws Exception {
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complain.getId().intValue())))
            .andExpect(jsonPath("$.[*].complainNumber").value(hasItem(DEFAULT_COMPLAIN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].descpcription").value(hasItem(DEFAULT_DESCPCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].ownerPhone").value(hasItem(DEFAULT_OWNER_PHONE)))
            .andExpect(jsonPath("$.[*].ownerID").value(hasItem(DEFAULT_OWNER_ID)))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())));

        // Check, that the count call also returns 1
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComplainShouldNotBeFound(String filter) throws Exception {
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComplain() throws Exception {
        // Get the complain
        restComplainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain
        Complain updatedComplain = complainRepository.findById(complain.getId()).get();
        // Disconnect from session so that the updates on updatedComplain are not directly saved in db
        em.detach(updatedComplain);
        updatedComplain
            .complainNumber(UPDATED_COMPLAIN_NUMBER)
            .descpcription(UPDATED_DESCPCRIPTION)
            .ownerName(UPDATED_OWNER_NAME)
            .ownerPhone(UPDATED_OWNER_PHONE)
            .ownerID(UPDATED_OWNER_ID)
            .idType(UPDATED_ID_TYPE);

        restComplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComplain.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComplain))
            )
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getComplainNumber()).isEqualTo(UPDATED_COMPLAIN_NUMBER);
        assertThat(testComplain.getDescpcription()).isEqualTo(UPDATED_DESCPCRIPTION);
        assertThat(testComplain.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testComplain.getOwnerPhone()).isEqualTo(UPDATED_OWNER_PHONE);
        assertThat(testComplain.getOwnerID()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testComplain.getIdType()).isEqualTo(UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complain.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComplainWithPatch() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain using partial update
        Complain partialUpdatedComplain = new Complain();
        partialUpdatedComplain.setId(complain.getId());

        partialUpdatedComplain.complainNumber(UPDATED_COMPLAIN_NUMBER).descpcription(UPDATED_DESCPCRIPTION).ownerName(UPDATED_OWNER_NAME);

        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplain))
            )
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getComplainNumber()).isEqualTo(UPDATED_COMPLAIN_NUMBER);
        assertThat(testComplain.getDescpcription()).isEqualTo(UPDATED_DESCPCRIPTION);
        assertThat(testComplain.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testComplain.getOwnerPhone()).isEqualTo(DEFAULT_OWNER_PHONE);
        assertThat(testComplain.getOwnerID()).isEqualTo(DEFAULT_OWNER_ID);
        assertThat(testComplain.getIdType()).isEqualTo(DEFAULT_ID_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateComplainWithPatch() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain using partial update
        Complain partialUpdatedComplain = new Complain();
        partialUpdatedComplain.setId(complain.getId());

        partialUpdatedComplain
            .complainNumber(UPDATED_COMPLAIN_NUMBER)
            .descpcription(UPDATED_DESCPCRIPTION)
            .ownerName(UPDATED_OWNER_NAME)
            .ownerPhone(UPDATED_OWNER_PHONE)
            .ownerID(UPDATED_OWNER_ID)
            .idType(UPDATED_ID_TYPE);

        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplain))
            )
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getComplainNumber()).isEqualTo(UPDATED_COMPLAIN_NUMBER);
        assertThat(testComplain.getDescpcription()).isEqualTo(UPDATED_DESCPCRIPTION);
        assertThat(testComplain.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testComplain.getOwnerPhone()).isEqualTo(UPDATED_OWNER_PHONE);
        assertThat(testComplain.getOwnerID()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testComplain.getIdType()).isEqualTo(UPDATED_ID_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, complain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeDelete = complainRepository.findAll().size();

        // Delete the complain
        restComplainMockMvc
            .perform(delete(ENTITY_API_URL_ID, complain.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
