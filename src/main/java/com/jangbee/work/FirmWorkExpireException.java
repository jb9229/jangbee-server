package com.jangbee.work;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-05-22.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "죄송합니다, 차주일감 보장시간이 만료되어 지원할 수 없습니다.")
public class FirmWorkExpireException extends RuntimeException {
}
