package com.vayl.identityAccess.core.domain.common.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.jspecify.annotations.NonNull;

public class EmailChecker implements Validator {
  private final EmailValidator validator = EmailValidator.getInstance();

  public boolean isValid(@NonNull String email) {
    if (email == null) return false;

    return validator.isValid(email);
  }
}
