package com.vayl.identityAccess.core.domain.common;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReservedNameValidator {
    private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
           "password", "username", "primary_email", "authorization_policy", "authentication_policy" , "mfa_policy"
    ));

    public boolean isValid(String input) {
        if (input == null) return false;
        return !RESERVED_WORDS.contains(input.toLowerCase());
    }
}
