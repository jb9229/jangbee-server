package com.jangbee.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-03-21.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "요청값에 문제가 있습니다, 관리자에게 문의해 주세요")
public class JBBadRequestException extends RuntimeException {
}
