package com.fu.ivsfpi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fu.ivsfpi.IntegrationTest;
import com.fu.ivsfpi.domain.Complain;
import com.fu.ivsfpi.domain.Phone;
import com.fu.ivsfpi.domain.enumeration.PhoneStatus;
import com.fu.ivsfpi.repository.PhoneRepository;
import com.fu.ivsfpi.service.criteria.PhoneCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhoneResourceIT {

    private static final String DEFAULT_IMEI = "AAAAAAAAAA";
    private static final String UPDATED_IMEI = "BBBBBBBBBB";

    private static final String DEFAULT_IMEI_2 = "AAAAAAAAAA";
    private static final String UPDATED_IMEI_2 = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCROPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_DESCROPTIONS = "BBBBBBBBBB";

    private static final PhoneStatus DEFAULT_STATUS = PhoneStatus.STOLEN;
    private static final PhoneStatus UPDATED_STATUS = PhoneStatus.VERVIED;

    private static final String DEFAULT_VERIFED_BY = "AAAAAAAAAA";
    private static final String UPDATED_VERIFED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_VERIFED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERIFED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneMockMvc;

    private Phone phone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createEntity(EntityManager em) {
        Phone phone = new Phone()
            .imei(DEFAULT_IMEI)
            .imei2(DEFAULT_IMEI_2)
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .color(DEFAULT_COLOR)
            .descroptions(DEFAULT_DESCROPTIONS)
            .status(DEFAULT_STATUS)
            .verifedBy(DEFAULT_VERIFED_BY)
            .verifedDate(DEFAULT_VERIFED_DATE);
        return phone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createUpdatedEntity(EntityManager em) {
        Phone phone = new Phone()
            .imei(UPDATED_IMEI)
            .imei2(UPDATED_IMEI_2)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .color(UPDATED_COLOR)
            .descroptions(UPDATED_DESCROPTIONS)
            .status(UPDATED_STATUS)
            .verifedBy(UPDATED_VERIFED_BY)
            .verifedDate(UPDATED_VERIFED_DATE);
        return phone;
    }

    @BeforeEach
    public void initTest() {
        phone = createEntity(em);
    }

    @Test
    @Transactional
    void createPhone() throws Exception {
        int databaseSizeBeforeCreate = phoneRepository.findAll().size();
        // Create the Phone
        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isCreated());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate + 1);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getImei()).isEqualTo(DEFAULT_IMEI);
        assertThat(testPhone.getImei2()).isEqualTo(DEFAULT_IMEI_2);
        assertThat(testPhone.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testPhone.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testPhone.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testPhone.getDescroptions()).isEqualTo(DEFAULT_DESCROPTIONS);
        assertThat(testPhone.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPhone.getVerifedBy()).isEqualTo(DEFAULT_VERIFED_BY);
        assertThat(testPhone.getVerifedDate()).isEqualTo(DEFAULT_VERIFED_DATE);
    }

    @Test
    @Transactional
    void createPhoneWithExistingId() throws Exception {
        // Create the Phone with an existing ID
        phone.setId(1L);

        int databaseSizeBeforeCreate = phoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImeiIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneRepository.findAll().size();
        // set the field null
        phone.setImei(null);

        // Create the Phone, which fails.

        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneRepository.findAll().size();
        // set the field null
        phone.setBrand(null);

        // Create the Phone, which fails.

        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = phoneRepository.findAll().size();
        // set the field null
        phone.setModel(null);

        // Create the Phone, which fails.

        restPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhones() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].imei").value(hasItem(DEFAULT_IMEI)))
            .andExpect(jsonPath("$.[*].imei2").value(hasItem(DEFAULT_IMEI_2)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].descroptions").value(hasItem(DEFAULT_DESCROPTIONS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].verifedBy").value(hasItem(DEFAULT_VERIFED_BY)))
            .andExpect(jsonPath("$.[*].verifedDate").value(hasItem(DEFAULT_VERIFED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get the phone
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, phone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phone.getId().intValue()))
            .andExpect(jsonPath("$.imei").value(DEFAULT_IMEI))
            .andExpect(jsonPath("$.imei2").value(DEFAULT_IMEI_2))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.descroptions").value(DEFAULT_DESCROPTIONS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.verifedBy").value(DEFAULT_VERIFED_BY))
            .andExpect(jsonPath("$.verifedDate").value(DEFAULT_VERIFED_DATE.toString()));
    }

    @Test
    @Transactional
    void getPhonesByIdFiltering() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        Long id = phone.getId();

        defaultPhoneShouldBeFound("id.equals=" + id);
        defaultPhoneShouldNotBeFound("id.notEquals=" + id);

        defaultPhoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhoneShouldNotBeFound("id.greaterThan=" + id);

        defaultPhoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPhonesByImeiIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei equals to DEFAULT_IMEI
        defaultPhoneShouldBeFound("imei.equals=" + DEFAULT_IMEI);

        // Get all the phoneList where imei equals to UPDATED_IMEI
        defaultPhoneShouldNotBeFound("imei.equals=" + UPDATED_IMEI);
    }

    @Test
    @Transactional
    void getAllPhonesByImeiIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei not equals to DEFAULT_IMEI
        defaultPhoneShouldNotBeFound("imei.notEquals=" + DEFAULT_IMEI);

        // Get all the phoneList where imei not equals to UPDATED_IMEI
        defaultPhoneShouldBeFound("imei.notEquals=" + UPDATED_IMEI);
    }

    @Test
    @Transactional
    void getAllPhonesByImeiIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei in DEFAULT_IMEI or UPDATED_IMEI
        defaultPhoneShouldBeFound("imei.in=" + DEFAULT_IMEI + "," + UPDATED_IMEI);

        // Get all the phoneList where imei equals to UPDATED_IMEI
        defaultPhoneShouldNotBeFound("imei.in=" + UPDATED_IMEI);
    }

    @Test
    @Transactional
    void getAllPhonesByImeiIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei is not null
        defaultPhoneShouldBeFound("imei.specified=true");

        // Get all the phoneList where imei is null
        defaultPhoneShouldNotBeFound("imei.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByImeiContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei contains DEFAULT_IMEI
        defaultPhoneShouldBeFound("imei.contains=" + DEFAULT_IMEI);

        // Get all the phoneList where imei contains UPDATED_IMEI
        defaultPhoneShouldNotBeFound("imei.contains=" + UPDATED_IMEI);
    }

    @Test
    @Transactional
    void getAllPhonesByImeiNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei does not contain DEFAULT_IMEI
        defaultPhoneShouldNotBeFound("imei.doesNotContain=" + DEFAULT_IMEI);

        // Get all the phoneList where imei does not contain UPDATED_IMEI
        defaultPhoneShouldBeFound("imei.doesNotContain=" + UPDATED_IMEI);
    }

    @Test
    @Transactional
    void getAllPhonesByImei2IsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei2 equals to DEFAULT_IMEI_2
        defaultPhoneShouldBeFound("imei2.equals=" + DEFAULT_IMEI_2);

        // Get all the phoneList where imei2 equals to UPDATED_IMEI_2
        defaultPhoneShouldNotBeFound("imei2.equals=" + UPDATED_IMEI_2);
    }

    @Test
    @Transactional
    void getAllPhonesByImei2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei2 not equals to DEFAULT_IMEI_2
        defaultPhoneShouldNotBeFound("imei2.notEquals=" + DEFAULT_IMEI_2);

        // Get all the phoneList where imei2 not equals to UPDATED_IMEI_2
        defaultPhoneShouldBeFound("imei2.notEquals=" + UPDATED_IMEI_2);
    }

    @Test
    @Transactional
    void getAllPhonesByImei2IsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei2 in DEFAULT_IMEI_2 or UPDATED_IMEI_2
        defaultPhoneShouldBeFound("imei2.in=" + DEFAULT_IMEI_2 + "," + UPDATED_IMEI_2);

        // Get all the phoneList where imei2 equals to UPDATED_IMEI_2
        defaultPhoneShouldNotBeFound("imei2.in=" + UPDATED_IMEI_2);
    }

    @Test
    @Transactional
    void getAllPhonesByImei2IsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei2 is not null
        defaultPhoneShouldBeFound("imei2.specified=true");

        // Get all the phoneList where imei2 is null
        defaultPhoneShouldNotBeFound("imei2.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByImei2ContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei2 contains DEFAULT_IMEI_2
        defaultPhoneShouldBeFound("imei2.contains=" + DEFAULT_IMEI_2);

        // Get all the phoneList where imei2 contains UPDATED_IMEI_2
        defaultPhoneShouldNotBeFound("imei2.contains=" + UPDATED_IMEI_2);
    }

    @Test
    @Transactional
    void getAllPhonesByImei2NotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where imei2 does not contain DEFAULT_IMEI_2
        defaultPhoneShouldNotBeFound("imei2.doesNotContain=" + DEFAULT_IMEI_2);

        // Get all the phoneList where imei2 does not contain UPDATED_IMEI_2
        defaultPhoneShouldBeFound("imei2.doesNotContain=" + UPDATED_IMEI_2);
    }

    @Test
    @Transactional
    void getAllPhonesByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where brand equals to DEFAULT_BRAND
        defaultPhoneShouldBeFound("brand.equals=" + DEFAULT_BRAND);

        // Get all the phoneList where brand equals to UPDATED_BRAND
        defaultPhoneShouldNotBeFound("brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllPhonesByBrandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where brand not equals to DEFAULT_BRAND
        defaultPhoneShouldNotBeFound("brand.notEquals=" + DEFAULT_BRAND);

        // Get all the phoneList where brand not equals to UPDATED_BRAND
        defaultPhoneShouldBeFound("brand.notEquals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllPhonesByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where brand in DEFAULT_BRAND or UPDATED_BRAND
        defaultPhoneShouldBeFound("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND);

        // Get all the phoneList where brand equals to UPDATED_BRAND
        defaultPhoneShouldNotBeFound("brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllPhonesByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where brand is not null
        defaultPhoneShouldBeFound("brand.specified=true");

        // Get all the phoneList where brand is null
        defaultPhoneShouldNotBeFound("brand.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByBrandContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where brand contains DEFAULT_BRAND
        defaultPhoneShouldBeFound("brand.contains=" + DEFAULT_BRAND);

        // Get all the phoneList where brand contains UPDATED_BRAND
        defaultPhoneShouldNotBeFound("brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllPhonesByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where brand does not contain DEFAULT_BRAND
        defaultPhoneShouldNotBeFound("brand.doesNotContain=" + DEFAULT_BRAND);

        // Get all the phoneList where brand does not contain UPDATED_BRAND
        defaultPhoneShouldBeFound("brand.doesNotContain=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllPhonesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where model equals to DEFAULT_MODEL
        defaultPhoneShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the phoneList where model equals to UPDATED_MODEL
        defaultPhoneShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllPhonesByModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where model not equals to DEFAULT_MODEL
        defaultPhoneShouldNotBeFound("model.notEquals=" + DEFAULT_MODEL);

        // Get all the phoneList where model not equals to UPDATED_MODEL
        defaultPhoneShouldBeFound("model.notEquals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllPhonesByModelIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultPhoneShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the phoneList where model equals to UPDATED_MODEL
        defaultPhoneShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllPhonesByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where model is not null
        defaultPhoneShouldBeFound("model.specified=true");

        // Get all the phoneList where model is null
        defaultPhoneShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByModelContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where model contains DEFAULT_MODEL
        defaultPhoneShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the phoneList where model contains UPDATED_MODEL
        defaultPhoneShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllPhonesByModelNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where model does not contain DEFAULT_MODEL
        defaultPhoneShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the phoneList where model does not contain UPDATED_MODEL
        defaultPhoneShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllPhonesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where color equals to DEFAULT_COLOR
        defaultPhoneShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the phoneList where color equals to UPDATED_COLOR
        defaultPhoneShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllPhonesByColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where color not equals to DEFAULT_COLOR
        defaultPhoneShouldNotBeFound("color.notEquals=" + DEFAULT_COLOR);

        // Get all the phoneList where color not equals to UPDATED_COLOR
        defaultPhoneShouldBeFound("color.notEquals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllPhonesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultPhoneShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the phoneList where color equals to UPDATED_COLOR
        defaultPhoneShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllPhonesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where color is not null
        defaultPhoneShouldBeFound("color.specified=true");

        // Get all the phoneList where color is null
        defaultPhoneShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByColorContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where color contains DEFAULT_COLOR
        defaultPhoneShouldBeFound("color.contains=" + DEFAULT_COLOR);

        // Get all the phoneList where color contains UPDATED_COLOR
        defaultPhoneShouldNotBeFound("color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllPhonesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where color does not contain DEFAULT_COLOR
        defaultPhoneShouldNotBeFound("color.doesNotContain=" + DEFAULT_COLOR);

        // Get all the phoneList where color does not contain UPDATED_COLOR
        defaultPhoneShouldBeFound("color.doesNotContain=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllPhonesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where status equals to DEFAULT_STATUS
        defaultPhoneShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the phoneList where status equals to UPDATED_STATUS
        defaultPhoneShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPhonesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where status not equals to DEFAULT_STATUS
        defaultPhoneShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the phoneList where status not equals to UPDATED_STATUS
        defaultPhoneShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPhonesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPhoneShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the phoneList where status equals to UPDATED_STATUS
        defaultPhoneShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPhonesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where status is not null
        defaultPhoneShouldBeFound("status.specified=true");

        // Get all the phoneList where status is null
        defaultPhoneShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedByIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedBy equals to DEFAULT_VERIFED_BY
        defaultPhoneShouldBeFound("verifedBy.equals=" + DEFAULT_VERIFED_BY);

        // Get all the phoneList where verifedBy equals to UPDATED_VERIFED_BY
        defaultPhoneShouldNotBeFound("verifedBy.equals=" + UPDATED_VERIFED_BY);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedBy not equals to DEFAULT_VERIFED_BY
        defaultPhoneShouldNotBeFound("verifedBy.notEquals=" + DEFAULT_VERIFED_BY);

        // Get all the phoneList where verifedBy not equals to UPDATED_VERIFED_BY
        defaultPhoneShouldBeFound("verifedBy.notEquals=" + UPDATED_VERIFED_BY);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedByIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedBy in DEFAULT_VERIFED_BY or UPDATED_VERIFED_BY
        defaultPhoneShouldBeFound("verifedBy.in=" + DEFAULT_VERIFED_BY + "," + UPDATED_VERIFED_BY);

        // Get all the phoneList where verifedBy equals to UPDATED_VERIFED_BY
        defaultPhoneShouldNotBeFound("verifedBy.in=" + UPDATED_VERIFED_BY);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedBy is not null
        defaultPhoneShouldBeFound("verifedBy.specified=true");

        // Get all the phoneList where verifedBy is null
        defaultPhoneShouldNotBeFound("verifedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedByContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedBy contains DEFAULT_VERIFED_BY
        defaultPhoneShouldBeFound("verifedBy.contains=" + DEFAULT_VERIFED_BY);

        // Get all the phoneList where verifedBy contains UPDATED_VERIFED_BY
        defaultPhoneShouldNotBeFound("verifedBy.contains=" + UPDATED_VERIFED_BY);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedByNotContainsSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedBy does not contain DEFAULT_VERIFED_BY
        defaultPhoneShouldNotBeFound("verifedBy.doesNotContain=" + DEFAULT_VERIFED_BY);

        // Get all the phoneList where verifedBy does not contain UPDATED_VERIFED_BY
        defaultPhoneShouldBeFound("verifedBy.doesNotContain=" + UPDATED_VERIFED_BY);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedDate equals to DEFAULT_VERIFED_DATE
        defaultPhoneShouldBeFound("verifedDate.equals=" + DEFAULT_VERIFED_DATE);

        // Get all the phoneList where verifedDate equals to UPDATED_VERIFED_DATE
        defaultPhoneShouldNotBeFound("verifedDate.equals=" + UPDATED_VERIFED_DATE);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedDate not equals to DEFAULT_VERIFED_DATE
        defaultPhoneShouldNotBeFound("verifedDate.notEquals=" + DEFAULT_VERIFED_DATE);

        // Get all the phoneList where verifedDate not equals to UPDATED_VERIFED_DATE
        defaultPhoneShouldBeFound("verifedDate.notEquals=" + UPDATED_VERIFED_DATE);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedDateIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedDate in DEFAULT_VERIFED_DATE or UPDATED_VERIFED_DATE
        defaultPhoneShouldBeFound("verifedDate.in=" + DEFAULT_VERIFED_DATE + "," + UPDATED_VERIFED_DATE);

        // Get all the phoneList where verifedDate equals to UPDATED_VERIFED_DATE
        defaultPhoneShouldNotBeFound("verifedDate.in=" + UPDATED_VERIFED_DATE);
    }

    @Test
    @Transactional
    void getAllPhonesByVerifedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where verifedDate is not null
        defaultPhoneShouldBeFound("verifedDate.specified=true");

        // Get all the phoneList where verifedDate is null
        defaultPhoneShouldNotBeFound("verifedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPhonesByComplainIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);
        Complain complain;
        if (TestUtil.findAll(em, Complain.class).isEmpty()) {
            complain = ComplainResourceIT.createEntity(em);
            em.persist(complain);
            em.flush();
        } else {
            complain = TestUtil.findAll(em, Complain.class).get(0);
        }
        em.persist(complain);
        em.flush();
        phone.setComplain(complain);
        phoneRepository.saveAndFlush(phone);
        Long complainId = complain.getId();

        // Get all the phoneList where complain equals to complainId
        defaultPhoneShouldBeFound("complainId.equals=" + complainId);

        // Get all the phoneList where complain equals to (complainId + 1)
        defaultPhoneShouldNotBeFound("complainId.equals=" + (complainId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhoneShouldBeFound(String filter) throws Exception {
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].imei").value(hasItem(DEFAULT_IMEI)))
            .andExpect(jsonPath("$.[*].imei2").value(hasItem(DEFAULT_IMEI_2)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].descroptions").value(hasItem(DEFAULT_DESCROPTIONS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].verifedBy").value(hasItem(DEFAULT_VERIFED_BY)))
            .andExpect(jsonPath("$.[*].verifedDate").value(hasItem(DEFAULT_VERIFED_DATE.toString())));

        // Check, that the count call also returns 1
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhoneShouldNotBeFound(String filter) throws Exception {
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPhone() throws Exception {
        // Get the phone
        restPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone
        Phone updatedPhone = phoneRepository.findById(phone.getId()).get();
        // Disconnect from session so that the updates on updatedPhone are not directly saved in db
        em.detach(updatedPhone);
        updatedPhone
            .imei(UPDATED_IMEI)
            .imei2(UPDATED_IMEI_2)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .color(UPDATED_COLOR)
            .descroptions(UPDATED_DESCROPTIONS)
            .status(UPDATED_STATUS)
            .verifedBy(UPDATED_VERIFED_BY)
            .verifedDate(UPDATED_VERIFED_DATE);

        restPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhone))
            )
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getImei()).isEqualTo(UPDATED_IMEI);
        assertThat(testPhone.getImei2()).isEqualTo(UPDATED_IMEI_2);
        assertThat(testPhone.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testPhone.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testPhone.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testPhone.getDescroptions()).isEqualTo(UPDATED_DESCROPTIONS);
        assertThat(testPhone.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPhone.getVerifedBy()).isEqualTo(UPDATED_VERIFED_BY);
        assertThat(testPhone.getVerifedDate()).isEqualTo(UPDATED_VERIFED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhoneWithPatch() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone using partial update
        Phone partialUpdatedPhone = new Phone();
        partialUpdatedPhone.setId(phone.getId());

        partialUpdatedPhone.color(UPDATED_COLOR).descroptions(UPDATED_DESCROPTIONS).verifedDate(UPDATED_VERIFED_DATE);

        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhone))
            )
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getImei()).isEqualTo(DEFAULT_IMEI);
        assertThat(testPhone.getImei2()).isEqualTo(DEFAULT_IMEI_2);
        assertThat(testPhone.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testPhone.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testPhone.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testPhone.getDescroptions()).isEqualTo(UPDATED_DESCROPTIONS);
        assertThat(testPhone.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPhone.getVerifedBy()).isEqualTo(DEFAULT_VERIFED_BY);
        assertThat(testPhone.getVerifedDate()).isEqualTo(UPDATED_VERIFED_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePhoneWithPatch() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone using partial update
        Phone partialUpdatedPhone = new Phone();
        partialUpdatedPhone.setId(phone.getId());

        partialUpdatedPhone
            .imei(UPDATED_IMEI)
            .imei2(UPDATED_IMEI_2)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .color(UPDATED_COLOR)
            .descroptions(UPDATED_DESCROPTIONS)
            .status(UPDATED_STATUS)
            .verifedBy(UPDATED_VERIFED_BY)
            .verifedDate(UPDATED_VERIFED_DATE);

        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhone))
            )
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getImei()).isEqualTo(UPDATED_IMEI);
        assertThat(testPhone.getImei2()).isEqualTo(UPDATED_IMEI_2);
        assertThat(testPhone.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testPhone.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testPhone.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testPhone.getDescroptions()).isEqualTo(UPDATED_DESCROPTIONS);
        assertThat(testPhone.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPhone.getVerifedBy()).isEqualTo(UPDATED_VERIFED_BY);
        assertThat(testPhone.getVerifedDate()).isEqualTo(UPDATED_VERIFED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();
        phone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        int databaseSizeBeforeDelete = phoneRepository.findAll().size();

        // Delete the phone
        restPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, phone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
