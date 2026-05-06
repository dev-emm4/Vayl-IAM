//package com.vayl.identityAccess.core.domain.organization.ou.registrationSession;
//
//import com.vayl.identityAccess.core.domain.common.AssertionConcern;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import java.util.List;
//
//public class ProfileRegPhase {
//  List<FieldRegistration> fieldRegistrations;
//  Status status;
//
//  public ProfileRegPhase(List<FieldRegistration> fieldRegistrations, Status status) {
//    this.setFieldRegistrations(fieldRegistrations);
//    this.setStatus(status);
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
//}
