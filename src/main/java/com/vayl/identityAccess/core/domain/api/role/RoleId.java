package com.vayl.identityAccess.core.domain.api.role;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
import org.jspecify.annotations.NonNull;

public record RoleId(@NonNull String id) {
  public RoleId {
    this.throwErrorOnInvalidId(id);
  }

  private void throwErrorOnInvalidId(String id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_ROLE_ID);
    AssertionConcern.isValid(new UuidValidator(), id, ExceptionReason.INVALID_ROLE_ID);
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
