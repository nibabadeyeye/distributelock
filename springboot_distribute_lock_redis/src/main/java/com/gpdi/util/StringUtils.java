package com.gpdi.util;

import java.util.UUID;

public class StringUtils {
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String ObjectToString(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }
}
