package com.jangbee.openbank;

import lombok.Data;

/**
 * Created by test on 2019-07-06.
 */
public class OpenbankDto {
    @Data
    public static class OobAccTokenResponse {
        private String access_token;
        private String token_type;
        private Integer expires_in;
        private String scope;
        private String client_use_code;

    }

    @Data
    public static class OobAccTokenInfo {
        private String access_token;
        private String token_type;
        private String exp_date;
        private String scope;
        private String client_use_code;

    }
}
