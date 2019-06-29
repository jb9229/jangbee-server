package com.jangbee.utils;

/**
 * Created by test on 2019-06-29.
 */
public class StringUtils {
    public static String getTelNumber(String number){
        if (number != null && !number.isEmpty()) {
            if (!number.startsWith("02")) {
                if (number.length() == 10) {
                    return number.substring(0, 3)+"-"+number.substring(3, 6)+"-"+number.substring(6, 10);
                }
                if (number.length() == 11) {
                    return number.substring(0, 3)+"-"+number.substring(3, 7)+"-"+number.substring(7, 11);
                }
            } else {
                if (number.length() == 9) {
                    return number.substring(0, 2)+"-"+number.substring(2, 5)+"-"+number.substring(5, 9);
                }
                if (number.length() == 10) {
                    return number.substring(0, 2)+"-"+number.substring(2, 6)+"-"+number.substring(6, 10);
                }
            }
        }
        return number;
    }
}
