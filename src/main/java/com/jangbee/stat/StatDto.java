package com.jangbee.stat;

import lombok.Data;

/**
 * Created by test on 2019-06-08.
 */
public class StatDto {
    @Data
    public static class FirmLocalCount {
        public FirmLocalCount() {}
        public FirmLocalCount(String sido, int firmCount, String model) {
            this.sido = sido;
            this.firmCount = firmCount;
            this.model = model;
        }

        String sido;
        int firmCount;
        String model;
    }
}
