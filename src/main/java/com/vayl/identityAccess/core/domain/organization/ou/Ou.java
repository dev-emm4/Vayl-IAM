package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.api.role.Role;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class Ou {
  private OrgId orgId;
  private OuId id;
  private String name;
  private boolean isTopLevel;
  private OuId parent;
  private AuthorizationPolicy authorizationPolicy;
  private AuthenticationPolicy authenticationPolicy;

  public Ou(
      @NonNull OrgId orgId,
      @NonNull OuId id,
      @NonNull String name,
      boolean isTopLevel,
      @NonNull OuId parent,
      @NonNull AuthorizationPolicy authorizationPolicy,
      @NonNull AuthenticationPolicy authenticationPolicy) {
    this.setOrgId(orgId);
    this.setId(id);
    this.setName(name);
    this.setIsTopLevel(isTopLevel);
    this.setParent(parent);
    this.setAuthorizationPolicy(authorizationPolicy);
    this.setAuthenticationPolicy(authenticationPolicy);
  }

  private void setOrgId(OrgId orgId) {
    AssertionConcern.isNotNull(orgId, ExceptionReason.INVALID_OU_ARG);
    this.orgId = orgId;
  }

  private void setId(OuId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_OU_ARG);
    this.id = id;
  }

  private void setName(String name) {
    AssertionConcern.isNotNull(name, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotBlank(name, ExceptionReason.INVALID_OU_ARG);
    this.name = name;
  }

  private void setIsTopLevel(boolean isTopLevel) {
    this.isTopLevel = isTopLevel;
  }

  private void setParent(OuId parent) {
    AssertionConcern.isNotNull(parent, ExceptionReason.INVALID_OU_ARG);
    this.parent = parent;
  }

  private void setAuthorizationPolicy(AuthorizationPolicy authorizationPolicy) {
    AssertionConcern.isNotNull(authorizationPolicy, ExceptionReason.INVALID_OU_ARG);
    this.authorizationPolicy = authorizationPolicy;
  }

  private void setAuthenticationPolicy(AuthenticationPolicy authenticationPolicy) {
    AssertionConcern.isNotNull(authenticationPolicy, ExceptionReason.INVALID_OU_ARG);
    this.authenticationPolicy = authenticationPolicy;
  }

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

  public @NonNull OuId parent() {
    return this.parent;
  }

  public @NonNull AuthorizationPolicy authorizationPolicy() {
    return this.authorizationPolicy;
  }

  public @NonNull AuthenticationPolicy authenticationPolicy() {
    return this.authenticationPolicy;
  }

  @Contract("_ -> new")
  public @NonNull Ou createOu(@NonNull String name) {
    OuId newOuId = new OuId(UUID.randomUUID().toString());
    AuthorizationPolicy newAuthorizationPolicy =
        this.authorizationPolicy().copyWith(List.of(), List.of(), true);
    AuthenticationPolicy newAuthenticationPolicy =
        this.authenticationPolicy().copyWith(null, null, true);

    return new Ou(
        this.orgId(),
        newOuId,
        name,
        false,
        this.id(),
        newAuthorizationPolicy,
        newAuthenticationPolicy);
  }

  public void updateAuthenticationPolicy(
      @NonNull MfaPolicy mfaPolicy, @NonNull RecoveryPolicy recoveryPolicy) {
    AssertionConcern.isNotNull(mfaPolicy, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotNull(recoveryPolicy, ExceptionReason.INVALID_OU_ARG);

    boolean shouldPublishEvent =
        !(this.isMfaPolicyAssigned(mfaPolicy)) || !(this.isRecoveryPolicyAssigned(recoveryPolicy));

    this.setAuthenticationPolicy(new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false));
    this.publishUpdateOuAuthenticationPolicyEvent(shouldPublishEvent);
  }

  public void updateAuthorizationPolicy(
      @NonNull List<LicenseContractId> licenseContractIds, @NonNull List<Role> roles) {
    AssertionConcern.isNotNull(licenseContractIds, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotNull(roles, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isTrue(
        this.isLicenseContractsInSameOrg(licenseContractIds), ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isTrue(this.isRolesInSameOrg(roles), ExceptionReason.INVALID_OU_ARG);

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
      boolean shouldInheritAuthorizationPolicy,
      boolean shouldInheritAuthenticationPolicy) {
    AssertionConcern.isNotNull(childOu, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isFalse(this.isChildInDifferentOrg(childOu), ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isFalse(this.isChildSelf(childOu), ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isFalse(this.isChildParent(childOu), ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isFalse(this.isChildTopLevel(childOu), ExceptionReason.INVALID_OU_ARG);

    if (childOu.parent() == this.id()) {
      return;
    }

    childOu.setParent(this.id());

    if (shouldInheritAuthorizationPolicy) {
      childOu.synchronizeAuthorizationPolicyWith(this);
    } else {
      AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();
      AuthorizationPolicy newChildAuthorizationPolicy =
          childAuthorizationPolicy.copyWith(List.of(), List.of(), false);
      childOu.setAuthorizationPolicy(newChildAuthorizationPolicy);
    }

    if (shouldInheritAuthenticationPolicy) {
      childOu.synchronizeAuthenticationPolicyWith(this);
    } else {
      AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();
      AuthenticationPolicy newChildAuthenticationPolicy =
          childAuthenticationPolicy.copyWith(null, null, false);
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
    return this.parent().equals(childOu.id());
  }

  private boolean isChildTopLevel(@NonNull Ou childOu) {
    return childOu.isTopLevel();
  }

  public void synchronizeAuthorizationPolicyWith(@NonNull Ou parentOu) {
    AssertionConcern.isNotNull(parentOu, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isTrue(isOuParent(parentOu), ExceptionReason.INVALID_OU_ARG);

    AuthorizationPolicy parentAuthorizationPolicy = parentOu.authorizationPolicy();
    boolean isOuAnAudience =
        !(this.isLicenseContractIdsAssigned(parentAuthorizationPolicy.licenseContractIds()))
            || !(this.isRoleIdsAssigned(parentAuthorizationPolicy.roleIds()));
    boolean isUserAnAudience =
        !(this.isLicenseContractIdsAssigned(parentAuthorizationPolicy.licenseContractIds()));

    this.setAuthorizationPolicy(parentAuthorizationPolicy.copyWith(List.of(), List.of(), true));
    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUserAnAudience);
  }

  private boolean isLicenseContractIdsAssigned(List<LicenseContractId> licenseContractIds) {
    return this.authorizationPolicy().licenseContractIds().equals(licenseContractIds);
  }

  private boolean isRoleIdsAssigned(List<RoleId> roleIds) {
    return this.authorizationPolicy().roleIds().equals(roleIds);
  }

  public void synchronizeAuthenticationPolicyWith(@NonNull Ou parentOu) {
    AssertionConcern.isNotNull(parentOu, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isTrue(this.isOuParent(parentOu), ExceptionReason.INVALID_OU_ARG);

    AuthenticationPolicy parentAuthenticationPolicy = parentOu.authenticationPolicy();
    boolean shouldPublishEvent =
        !(this.isMfaPolicyAssigned(parentAuthenticationPolicy.mfaPolicy()))
            || !(this.isRecoveryPolicyAssigned(parentAuthenticationPolicy.recoveryPolicy()));

    this.setAuthenticationPolicy(parentAuthenticationPolicy.copyWith(null, null, true));
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
}
