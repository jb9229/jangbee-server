package com.jangbee.client_evalu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-03-26.
 */

public interface ClientEvaluRepository extends JpaRepository<ClientEvalu, Long> {
    boolean existsByTelNumber(String telNumber);
    @Query(value="SELECT ev FROM ClientEvalu ev WHERE ev.updateDate between :startDate and :endDate ORDER BY ev.updateDate desc")
    Page<ClientEvalu> getNewest(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    @Query(value="SELECT cEvalu FROM ClientEvalu cEvalu WHERE cEvalu.accountId = :accountId ORDER BY cEvalu.updateDate desc")
    Page<ClientEvalu> getMy(@Param("accountId") String accountId, Pageable pageable);

    List<ClientEvalu> findByCliNameLike(String cliName);

    List<ClientEvalu> findByFirmNameLike(String firmName);

    List<ClientEvalu> findByFirmNumberLike(String firmNumber);

    List<ClientEvalu> findByTelNumberLikeOrTelNumber2LikeOrTelNumber3Like(String telNumber, String telNumber2, String telNumber3);

    @Modifying
    @Transactional
    Long deleteByAccountId(String accountId);
}
