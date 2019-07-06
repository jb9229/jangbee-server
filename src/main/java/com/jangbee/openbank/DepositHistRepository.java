package com.jangbee.openbank;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by test on 2019-07-05.
 */
public interface DepositHistRepository extends JpaRepository<DepositHistory, Long> {
}
