package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

public record UsernameInput(@NonNull String username) implements InputtableValue {
  public UsernameInput {
    this.throwErrorIfUsernameIsInvalid(username);
  }

  public void throwErrorIfUsernameIsInvalid(String username) {
    AssertionConcern.isNotNull(username, ExceptionReason.INVALID_USERNAME_INPUT);
    AssertionConcern.isNotBlank(username, ExceptionReason.INVALID_USERNAME_INPUT);
  }
}
