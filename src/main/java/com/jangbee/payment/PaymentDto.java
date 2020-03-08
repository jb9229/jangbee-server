package com.jangbee.payment;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by kosac on 08/03/2020.
 */
public class PaymentDto {
    @Data
    public static class ApprovalResponse {
        private String sid;
    }

    @Data
    public static class Approval {
        @NotBlank
        @NotNull
        private String pgToken;
        @NotBlank
        @NotNull
        private String tid;
        @NotBlank
        @NotNull
        private String partnerOrderId;
        @NotBlank
        @NotNull
        private String partnerUserId;
    }
}
