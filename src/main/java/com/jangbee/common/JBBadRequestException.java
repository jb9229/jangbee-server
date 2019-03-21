package com.jangbee.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by test on 2019-03-21.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class JBBadRequestException extends RuntimeException {
    public JBBadRequestException (String message) {
        super(message);
    }
}
