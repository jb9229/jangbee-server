package com.jangbee.firebase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-04-04.
 */
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "Firebase Database 조회 문제입니다, 다시 시도해 주세요")
public class FBDatabaseException extends RuntimeException{
}
