package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
import org.jspecify.annotations.NonNull;

public record OuId(String id) {
  public OuId(@NonNull String id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isValid(new UuidValidator(), id, ExceptionReason.INVALID_OU_ARG);
    this.id = id;
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
