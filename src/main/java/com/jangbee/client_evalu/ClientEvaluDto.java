package com.jangbee.client_evalu;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by test on 2019-03-26.
 */
public class ClientEvaluDto {
    @Data
    public static class Response {
        private Long id;
        private String accountId;
        private String telNumber;
        private String cliName;
        private String reason;
        private int likeCount;
        private int unlikeCount;
        private boolean likedEvau;
    }

    @Data
    public static class Create {
        @NotNull
        private String accountId;
        @NotBlank
        @Size(max = 45)
        private String telNumber;
        @NotBlank
        @Size(max = 45)
        private String cliName;

        @NotBlank
        @Size(max = 1000)
        private String reason;
    }

    @Data
    public static class Update {
        @NotNull
        private Long id;

        @NotBlank
        @Size(max = 45)
        private String cliName;

        @NotBlank
        @Size(max = 1000)
        private String reason;
    }
}
