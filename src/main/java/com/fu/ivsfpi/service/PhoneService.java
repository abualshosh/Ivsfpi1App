package com.fu.ivsfpi.service;

import com.fu.ivsfpi.domain.Phone;
import com.fu.ivsfpi.repository.PhoneRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Phone}.
 */
@Service
@Transactional
public class PhoneService {

    private final Logger log = LoggerFactory.getLogger(PhoneService.class);

    private final PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * Save a phone.
     *
     * @param phone the entity to save.
     * @return the persisted entity.
     */
    public Phone save(Phone phone) {
        log.debug("Request to save Phone : {}", phone);
        return phoneRepository.save(phone);
    }

    public List<Phone> saveAll(Set<Phone> phones) {
        log.debug("Request to save Phone : {}", phones);
        return phoneRepository.saveAll(phones);
    }

    /**
     * Partially update a phone.
     *
     * @param phone the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Phone> partialUpdate(Phone phone) {
        log.debug("Request to partially update Phone : {}", phone);

        return phoneRepository
            .findById(phone.getId())
            .map(existingPhone -> {
                if (phone.getImei() != null) {
                    existingPhone.setImei(phone.getImei());
                }
                if (phone.getImei2() != null) {
                    existingPhone.setImei2(phone.getImei2());
                }
                if (phone.getBrand() != null) {
                    existingPhone.setBrand(phone.getBrand());
                }
                if (phone.getModel() != null) {
                    existingPhone.setModel(phone.getModel());
                }
                if (phone.getColor() != null) {
                    existingPhone.setColor(phone.getColor());
                }
                if (phone.getDescroptions() != null) {
                    existingPhone.setDescroptions(phone.getDescroptions());
                }
                if (phone.getStatus() != null) {
                    existingPhone.setStatus(phone.getStatus());
                }
                if (phone.getVerifedBy() != null) {
                    existingPhone.setVerifedBy(phone.getVerifedBy());
                }
                if (phone.getVerifedDate() != null) {
                    existingPhone.setVerifedDate(phone.getVerifedDate());
                }

                return existingPhone;
            })
            .map(phoneRepository::save);
    }

    /**
     * Get all the phones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Phone> findAll(Pageable pageable) {
        log.debug("Request to get all Phones");
        return phoneRepository.findAll(pageable);
    }

    /**
     * Get one phone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Phone> findOne(Long id) {
        log.debug("Request to get Phone : {}", id);
        return phoneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Phone> findOneByImei(String imei) {
        log.debug("Request to get Phone : {}", imei);
        return phoneRepository.findOneByImeiOrImei2(imei, imei);
    }

    @Transactional(readOnly = true)
    public Set<Phone> findAllByImeiOrImei2(String imei) {
        String imei2 = imei;
        return phoneRepository.findAllByImeiOrImei2(imei, imei2);
    }

    /**
     * Delete the phone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Phone : {}", id);
        phoneRepository.deleteById(id);
    }
}
