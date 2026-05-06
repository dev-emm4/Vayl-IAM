package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.UrlValidator;
import org.jspecify.annotations.NonNull;

public record ApiId(@NonNull String id) implements LicenseRestrictable {
  public ApiId {
    this.throwErrorOnInvalidId(id);
  }

  private void throwErrorOnInvalidId(String id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_API_ID);
    AssertionConcern.isValid(new UrlValidator(), id, ExceptionReason.INVALID_API_ID);
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
