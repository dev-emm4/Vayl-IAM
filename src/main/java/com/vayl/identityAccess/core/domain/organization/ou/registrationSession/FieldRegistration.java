//package com.vayl.identityAccess.core.domain.organization.ou.registrationSession;
//
//import com.vayl.identityAccess.core.domain.common.AssertionConcern;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import com.vayl.identityAccess.core.domain.common.inputtableValue.InputtableValue;
//import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
//import org.jspecify.annotations.NonNull;
//
//public record FieldRegistration(
//    String fieldName,
//    InputtableValue fieldValue,
//    FieldType type,
//    Boolean isValueRequired,
//    Boolean isVerificationRequired,
//    Boolean isVerified) {
//  public FieldRegistration(
//      @NonNull String fieldName,
//      InputtableValue fieldValue,
//      @NonNull FieldType type,
//      @NonNull Boolean isValueRequired,
//      @NonNull Boolean isVerificationRequired,
//      @NonNull Boolean isVerified) {
//    AssertionConcern.isNotNull(fieldName, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isNotNull(type, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isNotNull(isValueRequired, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isNotNull(isVerificationRequired, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isNotNull(isVerified, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isNotBlank(fieldName, ExceptionReason.INVALID_REG_SESSION_ARG);
//
//    this.fieldName = fieldName;
//    this.fieldValue = fieldValue;
//    this.type = type;
//    this.isValueRequired = isValueRequired;
//    this.isVerificationRequired = isVerificationRequired;
//    this.isVerified = isVerified;
//  }
//
//  @Override
//  public @NonNull Boolean isVerified() {
//    if (!this.isVerificationRequired) {
//      return true;
//    }
//
//    return this.isVerified;
//  }
//}
