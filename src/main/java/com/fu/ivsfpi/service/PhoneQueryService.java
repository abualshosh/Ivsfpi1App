package com.fu.ivsfpi.service;

import com.fu.ivsfpi.domain.*; // for static metamodels
import com.fu.ivsfpi.domain.Phone;
import com.fu.ivsfpi.repository.PhoneRepository;
import com.fu.ivsfpi.service.criteria.PhoneCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Phone} entities in the database.
 * The main input is a {@link PhoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Phone} or a {@link Page} of {@link Phone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhoneQueryService extends QueryService<Phone> {

    private final Logger log = LoggerFactory.getLogger(PhoneQueryService.class);

    private final PhoneRepository phoneRepository;

    public PhoneQueryService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * Return a {@link List} of {@link Phone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Phone> findByCriteria(PhoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Phone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Phone> findByCriteria(PhoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PhoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.count(specification);
    }

    /**
     * Function to convert {@link PhoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Phone> createSpecification(PhoneCriteria criteria) {
        Specification<Phone> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Phone_.id));
            }
            if (criteria.getImei() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImei(), Phone_.imei));
            }
            if (criteria.getImei2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImei2(), Phone_.imei2));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), Phone_.brand));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Phone_.model));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), Phone_.color));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Phone_.status));
            }
            if (criteria.getVerifedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVerifedBy(), Phone_.verifedBy));
            }
            if (criteria.getVerifedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVerifedDate(), Phone_.verifedDate));
            }
            if (criteria.getComplainId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getComplainId(), root -> root.join(Phone_.complain, JoinType.LEFT).get(Complain_.id))
                    );
            }
        }
        return specification;
    }
}
