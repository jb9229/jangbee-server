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
        private String cliName;
        private String firmName;
        private String telNumber;
        private String telNumber2;
        private String telNumber3;
        private String firmNumber;
        private String reason;
        private int likeCount;
        private int unlikeCount;
        private boolean likedEvau;
    }

    @Data
    public static class Create {
        @NotNull
        private String accountId;

        @Size(max = 8)
        private String cliName;
        @Size(max = 12)
        private String firmName;
        @NotBlank
        @Size(max = 45)
        private String telNumber;
        @Size(max = 45)
        private String telNumber2;
        @Size(max = 45)
        private String telNumber3;

        @Size(min=12, max=12)
        private String firmNumber;

        @NotBlank
        @Size(max = 1000)
        private String reason;
    }

    @Data
    public static class Update {
        @NotNull
        private Long id;

        @Size(max = 8)
        private String cliName;

        @Size(max = 12)
        private String firmName;

        @Size(max = 45)
        private String telNumber2;
        @Size(max = 45)
        private String telNumber3;

        @Size(min=12, max=12)
        private String firmNumber;

        @NotBlank
        @Size(max = 1000)
        private String reason;
    }
}
