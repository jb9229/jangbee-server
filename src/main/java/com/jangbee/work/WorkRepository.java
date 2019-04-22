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

    @Query(value="SELECT w FROM Work w WHERE w.equipment = :equipment and (w.workState = :openWorkState or (w.workState = :selectWorkState and w.matchedAccId = :accountId)) ORDER BY w.workState desc")
    List<Work> getOpenFirmWorkList(@Param("equipment")String equipment, @Param("accountId")String accountId, @Param("openWorkState") WorkState openWorkState, @Param("selectWorkState") WorkState selectWorkState);

    @Query(value="SELECT w FROM Work w WHERE w.accountId = :accountId and w.workState <= :workState ORDER BY w.workState desc")
    List<Work> getOpenClientWorkList(@Param("accountId")String accountId, @Param("workState") WorkState workingWorkState);

    @Query(value="SELECT w FROM Work w WHERE w.equipment = :equipment and w.workState >= :workState and w.matchedAccId = :accountId ORDER BY w.startDate desc")
    List<Work> getMatchedFirmWorkList(@Param("equipment")String equipment, @Param("accountId")String accountId, @Param("workState") WorkState workState);

    @Query(value="SELECT w FROM Work w WHERE w.accountId = :accountId and w.workState >= :workState ORDER BY w.workState")
    List<Work> getMatchedClientWorkList(@Param("accountId")String accountId, @Param("workState") WorkState workState);

    @Modifying
    @Transactional
    @Query(value="UPDATE Work w SET w.applyNoticeTime = :date WHERE w.id = :workId")
    void updateApplyNoticeTime(@Param("date") Date date, @Param("workId") Long workId);

    @Query(value="SELECT w FROM Work w WHERE w.startDate < :nowDate and w.workState < :workState")
    List<Work> getOvertimeOpenWorkList(@Param("nowDate") Date nowDate, @Param("workState") WorkState workState);

    @Query(value="SELECT w FROM Work w WHERE w.startDate < :beforeTwoMonth and w.workState = :workState")
    List<Work> getOverTwoMonthWorkList(@Param("beforeTwoMonth") Date beforeTwoMonth, @Param("workState") WorkState workState);

    @Query(value="SELECT w FROM Work w WHERE w.endDate < :beforeOneDate and w.workState = :workState") // Working State
    List<Work> getOvertimeWorkingWorkList(@Param("beforeOneDate") Date beforeOneDate, @Param("workState")WorkState workState);
}
