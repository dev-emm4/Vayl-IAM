package com.vayl.identityAccess.core.domain.common.validator;

import org.jspecify.annotations.NonNull;

import java.util.regex.Pattern;

public class UuidValidator implements Validator {
  private final Pattern UUID_PATTERN =
      Pattern.compile(
          "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

  public boolean isValid(@NonNull String uuidString) {
      return UUID_PATTERN.matcher(uuidString).matches();
  }
}
