package com.jangbee.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-04-15.
 */
public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query(value="SELECT w FROM Work w WHERE w.equipment = :equipment and (w.workState = 0 or (w.workState = 0 and w.matchedAccId = :accountId)) ORDER BY w.workState desc")
    List<Work> getOpenFirmWorkList(@Param("equipment")String equipment, @Param("accountId")String accountId);

    @Query(value="SELECT w FROM Work w WHERE w.equipment = :equipment and w.workState = 1 and w.matchedAccId = :accountId ORDER BY w.startDate desc")
    List<Work> getMatchedFirmWorkList(@Param("equipment")String equipment, @Param("accountId")String accountId);

    @Modifying
    @Transactional
    @Query(value="UPDATE Work w SET w.applyNoticeTime = :date WHERE w.id = :workId")
    void updateApplyNoticeTime(@Param("date") Date date, @Param("workId") Long workId);
}
