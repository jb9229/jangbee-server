package com.jangbee.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by test on 2019-04-17.
 */
public interface WorkApplicantRepository extends JpaRepository<WorkApplicant, Long> {
    @Query(value="SELECT wa.workId FROM WorkApplicant wa WHERE wa.accountId = :accountId")
    List<Long> getWorkIdList(@Param("accountId") String accountId);

    long countByWorkId(Long id);

    @Query(value="SELECT wa.accountId FROM WorkApplicant wa WHERE wa.workId = :workId")
    List<String> findByWorkId(@Param("workId") Long workId);
}
