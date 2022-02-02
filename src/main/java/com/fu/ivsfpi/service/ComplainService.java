package com.fu.ivsfpi.service;

import com.fu.ivsfpi.domain.Complain;
import com.fu.ivsfpi.repository.ComplainRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Complain}.
 */
@Service
@Transactional
public class ComplainService {

    private final Logger log = LoggerFactory.getLogger(ComplainService.class);

    private final ComplainRepository complainRepository;

    public ComplainService(ComplainRepository complainRepository) {
        this.complainRepository = complainRepository;
    }

    /**
     * Save a complain.
     *
     * @param complain the entity to save.
     * @return the persisted entity.
     */
    public Complain save(Complain complain) {
        log.debug("Request to save Complain : {}", complain);
        return complainRepository.save(complain);
    }

    /**
     * Partially update a complain.
     *
     * @param complain the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Complain> partialUpdate(Complain complain) {
        log.debug("Request to partially update Complain : {}", complain);

        return complainRepository
            .findById(complain.getId())
            .map(existingComplain -> {
                if (complain.getComplainNumber() != null) {
                    existingComplain.setComplainNumber(complain.getComplainNumber());
                }
                if (complain.getDescpcription() != null) {
                    existingComplain.setDescpcription(complain.getDescpcription());
                }
                if (complain.getOwnerName() != null) {
                    existingComplain.setOwnerName(complain.getOwnerName());
                }
                if (complain.getOwnerPhone() != null) {
                    existingComplain.setOwnerPhone(complain.getOwnerPhone());
                }
                if (complain.getOwnerID() != null) {
                    existingComplain.setOwnerID(complain.getOwnerID());
                }
                if (complain.getIdType() != null) {
                    existingComplain.setIdType(complain.getIdType());
                }

                return existingComplain;
            })
            .map(complainRepository::save);
    }

    /**
     * Get all the complains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Complain> findAll(Pageable pageable) {
        log.debug("Request to get all Complains");
        return complainRepository.findAll(pageable);
    }

    /**
     * Get one complain by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Complain> findOne(Long id) {
        log.debug("Request to get Complain : {}", id);
        return complainRepository.findById(id);
    }

    /**
     * Delete the complain by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Complain : {}", id);
        complainRepository.deleteById(id);
    }
}
