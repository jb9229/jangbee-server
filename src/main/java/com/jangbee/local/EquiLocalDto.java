package com.jangbee.local;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by test on 2019-02-14.
 */
public class EquiLocalDto {
    @Data
    public static class Response {
        List<String> sidoList;
        Map<String, List> gunguData;
    }
}
