package com.jangbee.work;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-05-21.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "죄송합니다, 선착순에 밀려 다른 차주가 매칭중 입니다.")
public class DuplicateWorkApplicantException extends RuntimeException {
}
