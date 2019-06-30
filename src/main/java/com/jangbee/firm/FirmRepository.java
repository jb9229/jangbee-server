package com.jangbee.firm;

import com.jangbee.stat.StatDto;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by test on 2019-01-20.
 */
public interface FirmRepository extends JpaRepository<Firm, Long> {
    Firm findByAccountId(String accountId);
    long countByEquiListStr(String equipment);

    List<FirmDto.ListResponse> getNearFirm(String equipment, Double longitude, Double latitude, int startRowNum, int size);

    Page<Firm> findByEquiListStrAndSidoAddrAndSigunguAddr(String equipmentStr, String sidoAddr, String sigunguAddr, Pageable pageable);

    Page<Firm> findByEquiListStrAndSidoAddr(String equipmentStr, String sidoAddr, Pageable pageable);

    @Query(value="SELECT f FROM Firm f WHERE f.equiListStr = :equiListStr and (f.workAlarmSido LIKE :workAlarmSido OR f.workAlarmSigungu LIKE :workAlarmSigungu)")
    List<Firm> findAvaWorkFirm(@Param("equiListStr") String equiListStr, @Param("workAlarmSido") String workAlarmSido, @Param("workAlarmSigungu") String workAlarmSigungu);

    @Query(value="SELECT f FROM Firm f WHERE f.workAlarmSido LIKE :workAlarmSido")
    List<Firm> findCEvaluAlarmSidoFirm(@Param("workAlarmSido") String workAlarmSido);

    @Query(value="SELECT f FROM Firm f WHERE f.workAlarmSigungu LIKE :workAlarmSigungu")
    List<Firm> findCEvaluAlarmSigunguFirm(@Param("workAlarmSigungu") String workAlarmSigungu);

    List<Firm> findByAccountIdIn(List<String> appliAccountIdList);

    @Transactional
    Long deleteByAccountId(String accountId);

    List<StatDto.FirmLocalCount> getFirmLocalCount(String equipmentStr);
}

// \n-- #pageable\n
// ?#{#pageable}