package com.jangbee.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by test on 2019-02-14.
 */
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> getByAdLocationAndEquiTargetAndSidoTargetAndGugunTarget(AdLocation adLocation, String equiTarget, String sidoTarget, String gugunTarget);

    List<Ad> getByAccountId(String accountId);

    @Query(value="SELECT ad_type FROM ad", nativeQuery = true)
    List<Integer> getBookedAdType();

    Ad getByEquiTargetAndAdType(String equipment, short adType);

    Ad getByEquiTargetAndSidoTargetAndGugunTargetAndAdType(String equipment, String sidoTarget, String gunguTarget, short adType);
}
