package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.api.role.Role;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.ReservedNames;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfiguration;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.registrationSession.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class Ou {
  private OrgId orgId;
  private OuId id;
  private String name;
  private Boolean isTopLevel;
  private OuId parent;
  private AuthorizationPolicy authorizationPolicy;
  private AuthenticationPolicy authenticationPolicy;

  /** creates top-level OU */
  public Ou(
      @NonNull OrgId orgId,
      @NonNull OuId id,
      @NonNull AuthorizationPolicy authorizationPolicy,
      @NonNull AuthenticationPolicy authenticationPolicy) {
    this.setOrgId(orgId);
    this.setId(id);
    this.setName("top-level");
    this.setIsTopLevel(true);
    this.setAuthorizationPolicy(authorizationPolicy);
    this.setAuthenticationPolicy(authenticationPolicy);
    // TODO: PUBLISH TopLevelOuCreatedEvent
  }

  /** creates non-top-level OU */
  private Ou(
      @NonNull OrgId orgId,
      @NonNull OuId id,
      @NonNull String name,
      @NonNull OuId parent,
      @NonNull AuthorizationPolicy authorizationPolicy,
      @NonNull AuthenticationPolicy authenticationPolicy) {
    this.setOrgId(orgId);
    this.setId(id);
    this.setName(name);
    this.setIsTopLevel(false);
    this.setParent(parent);
    this.setAuthorizationPolicy(authorizationPolicy);
    this.setAuthenticationPolicy(authenticationPolicy);
  }

  private void setOrgId(OrgId orgId) {
    AssertionConcern.isNotNull(orgId, ExceptionReason.INVALID_ORG_ID);
    this.orgId = orgId;
  }

  private void setId(OuId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_OU_ID);
    this.id = id;
  }

  private void setName(String name) {
    AssertionConcern.isNotNull(name, ExceptionReason.INVALID_OU_NAME);
    AssertionConcern.isNotBlank(name, ExceptionReason.INVALID_OU_NAME);
    this.name = name;
  }

  private void setIsTopLevel(boolean isTopLevel) {
    this.isTopLevel = isTopLevel;
  }

  private void setParent(OuId parent) {
    AssertionConcern.isNotNull(parent, ExceptionReason.INVALID_OU_ID);
    this.parent = parent;
  }

  private void setAuthorizationPolicy(AuthorizationPolicy authorizationPolicy) {
    AssertionConcern.isNotNull(authorizationPolicy, ExceptionReason.INVALID_AUTHORIZATION_POLICY);
    this.authorizationPolicy = authorizationPolicy;
  }

  private void setAuthenticationPolicy(AuthenticationPolicy authenticationPolicy) {
    AssertionConcern.isNotNull(authenticationPolicy, ExceptionReason.INVALID_AUTHENTICATION_POLICY);
    this.authenticationPolicy = authenticationPolicy;
  }

  @Contract("_ -> new")
  public @NonNull Ou createOu(@NonNull String name) {
    OuId newOuId = new OuId(UUID.randomUUID().toString());
    AuthorizationPolicy newAuthorizationPolicy = this.authorizationPolicy().copyWith(true);
    AuthenticationPolicy newAuthenticationPolicy = this.authenticationPolicy().copyWith(true);

    return new Ou(
        this.orgId(), newOuId, name, this.id(), newAuthorizationPolicy, newAuthenticationPolicy);
  }

  public void updateAuthenticationPolicy(
      @NonNull MfaPolicy mfaPolicy, @NonNull RecoveryPolicy recoveryPolicy) {
    AssertionConcern.isNotNull(mfaPolicy, ExceptionReason.INVALID_MFA_POLICY);
    AssertionConcern.isNotNull(recoveryPolicy, ExceptionReason.INVALID_RECOVERY_POLICY);

    boolean shouldPublishEvent =
        !(this.isMfaPolicyAssigned(mfaPolicy)) || !(this.isRecoveryPolicyAssigned(recoveryPolicy));

    this.setAuthenticationPolicy(new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false));
    this.publishUpdateOuAuthenticationPolicyEvent(shouldPublishEvent);
  }

  public void updateAuthorizationPolicy(
      @NonNull List<LicenseContractId> licenseContractIds, @NonNull List<Role> roles) {
    AssertionConcern.isNotNull(licenseContractIds, ExceptionReason.INVALID_LICENSE_CONTRACT_ID);
    AssertionConcern.isNotNull(roles, ExceptionReason.INVALID_ROLE_ID);
    AssertionConcern.isTrue(
        this.isLicenseContractsInSameOrg(licenseContractIds),
        ExceptionReason.UNAUTHORIZED_ACCESS_TO_LICENSE_CONTRACT);
    AssertionConcern.isTrue(
        this.isRolesInSameOrg(roles), ExceptionReason.UNAUTHORIZED_ACCESS_TO_ROLE);

    boolean isOuAnAudience =
        !(this.isLicenseContractIdsAssigned(licenseContractIds))
            || !(this.isRoleIdsAssigned(roles.stream().map(Role::id).toList()));
    boolean isUaAnAudience = !(this.isLicenseContractIdsAssigned(licenseContractIds));

    this.setAuthorizationPolicy(
        new AuthorizationPolicy(licenseContractIds, roles.stream().map(Role::id).toList(), false));

    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUaAnAudience);
  }

  private boolean isLicenseContractsInSameOrg(@NonNull List<LicenseContractId> licenseContractIds) {
    for (LicenseContractId licenseContractId : licenseContractIds) {
      if (!(licenseContractId.orgId() == this.orgId())) {
        return false;
      }
    }
    return true;
  }

  private boolean isRolesInSameOrg(@NonNull List<Role> roles) {
    for (Role role : roles) {
      if (!(role.accessibleBy(this.orgId()))) {
        return false;
      }
    }
    return true;
  }

  public void assignOu(
      @NonNull Ou childOu,
      @NonNull Boolean shouldInheritAuthorizationPolicy,
      @NonNull Boolean shouldInheritAuthenticationPolicy) {
    AssertionConcern.isNotNull(childOu, ExceptionReason.INVALID_OU_ID);
    AssertionConcern.isNotNull(
        shouldInheritAuthorizationPolicy,
        ExceptionReason.INVALID_SHOULD_INHERIT_AUTHORIZATION_POLICY);
    AssertionConcern.isNotNull(
        shouldInheritAuthenticationPolicy,
        ExceptionReason.INVALID_SHOULD_INHERIT_AUTHENTICATION_POLICY);
    AssertionConcern.isFalse(
        this.isChildInDifferentOrg(childOu), ExceptionReason.UNAUTHORIZED_ACCESS_TO_OU);
    AssertionConcern.isFalse(
        this.isChildSelf(childOu), ExceptionReason.UNPROCESSABLE_CHILD_AND_PARENT_ARE_THE_SAME);
    AssertionConcern.isFalse(
        this.isChildParent(childOu), ExceptionReason.UNPROCESSABLE_CANNOT_ASSIGN_PARENT_TO_OU);
    AssertionConcern.isFalse(
        this.isChildTopLevel(childOu), ExceptionReason.UNPROCESSABLE_CANNOT_ASSIGN_TOP_LEVEL_TO_OU);

    if (childOu.parent() == this.id()) {
      return;
    }

    childOu.setParent(this.id());

    if (shouldInheritAuthorizationPolicy) {
      childOu.synchronizeAuthorizationPolicyWith(this);
    } else {
      AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();
      AuthorizationPolicy newChildAuthorizationPolicy = childAuthorizationPolicy.copyWith(false);
      childOu.setAuthorizationPolicy(newChildAuthorizationPolicy);
    }

    if (shouldInheritAuthenticationPolicy) {
      childOu.synchronizeAuthenticationPolicyWith(this);
    } else {
      AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();
      AuthenticationPolicy newChildAuthenticationPolicy = childAuthenticationPolicy.copyWith(false);
      childOu.setAuthenticationPolicy(newChildAuthenticationPolicy);
    }
  }

  private boolean isChildInDifferentOrg(@NonNull Ou childOu) {
    return !this.orgId().equals(childOu.orgId());
  }

  private boolean isChildSelf(@NonNull Ou childOu) {
    return childOu.id().equals(this.id());
  }

  private boolean isChildParent(@NonNull Ou childOu) {
    if (this.isTopLevel()) return false;
    return this.parent().equals(childOu.id());
  }

  private boolean isChildTopLevel(@NonNull Ou childOu) {
    return childOu.isTopLevel();
  }

  public void synchronizeAuthorizationPolicyWith(@NonNull Ou parentOu) {
    AssertionConcern.isNotNull(parentOu, ExceptionReason.INVALID_OU_ID);
    AssertionConcern.isTrue(
        isOuParent(parentOu), ExceptionReason.UNPROCESSABLE_MUST_SYNCHRONIZE_WITH_PARENT);

    AuthorizationPolicy parentAuthorizationPolicy = parentOu.authorizationPolicy();
    boolean isOuAnAudience =
        !(this.isLicenseContractIdsAssigned(parentAuthorizationPolicy.licenseContractIds()))
            || !(this.isRoleIdsAssigned(parentAuthorizationPolicy.roleIds()));
    boolean isUserAnAudience =
        !(this.isLicenseContractIdsAssigned(parentAuthorizationPolicy.licenseContractIds()));

    this.setAuthorizationPolicy(parentAuthorizationPolicy.copyWith(true));
    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUserAnAudience);
  }

  private boolean isLicenseContractIdsAssigned(List<LicenseContractId> licenseContractIds) {
    return this.authorizationPolicy().licenseContractIds().equals(licenseContractIds);
  }

  private boolean isRoleIdsAssigned(List<RoleId> roleIds) {
    return this.authorizationPolicy().roleIds().equals(roleIds);
  }

  public void synchronizeAuthenticationPolicyWith(@NonNull Ou parentOu) {
    AssertionConcern.isNotNull(parentOu, ExceptionReason.INVALID_OU_ID);
    AssertionConcern.isTrue(
        this.isOuParent(parentOu), ExceptionReason.UNPROCESSABLE_MUST_SYNCHRONIZE_WITH_PARENT);

    AuthenticationPolicy parentAuthenticationPolicy = parentOu.authenticationPolicy();
    boolean shouldPublishEvent =
        !(this.isMfaPolicyAssigned(parentAuthenticationPolicy.mfaPolicy()))
            || !(this.isRecoveryPolicyAssigned(parentAuthenticationPolicy.recoveryPolicy()));

    this.setAuthenticationPolicy(parentAuthenticationPolicy.copyWith(true));
    this.publishUpdateOuAuthenticationPolicyEvent(shouldPublishEvent);
  }

  private boolean isMfaPolicyAssigned(MfaPolicy mfaPolicy) {
    return this.authenticationPolicy().mfaPolicy().equals(mfaPolicy);
  }

  private boolean isRecoveryPolicyAssigned(RecoveryPolicy recoveryPolicy) {
    return this.authenticationPolicy().recoveryPolicy().equals(recoveryPolicy);
  }

  private boolean isOuParent(Ou parentOu) {
    return this.parent().equals(parentOu.id());
  }

  private void publishUpdateOuAuthorizationPolicyEvent(
      boolean isOuAnAudience, boolean isUserAnAudience) {
    // TODO: Publish updatedOuAuthorizationPolicy

  }

  private void publishUpdateOuAuthenticationPolicyEvent(boolean shouldPublishEvent) {
    // TODO: Publish updatedOuAuthenticationPolicy

  }

