package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

public record FieldConfigId(@NonNull String id) {
  public FieldConfigId {
    this.throwErrorIfIdIsInvalid(id);
  }

  private void throwErrorIfIdIsInvalid(@NonNull String id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
    AssertionConcern.isNotBlank(id, ExceptionReason.INVALID_FIELD_CONFIG_ARG);
  }

  @Override
  public @NonNull String toString() {
    return this.id;
  }
}
