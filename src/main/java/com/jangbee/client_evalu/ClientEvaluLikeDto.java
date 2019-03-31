package com.jangbee.client_evalu;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by test on 2019-03-28.
 */
public class ClientEvaluLikeDto {
    @Data
    static class Response {
        private long id;
        private String reason;
        private boolean evaluLike;
        private String accountId;
    }

    @Data
    static class Create {
        private String accountId;
        @Min(1)
        private long evaluId;
        @NotNull
        private boolean evaluLike;
        @NotBlank
        @Size(max = 500)
        private String reason;
    }
}
