package com.fu.ivsfpi.service;

import com.fu.ivsfpi.domain.Mercahnt;
import com.fu.ivsfpi.repository.MercahntRepository;
import com.fu.ivsfpi.repository.UserRepository;
import com.fu.ivsfpi.security.SecurityUtils;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mercahnt}.
 */
@Service
@Transactional
public class MercahntService {

    private final Logger log = LoggerFactory.getLogger(MercahntService.class);

    private final MercahntRepository mercahntRepository;

    private final UserRepository userRepository;

    public MercahntService(MercahntRepository mercahntRepository, UserRepository userRepository) {
        this.mercahntRepository = mercahntRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a mercahnt.
     *
     * @param mercahnt the entity to save.
     * @return the persisted entity.
     */
    public Mercahnt save(Mercahnt mercahnt) {
        log.debug("Request to save Mercahnt : {}", mercahnt);
        Long userId = mercahnt.getUser().getId();
        userRepository.findById(userId).ifPresent(mercahnt::user);
        return mercahntRepository.save(mercahnt);
    }

    /**
     * Partially update a mercahnt.
     *
     * @param mercahnt the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Mercahnt> partialUpdate(Mercahnt mercahnt) {
        log.debug("Request to partially update Mercahnt : {}", mercahnt);

        return mercahntRepository
            .findById(mercahnt.getId())
            .map(existingMercahnt -> {
                if (mercahnt.getName() != null) {
                    existingMercahnt.setName(mercahnt.getName());
                }
                if (mercahnt.getAddress() != null) {
                    existingMercahnt.setAddress(mercahnt.getAddress());
                }
                if (mercahnt.getPhoneNumber() != null) {
                    existingMercahnt.setPhoneNumber(mercahnt.getPhoneNumber());
                }

                return existingMercahnt;
            })
            .map(mercahntRepository::save);
    }

    /**
     * Get all the mercahnts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Mercahnt> findAll(Pageable pageable) {
        log.debug("Request to get all Mercahnts");
        return mercahntRepository.findAll(pageable);
    }

    /**
     * Get one mercahnt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Mercahnt> findOne(Long id) {
        log.debug("Request to get Mercahnt : {}", id);
        return mercahntRepository.findById(id);
    }

    /**
     * Delete the mercahnt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mercahnt : {}", id);
        mercahntRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Mercahnt> findCurrentMerchant() {
        log.debug("Request to get current Profile");
        return mercahntRepository.findById(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId());
    }
}
