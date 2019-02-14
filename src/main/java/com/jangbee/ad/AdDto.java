package com.jangbee.ad;

import lombok.Data;

/**
 * Created by test on 2019-02-14.
 */
public class AdDto {
    @Data
    public static class Response {
        private Long firmId;
        private String photoUrl;
        private String title;
        private String subTitle;
        private String telNumber;
    }
}
