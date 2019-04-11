package com.jangbee.client_evalu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by test on 2019-03-26.
 */

public interface ClientEvaluRepository extends JpaRepository<ClientEvalu, Long> {
    boolean existsByTelNumber(String telNumber);
    @Query(value="SELECT * FROM client_evalu WHERE update_date between :startDate and :endDate", nativeQuery = true)
    List<ClientEvalu> getNewest(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ClientEvalu> findByCliNameLike(String cliName);

    List<ClientEvalu> findByFirmNameLike(String firmName);

    List<ClientEvalu> findByFirmNumberLike(String firmNumber);

    List<ClientEvalu> findByTelNumberLikeOrTelNumber2LikeOrTelNumber3Like(String telNumber, String telNumber2, String telNumber3);
}
