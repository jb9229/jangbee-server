package com.jangbee.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-05-23.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "사용할 수 있는 쿠폰량이 부족합니다")
public class CouponLackException extends RuntimeException {
}
