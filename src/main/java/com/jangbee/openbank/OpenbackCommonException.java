package com.jangbee.openbank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-07-06.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class OpenbackCommonException extends RuntimeException {
    public OpenbackCommonException(String message) {
        super(message);
    }
}
