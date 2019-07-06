package com.jangbee.coupon;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by test on 2019-07-06.
 */
public class CashbackDto {
    @Data
    public static class Create {
        @NotBlank
        private String accountId;
        @NotBlank
        private String authToken;
        @NotBlank
        private String fintechUseNum;
        @NotNull
        private Integer cashback;
    }
}
