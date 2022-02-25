package com.fu.ivsfpi.repository;

import com.fu.ivsfpi.domain.Phone;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Phone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>, JpaSpecificationExecutor<Phone> {
    Optional<Phone> findOneByImeiOrImei2(String imei, String imei2);
    Set<Phone> findAllByImeiOrImei2(String imei, String imei2);
}
