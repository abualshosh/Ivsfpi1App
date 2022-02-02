package com.fu.ivsfpi.repository;

import com.fu.ivsfpi.domain.Complain;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Complain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComplainRepository extends JpaRepository<Complain, Long>, JpaSpecificationExecutor<Complain> {}
