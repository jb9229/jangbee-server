package com.jangbee.work;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-05-21.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "매칭비 결제 실패, 통장잔액 확인 또는 통장 재인증해 주세요")
public class FirmworkWithdrawException extends RuntimeException {
}
