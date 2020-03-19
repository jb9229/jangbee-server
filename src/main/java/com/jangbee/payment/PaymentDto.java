package com.jangbee.payment;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by kosac on 08/03/2020.
 */
public class PaymentDto {
    @Data
    public static class ReadyResponse {
        private String tid;
        private String next_redirect_app_url;
        private String next_redirect_mobile_url;
        private String next_redirect_pc_url;
        private String android_app_scheme;
        private String ios_app_scheme;
        private String created_at;
    }

    @Data
    public static class ApprovalResponse {
        private String sid;
    }

    @Data
    public static class SubscriptionResponse {
        private String tid;
        private String created_at;
    }

    @Data
    public static class Ready {
        @NotBlank
        @NotNull
        private String itemName;

        @NotBlank
        @NotNull
        private String totalAmount;

        @NotBlank
        @NotNull
        private String partnerOrderId;

        @NotBlank
        @NotNull
        private String partnerUserId;
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
