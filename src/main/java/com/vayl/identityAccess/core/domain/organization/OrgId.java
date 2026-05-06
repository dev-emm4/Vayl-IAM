package com.vayl.identityAccess.core.domain.organization;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
import org.jspecify.annotations.NonNull;

public record OrgId(String id) {
  public OrgId(@NonNull String id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_ORG_ID);
    AssertionConcern.isValid(new UuidValidator(), id, ExceptionReason.INVALID_ORG_ID);
    this.id = id;
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
