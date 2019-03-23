package com.jangbee.ad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-03-23.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "요청하신 광고를 찾을 수 업습니다")
public class AdNotFoundException extends RuntimeException{
}
