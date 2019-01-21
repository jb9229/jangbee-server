package com.jangbee.common;

import lombok.Data;

import java.util.List;

/**
 * Created by test on 2015-10-29.
 */
@Data
public class ErrorResponse {

    private String message;
    private String code;

    private List<FieldError> errors;

    public static class FieldError{

    }
}
