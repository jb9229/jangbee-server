package com.jangbee.firm_evalu;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by test on 2019-04-18.
 */
public class FirmEvaluDto {

    public static class Create {
        @NotNull
        private Long workId;
        @NotBlank
        private String accountId;
        @NotBlank
        private String firmAccountId;
        @NotNull
        private Byte rate;

        @NotBlank
        @Size(max=200)
        private String comment;
    }
}
