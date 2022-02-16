package com.fu.ivsfpi.web.rest;

import com.fu.ivsfpi.domain.Mercahnt;
import com.fu.ivsfpi.domain.User;
import com.fu.ivsfpi.repository.MercahntRepository;
import com.fu.ivsfpi.service.MercahntQueryService;
import com.fu.ivsfpi.service.MercahntService;
import com.fu.ivsfpi.service.UserService;
import com.fu.ivsfpi.service.criteria.MercahntCriteria;
import com.fu.ivsfpi.service.dto.AdminUserDTO;
import com.fu.ivsfpi.service.dto.MerchantUserDTO;
import com.fu.ivsfpi.service.dto.UserDTO;
import com.fu.ivsfpi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.fu.ivsfpi.domain.Mercahnt}.
 */
@RestController
@RequestMapping("/api")
public class MercahntResource {

    private final Logger log = LoggerFactory.getLogger(MercahntResource.class);

    private static final String ENTITY_NAME = "mercahnt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MercahntService mercahntService;

    private final MercahntRepository mercahntRepository;

    private final MercahntQueryService mercahntQueryService;

    private final UserService userService;

    public MercahntResource(
        MercahntService mercahntService,
        MercahntRepository mercahntRepository,
        MercahntQueryService mercahntQueryService,
        UserService userService
    ) {
        this.mercahntService = mercahntService;
        this.mercahntRepository = mercahntRepository;
        this.mercahntQueryService = mercahntQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /mercahnts} : Create a new mercahnt.
     *
     * @param mercahnt the mercahnt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mercahnt, or with status {@code 400 (Bad Request)} if the mercahnt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mercahnts")
    public ResponseEntity<Mercahnt> createMercahnt(@RequestBody Mercahnt mercahnt) throws URISyntaxException {
        log.debug("REST request to save Mercahnt : {}", mercahnt);
        if (mercahnt.getId() != null) {
            throw new BadRequestAlertException("A new mercahnt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(mercahnt.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        Mercahnt result = mercahntService.save(mercahnt);
        return ResponseEntity
            .created(new URI("/api/mercahnts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mercahnts/:id} : Updates an existing mercahnt.
     *
     * @param id the id of the mercahnt to save.
     * @param mercahnt the mercahnt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mercahnt,
     * or with status {@code 400 (Bad Request)} if the mercahnt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mercahnt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mercahnts/{id}")
    public ResponseEntity<Mercahnt> updateMercahnt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mercahnt mercahnt
    ) throws URISyntaxException {
        log.debug("REST request to update Mercahnt : {}, {}", id, mercahnt);
        if (mercahnt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mercahnt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mercahntRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mercahnt result = mercahntService.save(mercahnt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mercahnt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mercahnts/:id} : Partial updates given fields of an existing mercahnt, field will ignore if it is null
     *
     * @param id the id of the mercahnt to save.
     * @param mercahnt the mercahnt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mercahnt,
     * or with status {@code 400 (Bad Request)} if the mercahnt is not valid,
     * or with status {@code 404 (Not Found)} if the mercahnt is not found,
     * or with status {@code 500 (Internal Server Error)} if the mercahnt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mercahnts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mercahnt> partialUpdateMercahnt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mercahnt mercahnt
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mercahnt partially : {}, {}", id, mercahnt);
        if (mercahnt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mercahnt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mercahntRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mercahnt> result = mercahntService.partialUpdate(mercahnt);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mercahnt.getId().toString())
        );
    }

    /**
     * {@code GET  /mercahnts} : get all the mercahnts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mercahnts in body.
     */
    @GetMapping("/mercahnts")
    public ResponseEntity<List<Mercahnt>> getAllMercahnts(
        MercahntCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Mercahnts by criteria: {}", criteria);
        Page<Mercahnt> page = mercahntQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mercahnts/count} : count all the mercahnts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mercahnts/count")
    public ResponseEntity<Long> countMercahnts(MercahntCriteria criteria) {
        log.debug("REST request to count Mercahnts by criteria: {}", criteria);
        return ResponseEntity.ok().body(mercahntQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mercahnts/:id} : get the "id" mercahnt.
     *
     * @param id the id of the mercahnt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mercahnt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mercahnts/{id}")
    public ResponseEntity<Mercahnt> getMercahnt(@PathVariable Long id) {
        log.debug("REST request to get Mercahnt : {}", id);
        Optional<Mercahnt> mercahnt = mercahntService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mercahnt);
    }

    /**
     * {@code DELETE  /mercahnts/:id} : delete the "id" mercahnt.
     *
     * @param id the id of the mercahnt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mercahnts/{id}")
    public ResponseEntity<Void> deleteMercahnt(@PathVariable Long id) {
        log.debug("REST request to delete Mercahnt : {}", id);
        mercahntService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/merchant/user")
    public ResponseEntity<Mercahnt> createFullMerchant(@RequestBody MerchantUserDTO merchantUserDTO) throws URISyntaxException {
        System.out.println("====================================" + merchantUserDTO.toString() + "=====================================");
        Optional<AdminUserDTO> adminUserDTO = Optional.of(new AdminUserDTO());
        Optional<User> newUser;
        Mercahnt result = new Mercahnt();

        if (merchantUserDTO == null) {
            throw new BadRequestAlertException("cant be empty", "jkfgfjg", "kdhgjkdg");
        } else {
            adminUserDTO.get().setLogin(merchantUserDTO.getLogin());
            adminUserDTO.get().setEmail(merchantUserDTO.getEmail());
            adminUserDTO.get().setFirstName(merchantUserDTO.getFirstName());
            adminUserDTO.get().setLastName(merchantUserDTO.getLastName());
            adminUserDTO.get().setActivated(true);
            adminUserDTO.get().setCreatedDate(Instant.now());
            adminUserDTO.get().setAuthorities(merchantUserDTO.getAuthorities());
            newUser = Optional.ofNullable(userService.registerUser(adminUserDTO.get(), "1234"));

            if (newUser.isPresent()) {
                result.setUser(newUser.get());
                result.setName(newUser.get().getFirstName() + " " + newUser.get().getLastName());
                result.setAddress(merchantUserDTO.getAddress());
                result.setPhoneNumber(merchantUserDTO.getPhoneNumber());
                result = mercahntService.save(result);
            }
        }

        return ResponseEntity
            .created(new URI("/api/mercahnts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
