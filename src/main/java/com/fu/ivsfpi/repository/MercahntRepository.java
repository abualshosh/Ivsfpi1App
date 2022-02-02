package com.fu.ivsfpi.repository;

import com.fu.ivsfpi.domain.Mercahnt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mercahnt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MercahntRepository extends JpaRepository<Mercahnt, Long>, JpaSpecificationExecutor<Mercahnt> {}
