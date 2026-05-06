//package com.vayl.identityAccess.core.domain.organization.ou.registrationSession;
//
//import com.vayl.identityAccess.core.domain.common.AssertionConcern;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import java.util.List;
//import org.jspecify.annotations.NonNull;
//
//public class MfaRegPhase {
//  List<FieldRegistration> fieldRegistrations;
//  Status status;
//  Boolean isRequired;
//
//  public MfaRegPhase(
//      @NonNull List<FieldRegistration> fieldRegistrations,
//      @NonNull Status status,
//      @NonNull Boolean isRequired) {
//    this.setFieldRegistrations(fieldRegistrations);
//    this.setStatus(status);
//    this.setRequired(isRequired);
//  }
//
//  private void setFieldRegistrations(List<FieldRegistration> fieldRegistrations) {
//    AssertionConcern.isNotNull(fieldRegistrations, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.fieldRegistrations = fieldRegistrations;
//  }
//
//  private void setStatus(Status status) {
//    AssertionConcern.isNotNull(status, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.status = status;
//  }
//
//  private void setRequired(Boolean isRequired) {
//    AssertionConcern.isNotNull(isRequired, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.isRequired = isRequired;
//  }
//}
