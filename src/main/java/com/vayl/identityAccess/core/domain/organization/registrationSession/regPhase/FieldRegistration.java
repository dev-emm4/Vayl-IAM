package com.vayl.identityAccess.core.domain.organization.registrationSession.regPhase;

import com.vayl.identityAccess.core.application.ExceptionEvent;
import com.vayl.identityAccess.core.application.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import org.jspecify.annotations.NonNull;

public class FieldRegistration {
  String fieldName;
  String fieldValue;
  FieldType type;
  boolean isValueRequired;
  boolean isVerificationRequired;
  boolean isVerified;

  public FieldRegistration(
      @NonNull String fieldName,
      String fieldValue,
      @NonNull FieldType type,
      boolean isValueRequired,
      boolean isVerificationRequired,
      boolean isVerified) {
    this.setFieldName(fieldName);
    this.setFieldValue(fieldValue);
    this.setType(type);
    this.setValueRequired(isValueRequired);
    this.setVerificationRequired(isVerificationRequired);
    this.setVerified(isVerified);
  }

  private void setFieldName(String fieldName) {
    throwErrorIfNameIsBlank(fieldName);
    this.fieldName = fieldName;
  }

  private void throwErrorIfNameIsBlank(String fieldName) {
    if (fieldName.isBlank()) {
      throw new InvalidValueException(ExceptionReason.BLANK_NAME_IN_FIELD_REGISTRATION, fieldName);
    }
  }

  private void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }

  private void setType(@NonNull FieldType type) {
    this.type = type;
  }

  private void setValueRequired(boolean isValueRequired) {
    this.isValueRequired = isValueRequired;
  }

  private void setVerificationRequired(boolean isVerificationRequired) {
    this.isVerificationRequired = isVerificationRequired;
  }

  private void setVerified(boolean isVerified) {
    this.isVerified = isVerified;
  }

  public boolean isVerified() {
    if (!this.isVerificationRequired) {
      return true;
    }

    return this.isVerified;
  }

  public boolean isValueRequired() {
    return isValueRequired;
  }
}
