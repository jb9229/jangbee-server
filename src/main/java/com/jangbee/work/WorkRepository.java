package com.jangbee.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by test on 2019-04-15.
 */
public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query(value="SELECT w FROM Work w WHERE w.equipment = :equipment and (w.workState = 0 or (w.workState = 0 and w.matchedAccId = :accountId)) ORDER BY w.workState desc")
    List<Work> getFirmWorkingList(@Param("equipment")String equipment, @Param("accountId")String accountId);
}
