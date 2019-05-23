package com.jangbee.coupon;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
