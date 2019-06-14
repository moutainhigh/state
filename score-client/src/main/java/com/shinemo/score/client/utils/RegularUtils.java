package com.shinemo.score.client.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @author wenchao.li
 * @since 2019-06-14
 */
public class RegularUtils {

    private RegularUtils() {
    }


    public static String ignorePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