//  public @NonNull RegistrationSession createRegistrationSession(
//      List<FieldConfiguration> fieldConfigurations, String endUserId) {
//    //    AssertionConcern.isNotNull(fieldConfigurations, ExceptionReason.INVALID_REG_SESSION_ARG);
//
//    List<FieldRegistration> fieldRegistrations =
//        this.createFieldRegistrationWith(fieldConfigurations);
//
//    fieldRegistrations.add(this.createPasswordFieldRegistration());
//
//    ProfileRegPhase profileRegPhase = new ProfileRegPhase(fieldRegistrations, Status.REGISTRATION);
//
//    fieldRegistrations = this.createFieldRegistrationForMfaPhase();
//
//    MfaRegPhase mfaRegPhase =
//        new MfaRegPhase(
//            fieldRegistrations,
//            Status.REGISTRATION,
//            this.authenticationPolicy().mfaPolicy().enforcementDate().isDue());
//
//    return new RegistrationSession(
//        this.orgId(),
//        new RegSessionId(UUID.randomUUID().toString()),
//        this.id(),
//        endUserId,
//        Mode.CREATE,
//        profileRegPhase,
//        mfaRegPhase,
//        true);
//  }

//  private List<FieldRegistration> createFieldRegistrationWith(
//      List<FieldConfiguration> fieldConfigurations) {
//    List<FieldRegistration> fieldRegistrations = new ArrayList<>();
//
//    for (FieldConfiguration fieldConfiguration : fieldConfigurations) {
//      FieldRegistration fieldRegistration =
//          new FieldRegistration(
//              fieldConfiguration.fieldName(),
//              null,
//              fieldConfiguration.fieldType(),
//              fieldConfiguration.enforcementDate().isDue(),
//              fieldConfiguration.isVerifiable(),
//              false);
//
//      fieldRegistrations.add(fieldRegistration);
//    }
//
//    return fieldRegistrations;
//  }

