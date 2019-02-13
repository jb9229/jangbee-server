package com.jangbee.firm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by test on 2019-01-20.
 */
public interface FirmRepository extends JpaRepository<Firm, Long> {
    Firm findByAccountId(String accountId);
}
