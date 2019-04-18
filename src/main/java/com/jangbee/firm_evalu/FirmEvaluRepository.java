package com.jangbee.firm_evalu;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by test on 2019-04-18.
 */
public interface FirmEvaluRepository extends JpaRepository<FirmEvalu, Long> {
    FirmEvalu findByWorkId(Long workId);
}
