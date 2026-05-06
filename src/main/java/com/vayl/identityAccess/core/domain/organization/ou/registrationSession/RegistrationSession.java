//package com.vayl.identityAccess.core.domain.organization.ou.registrationSession;
//
//import com.vayl.identityAccess.core.domain.common.AssertionConcern;
//import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
//import com.vayl.identityAccess.core.domain.organization.OrgId;
//import com.vayl.identityAccess.core.domain.organization.RegSessInitiator;
//import com.vayl.identityAccess.core.domain.organization.ou.OuId;
//
//public class RegistrationSession {
//  OrgId orgId;
//  RegSessionId id;
//  RegSessInitiator initiator;
//  String endUserId;
//  Mode mode;
//  ProfileRegPhase profileRegPhase;
//  MfaRegPhase mfaRegPhase;
//  Boolean isOpen;
//
//  public RegistrationSession(
//      OrgId orgId,
//      RegSessionId id,
//      RegSessInitiator initiator,
//      String endUserId,
//      Mode mode,
//      ProfileRegPhase profileRegPhase,
//      MfaRegPhase mfaRegPhase,
//      Boolean isOpen) {
//    setOrgId(orgId);
//    setId(id);
//    setInitiator(initiator);
//    setEndUserId(endUserId);
//    setMode(mode);
//    setProfileRegPhase(profileRegPhase);
//    setMfaRegPhase(mfaRegPhase);
//    setIsOpen(isOpen);
//  }
//
//  private void setOrgId(OrgId orgId) {
//    AssertionConcern.isNotNull(orgId, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.orgId = orgId;
//  }
//
//  private void setId(RegSessionId id) {
//    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.id = id;
//  }
//
//  private void setInitiator(RegSessInitiator initiator) {
//    AssertionConcern.isNotNull(initiator, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.initiator = initiator;
//  }
//
//  private void setEndUserId(String endUserId) {
//    AssertionConcern.isNotNull(endUserId, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.endUserId = endUserId;
//  }
//
//  private void setMode(Mode mode) {
//    AssertionConcern.isNotNull(mode, ExceptionReason.INVALID_REG_SESSION_ARG);
//    AssertionConcern.isTrue(this.canInitiatorUseMode(mode), ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.mode = mode;
//  }
//
//  private boolean canInitiatorUseMode(Mode mode) {
//    // TODO: Check update mode
//      return mode == Mode.CREATE && this.initiator instanceof OuId;
//  }
//
//  private void setProfileRegPhase(ProfileRegPhase profileRegPhase) {
//    AssertionConcern.isNotNull(profileRegPhase, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.profileRegPhase = profileRegPhase;
//  }
//
//  private void setMfaRegPhase(MfaRegPhase mfaRegPhase) {
//    AssertionConcern.isNotNull(mfaRegPhase, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.mfaRegPhase = mfaRegPhase;
//  }
//
//  private void setIsOpen(Boolean isOpen) {
//    AssertionConcern.isNotNull(isOpen, ExceptionReason.INVALID_REG_SESSION_ARG);
//    this.isOpen = isOpen;
//  }
//}
