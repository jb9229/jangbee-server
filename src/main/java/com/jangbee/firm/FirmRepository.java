package com.jangbee.firm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Created by test on 2019-01-20.
 */
public interface FirmRepository extends JpaRepository<Firm, Long> {
    Firm findByAccountId(String accountId);
    long countByEquiListStrLike(String equipment);

    List<FirmDto.ListResponse> getNearFirm(String equipment, Double longitude, Double latitude, int startRowNum, int size);

    Page<Firm> findByEquiListStrLikeAndSidoAddrAndSigunguAddr(String likeEquipmentStr, String sidoAddr, String sigunguAddr, Pageable pageable);

    List<Firm> findByEquiListStr(String equiListStr);
}

// \n-- #pageable\n
// ?#{#pageable}