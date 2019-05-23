package com.jangbee.coupon;

import lombok.Data;

/**
 * Created by test on 2019-05-23.
 */
public class CouponDto {
    @Data
    public static class Response {
        private Long id;
        private String accountId;
        private int cpCount;
    }
}
