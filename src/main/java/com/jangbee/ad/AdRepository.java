package com.jangbee.ad;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by test on 2019-02-14.
 */
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> getByAdTypeAndEquiTargetAndSidoTargetAndGugunTarget(AdType adType, String equiTarget, String sidoTarget, String gugunTarget);

    List<Ad> getByAccountId(String accountId);
}
