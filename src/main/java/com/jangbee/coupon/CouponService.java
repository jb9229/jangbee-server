package com.jangbee.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by test on 2019-05-23.
 */
@Service
public class CouponService {

    @Autowired
    CouponRepository repository;

    public void payFirmWorkCoupon(String accountId, int addCnt) {
        Coupon coupon =  repository.findByAccountId(accountId);

        if(coupon == null){
            coupon = new Coupon();
            coupon.setCpCount(addCnt);
            coupon.setAccountId(accountId);
            coupon.setType(CouponType.FirmWorkCoupon);

            repository.save(coupon);
        } else {
            coupon.setCpCount(coupon.getCpCount()+addCnt);

            repository.saveAndFlush(coupon);
        }
    }

    public Coupon getCoupon(String accountId) {
        return repository.findByAccountId(accountId);
    }

    public Integer useCoupon(String accountId, int useCount) {
        return repository.updateCouponCount(accountId, useCount);
    }
}
