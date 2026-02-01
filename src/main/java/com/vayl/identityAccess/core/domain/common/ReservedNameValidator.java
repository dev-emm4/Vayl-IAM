package com.vayl.identityAccess.core.domain.common;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReservedNameValidator {
    private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
            "admin", "root", "superuser", "system"
    ));

    public boolean isValid(String input) {
        if (input == null) return false;
        return !RESERVED_WORDS.contains(input.toLowerCase());
    }
}
