package com.fu.ivsfpi.web.rest;

import com.fu.ivsfpi.domain.Phone;
import com.fu.ivsfpi.repository.PhoneRepository;
import com.fu.ivsfpi.service.PhoneQueryService;
import com.fu.ivsfpi.service.PhoneService;
import com.fu.ivsfpi.service.criteria.PhoneCriteria;
import com.fu.ivsfpi.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fu.ivsfpi.domain.Phone}.
 */
@RestController
@RequestMapping("/api")
public class PhoneResource {

    private final Logger log = LoggerFactory.getLogger(PhoneResource.class);

    private static final String ENTITY_NAME = "phone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhoneService phoneService;

    private final PhoneRepository phoneRepository;

    private final PhoneQueryService phoneQueryService;

    public PhoneResource(PhoneService phoneService, PhoneRepository phoneRepository, PhoneQueryService phoneQueryService) {
        this.phoneService = phoneService;
        this.phoneRepository = phoneRepository;
        this.phoneQueryService = phoneQueryService;
    }

    /**
     * {@code POST  /phones} : Create a new phone.
     *
     * @param phone the phone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phone, or with status {@code 400 (Bad Request)} if the phone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phones")
    public ResponseEntity<Phone> createPhone(@Valid @RequestBody Phone phone) throws URISyntaxException {
        log.debug("REST request to save Phone : {}", phone);
        if (phone.getId() != null) {
            throw new BadRequestAlertException("A new phone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Phone result = phoneService.save(phone);
        return ResponseEntity
            .created(new URI("/api/phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phones/:id} : Updates an existing phone.
     *
     * @param id the id of the phone to save.
     * @param phone the phone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phone,
     * or with status {@code 400 (Bad Request)} if the phone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phones/{id}")
    public ResponseEntity<Phone> updatePhone(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Phone phone)
        throws URISyntaxException {
        log.debug("REST request to update Phone : {}, {}", id, phone);
        if (phone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Phone result = phoneService.save(phone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phone.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phones/:id} : Partial updates given fields of an existing phone, field will ignore if it is null
     *
     * @param id the id of the phone to save.
     * @param phone the phone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phone,
     * or with status {@code 400 (Bad Request)} if the phone is not valid,
     * or with status {@code 404 (Not Found)} if the phone is not found,
     * or with status {@code 500 (Internal Server Error)} if the phone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Phone> partialUpdatePhone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Phone phone
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phone partially : {}, {}", id, phone);
        if (phone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Phone> result = phoneService.partialUpdate(phone);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phone.getId().toString())
        );
    }

    /**
     * {@code GET  /phones} : get all the phones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phones in body.
     */
    @GetMapping("/phones")
    public ResponseEntity<List<Phone>> getAllPhones(
        PhoneCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Phones by criteria: {}", criteria);
        Page<Phone> page = phoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /phones/count} : count all the phones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/phones/count")
    public ResponseEntity<Long> countPhones(PhoneCriteria criteria) {
        log.debug("REST request to count Phones by criteria: {}", criteria);
        return ResponseEntity.ok().body(phoneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /phones/:id} : get the "id" phone.
     *
     * @param id the id of the phone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phones/{id}")
    public ResponseEntity<Phone> getPhone(@PathVariable Long id) {
        log.debug("REST request to get Phone : {}", id);
        Optional<Phone> phone = phoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phone);
    }

    /**
     * {@code DELETE  /phones/:id} : delete the "id" phone.
     *
     * @param id the id of the phone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phones/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
        log.debug("REST request to delete Phone : {}", id);
        phoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/phone/{imei}")
    public ResponseEntity<Phone> getPhoneByImei(@PathVariable String imei) {
        log.debug("Request to get phone by Imei :{}", imei);
        Optional<Phone> phone = phoneService.findOneByImei(imei);
        return ResponseUtil.wrapOrNotFound(phone);
    }
}
