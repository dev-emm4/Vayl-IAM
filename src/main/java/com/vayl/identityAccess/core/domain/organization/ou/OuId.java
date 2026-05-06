package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
import com.vayl.identityAccess.core.domain.organization.RegSessInitiator;
import org.jspecify.annotations.NonNull;

public record OuId(String id) implements RegSessInitiator {
  public OuId(@NonNull String id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_OU_ID);
    AssertionConcern.isValid(new UuidValidator(), id, ExceptionReason.INVALID_OU_ID);
    this.id = id;
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
