//package com.vayl.identityAccess.core.domain.organization.ou.registrationSession;
//
//import com.vayl.identityAccess.core.domain.common.AssertionConcern;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
//import org.jspecify.annotations.NonNull;
//
//public record RegSessionId(String id) {
//  public RegSessionId(@NonNull String id) {
//    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isValid(new UuidValidator(), id, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.id = id;
//  }
//
//  @Override
//  public @NonNull String toString() {
//    return this.id;
//  }
//}
