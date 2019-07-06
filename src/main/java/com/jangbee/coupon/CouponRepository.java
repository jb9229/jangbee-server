package com.jangbee.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by test on 2019-05-23.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByAccountId(String accountId);

    @Modifying    // update , delete Query시 @Modifying 어노테이션을 추가
    @Transactional
    @Query(value="UPDATE Coupon c SET c.cpCount = c.cpCount - :useCount WHERE c.accountId = :accountId and c.cpCount >= :useCount")
    Integer updateCouponCount(@Param("accountId")String accountId, @Param("useCount") int useCount);

    @Query(value ="select c.availCashback from Coupon c where c.accountId = :accountId")
    Integer findAvailCashback(@Param("accountId")String accountId);

    @Modifying
    @Query(value ="update Coupon c set c.availCashback = c.availCashback - :cashback where c.accountId = :accountId and c.availCashback >= :cashback")
    Integer cashback(@Param("accountId")String accountId, @Param("cashback")int cashback);
}
