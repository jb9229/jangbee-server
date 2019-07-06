package com.jangbee.coupon;

import com.jangbee.openbank.OpenbackCommonException;
import com.jangbee.openbank.OpenbankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by test on 2019-05-23.
 */
@Service
public class CouponService {

    @Autowired
    CouponRepository repository;
    @Autowired
    OpenbankService OpenbankService;

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

    public int getAvailCashback(String accountId) {
        return repository.findAvailCashback(accountId);
    }

    @Transactional
    public boolean cashback(CashbackDto.Create create) {
        Integer udpateResult = repository.cashback(create.getAccountId(), create.getCashback());

        if(udpateResult != null &&  udpateResult > 0) {
            OpenbankService.diposit(create.getAuthToken(), create.getFintechUseNum(), "장비콜캐쉬백", create.getCashback());
        }
        return true;
    }
}
