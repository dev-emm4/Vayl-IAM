package com.vayl.identityAccess.core.domain.common.validator;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReservedNameValidator implements Validator{
    private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
           "password", "username", "primary_email", "authorization_policy", "authentication_policy" , "mfa_policy"
    ));

    public boolean isValid(@NonNull String input) {
        return !RESERVED_WORDS.contains(input.toLowerCase());
    }
}
