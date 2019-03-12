package com.jangbee.ad;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by test on 2019-02-14.
 */
public class AdDto {
    @Data
    public static class Response {
        private String accountId;
        private String photoUrl;
        private String title;
        private String subTitle;
        private String telNumber;
    }

    @Data
    public static class Create {
        @NotNull
        private AdType adType;
        private short adOrder;
        @NotBlank
        private String accountId;
        @NotBlank
        @Size(max = 10)
        private String title;
        @NotBlank
        @Size(max = 20)
        private String subTitle;
        private String photoUrl;
        @NotNull
        private Integer price;
        @NotNull
        private String accessToken;
        @NotNull
        private String obRefreshToken;
        @NotNull
        private String fintechUseNum;
    }

    @Data
    public static class TransferWithdrawResponse {
        private String rsp_code;    // "A0000"
        private String rsp_message;    // "A0000"

        private String account_holder_name;    // "홍길동"
        private String tran_amt;    // "A0000"
        private String bank_name;    // "A0000"
        private String account_alias; // 급여계좌
        private String print_content;
        private String bank_tran_id;  // "12345678901234567890"
        private String api_tran_dtm;  // "20160310101921567"
    }

    @Data
    public static class RefreshTokenResponse {
        private String access_token;    // "홍길동"
        private String token_type;    // "A0000"
        private String expires_in;    // "A0000"
        private String refresh_token; // 급여계좌
        private String scope;
        private String user_seq_no;  // "12345678901234567890"
    }
}
