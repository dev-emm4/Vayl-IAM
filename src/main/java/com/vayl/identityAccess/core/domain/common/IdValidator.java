package com.vayl.identityAccess.core.domain.common;

import java.util.regex.Pattern;

public class IdValidator {
    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static boolean isValid(String uuidString) {
        if (uuidString == null) return false;
        return UUID_PATTERN.matcher(uuidString).matches();
    }
}
