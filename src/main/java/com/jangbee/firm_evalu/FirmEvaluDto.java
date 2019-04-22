package com.jangbee.firm_evalu;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by test on 2019-04-18.
 */
public class FirmEvaluDto {

    @Data
    public static class Create {
        @NotNull
        private Long workId;

        @NotNull
        private Byte rating;

        @NotBlank
        @Size(max=200)
        private String comment;
    }
}
