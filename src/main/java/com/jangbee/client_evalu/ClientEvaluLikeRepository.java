package com.jangbee.client_evalu;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by test on 2019-03-28.
 */
public interface ClientEvaluLikeRepository extends JpaRepository<ClientEvaluLike, Long> {
    List<ClientEvaluLike> findByEvaluId(long evaluId);

    @Query(value="SELECT id FROM client_evalu_like e_like WHERE e_like.account_id = :accountId", nativeQuery = true)
    List<Long> findByAccountId(@Param("accountId") String accountId);

    @Modifying
    @Transactional
    Long deleteByAccountId(String accountId);

    boolean existsByAccountIdAndEvaluId(String accountId, Long evaluId);
}
