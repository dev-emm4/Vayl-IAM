package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.EmailChecker;
import org.jspecify.annotations.NonNull;

public record EmailInput(@NonNull String email) implements InputtableValue {
  public EmailInput {
    this.throwErrorIfInputIsInvalid(email);
  }

  private void throwErrorIfInputIsInvalid(String email) {
    AssertionConcern.isNotNull(email, ExceptionReason.INVALID_EMAIL_INPUT);
    AssertionConcern.isValid(new EmailChecker(), email, ExceptionReason.INVALID_EMAIL_INPUT);
  }
}
