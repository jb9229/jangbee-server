package com.jangbee.firm_evalu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by test on 2019-04-18.
 */
public interface FirmEvaluRepository extends JpaRepository<FirmEvalu, Long> {
    FirmEvalu findByWorkId(Long workId);

    @Query(value="SELECT fe.rating FROM FirmEvalu fe WHERE fe.firmAccountId = :firmAccountId")
    List<Byte> getRateByFirmAccountId(@Param("firmAccountId") String firmAccountId);

    List<FirmEvalu> findByFirmAccountId(String firmAccountId);
}
