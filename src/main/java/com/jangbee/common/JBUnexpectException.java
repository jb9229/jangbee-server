package com.jangbee.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-05-21.
 */
@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY, reason = "장비 콜 서버에 문제가 발생했습니다, 다시 시도해 주세요(네트워크 장애등..)")
public class JBUnexpectException extends RuntimeException {
}
