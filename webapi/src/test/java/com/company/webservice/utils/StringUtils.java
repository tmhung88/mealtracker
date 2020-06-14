package com.company.webservice.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
    private static final List<Character> SPECIAL_MYSQL_LETTERS = Arrays.asList('\'');
    public static String sqlEscape(String text) {
        var builder = new StringBuilder();
        for (Character letter : text.toCharArray()) {
            if (SPECIAL_MYSQL_LETTERS.contains(letter)) {
                builder.append('\'');
                builder.append(letter);
            } else {
                builder.append(letter);
            }
        }
        return builder.toString();
    }
}
