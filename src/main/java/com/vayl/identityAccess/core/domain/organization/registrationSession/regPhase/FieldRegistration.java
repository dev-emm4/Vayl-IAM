package com.vayl.identityAccess.core.domain.organization.registrationSession.regPhase;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import org.jspecify.annotations.NonNull;

public record FieldRegistration(
    String fieldName,
    String fieldValue,
    FieldType type,
    boolean isValueRequired,
    boolean isVerificationRequired,
    boolean isVerified) {
  public FieldRegistration(
      @NonNull String fieldName,
      String fieldValue,
      @NonNull FieldType type,
      boolean isValueRequired,
      boolean isVerificationRequired,
      boolean isVerified) {
    AssertionConcern.isNotNull(fieldName, ExceptionReason.INVALID_REG_SESSION_ARG);
    AssertionConcern.isNotNull(type, ExceptionReason.INVALID_REG_SESSION_ARG);
    AssertionConcern.isNotBlank(fieldName, ExceptionReason.INVALID_REG_SESSION_ARG);

    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    this.type = type;
    this.isValueRequired = isValueRequired;
    this.isVerificationRequired = isVerificationRequired;
    this.isVerified = isVerified;
  }

  @Override
  public boolean isVerified() {
    if (!this.isVerificationRequired) {
      return true;
    }

    return this.isVerified;
  }
}
