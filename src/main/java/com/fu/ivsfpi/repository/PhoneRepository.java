package com.fu.ivsfpi.repository;

import com.fu.ivsfpi.domain.Phone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Phone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>, JpaSpecificationExecutor<Phone> {}
