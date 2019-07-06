package com.jangbee.coupon;

import com.jangbee.common.JBBadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by test on 2019-05-23.
 */
@RestController
@RequestMapping("/api/v1/")
public class CouponController {
    @Autowired
    private CouponService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value="coupon", method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam String accountId) {
        Coupon coupon =   service.getCoupon(accountId);

        if(coupon == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(modelMapper.map(coupon, CouponDto.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value="cashback", method = RequestMethod.GET)
    public ResponseEntity getAvailCashback(@RequestParam String accountId) {
        int availCashback =   service.getAvailCashback(accountId);

        return new ResponseEntity<>(availCashback, HttpStatus.OK);
    }

    @RequestMapping(value="cashback", method = RequestMethod.POST)
    public ResponseEntity getAvailCashback(@RequestBody @Valid CashbackDto.Create create, BindingResult result) {
        if(result.hasErrors()){
            throw new JBBadRequestException();
        }

        boolean availCashback =   service.cashback(create);

        return new ResponseEntity<>(modelMapper.map(availCashback, CouponDto.Response.class), HttpStatus.OK);
    }
}