//  private FieldRegistration createPasswordFieldRegistration() {
//    return new FieldRegistration(
//        ReservedNames.PASSWORD.toString(), null, FieldType.STRING, true, false, false);
//  }
//
//  private List<FieldRegistration> createFieldRegistrationForMfaPhase() {
//    List<FieldRegistration> fieldRegistrations = new ArrayList<>();
//    switch (this.authenticationPolicy().mfaPolicy().mfaType()) {
//      case ANY ->
//          fieldRegistrations =
//              List.of(
//                  this.createMfaEmailFieldRegistration(),
//                  this.createMfaPhoneFieldRegistration(),
//                  this.createMfaAuthenticatorAppFieldRegistration());
//
//      case AUTHENTICATOR_APP ->
//          fieldRegistrations = List.of(this.createMfaAuthenticatorAppFieldRegistration());
//    }
//
//    return fieldRegistrations;
//  }

//  private FieldRegistration createMfaPhoneFieldRegistration() {
//    return new FieldRegistration(
//        ReservedNames.MFA_PHONE.toString(), null, FieldType.PHONE, true, true, false);
//  }
//
//  private FieldRegistration createMfaEmailFieldRegistration() {
//    return new FieldRegistration(
//        ReservedNames.MFA_EMAIL.toString(), null, FieldType.EMAIL, true, true, false);
//  }
//
//  private FieldRegistration createMfaAuthenticatorAppFieldRegistration() {
//    return new FieldRegistration(
//        ReservedNames.MFA_AUTHENTICATOR_APP_SECRET.toString(),
//        null,
//        FieldType.SECRET,
//        true,
//        true,
//        false);
//  }

  public @NonNull OrgId orgId() {
    return this.orgId;
  }

  public @NonNull OuId id() {
    return this.id;
  }

  public @NonNull String name() {
    return this.name;
  }

  public boolean isTopLevel() {
    return this.isTopLevel;
  }

  public OuId parent() {
    return this.parent;
  }

  public @NonNull AuthorizationPolicy authorizationPolicy() {
    return this.authorizationPolicy;
  }

  public @NonNull AuthenticationPolicy authenticationPolicy() {
    return this.authenticationPolicy;
  }
}
