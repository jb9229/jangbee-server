package com.jangbee.firm;

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
    long countByEquiListStrLike(String equipment);

    List<FirmDto.ListResponse> getNearFirm(String equipment, Double longitude, Double latitude, int startRowNum, int size);

    Page<Firm> findByEquiListStrLikeAndSidoAddrAndSigunguAddr(String likeEquipmentStr, String sidoAddr, String sigunguAddr, Pageable pageable);

    @Query(value="SELECT f FROM Firm f WHERE f.equiListStr = :equiListStr and (f.workAlarmSido LIKE :workAlarmSido OR f.workAlarmSigungu LIKE :workAlarmSigungu)")
    List<Firm> findAvaWorkFirm(@Param("equiListStr") String equiListStr, @Param("workAlarmSido") String workAlarmSido, @Param("workAlarmSigungu") String workAlarmSigungu);

    List<Firm> findByAccountIdIn(List<String> appliAccountIdList);

    @Transactional
    Long deleteByAccountId(String accountId);
}

// \n-- #pageable\n
// ?#{#pageable}