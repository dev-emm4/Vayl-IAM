package com.vayl.identityAccess.core.domain.license;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
import org.jspecify.annotations.NonNull;

public record LicenseId(@NonNull String id) {
  public LicenseId {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_LICENSE_ARG);
    AssertionConcern.isValid(new UuidValidator(), id, ExceptionReason.INVALID_LICENSE_ARG);
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
