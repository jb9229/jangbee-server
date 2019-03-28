package com.jangbee.client_evalu;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-03-28.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "해당 아이디의 블랙리스트가 존재하지 않습니다")
public class ClientEvaluNotFoundException extends RuntimeException {
}
