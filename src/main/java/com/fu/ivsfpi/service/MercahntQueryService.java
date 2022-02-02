package com.fu.ivsfpi.service;

import com.fu.ivsfpi.domain.*; // for static metamodels
import com.fu.ivsfpi.domain.Mercahnt;
import com.fu.ivsfpi.repository.MercahntRepository;
import com.fu.ivsfpi.service.criteria.MercahntCriteria;
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
 * Service for executing complex queries for {@link Mercahnt} entities in the database.
 * The main input is a {@link MercahntCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Mercahnt} or a {@link Page} of {@link Mercahnt} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MercahntQueryService extends QueryService<Mercahnt> {

    private final Logger log = LoggerFactory.getLogger(MercahntQueryService.class);

    private final MercahntRepository mercahntRepository;

    public MercahntQueryService(MercahntRepository mercahntRepository) {
        this.mercahntRepository = mercahntRepository;
    }

    /**
     * Return a {@link List} of {@link Mercahnt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Mercahnt> findByCriteria(MercahntCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mercahnt> specification = createSpecification(criteria);
        return mercahntRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Mercahnt} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Mercahnt> findByCriteria(MercahntCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mercahnt> specification = createSpecification(criteria);
        return mercahntRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MercahntCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mercahnt> specification = createSpecification(criteria);
        return mercahntRepository.count(specification);
    }

    /**
     * Function to convert {@link MercahntCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mercahnt> createSpecification(MercahntCriteria criteria) {
        Specification<Mercahnt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mercahnt_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Mercahnt_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Mercahnt_.address));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Mercahnt_.phoneNumber));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Mercahnt_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
