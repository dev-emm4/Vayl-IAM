package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.api.role.Role;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
      OrgId orgId,
      OuId id,
      String name,
      boolean isTopLevel,
      OuId parent,
      AuthorizationPolicy authorizationPolicy,
      AuthenticationPolicy authenticationPolicy) {
    this.setOrgId(orgId);
    this.setId(id);
    this.setName(name);
    this.setIsTopLevel(isTopLevel);
    this.setParent(parent);
    this.setAuthorizationPolicy(authorizationPolicy);
    this.setAuthenticationPolicy(authenticationPolicy);
  }

  private void setOrgId(OrgId orgId) {
    this.orgId = orgId;
  }

  private void setId(OuId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setIsTopLevel(boolean isTopLevel) {
    this.isTopLevel = isTopLevel;
  }

  private void setParent(OuId parent) {
    this.parent = parent;
  }

  private void setAuthorizationPolicy(AuthorizationPolicy authorizationPolicy) {
    this.authorizationPolicy = authorizationPolicy;
  }

  private void setAuthenticationPolicy(AuthenticationPolicy authenticationPolicy) {
    this.authenticationPolicy = authenticationPolicy;
  }

  public OrgId orgId() {
    return this.orgId;
  }

  public OuId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public boolean isTopLevel() {
    return this.isTopLevel;
  }

  public OuId parent() {
    return this.parent;
  }

  public AuthorizationPolicy authorizationPolicy() {
    return this.authorizationPolicy;
  }

  public AuthenticationPolicy authenticationPolicy() {
    return this.authenticationPolicy;
  }

  private void assignAuthorizationPolicy(AuthorizationPolicy authorizationPolicy) {
    this.setAuthorizationPolicy(authorizationPolicy);
  }

  private void assignAuthenticationPolicy(AuthenticationPolicy authenticationPolicy) {
    this.setAuthenticationPolicy(authenticationPolicy);
  }

  private void updateParent(OuId parentId) {
    this.setParent(parentId);
  }

  public Ou createOu(String name) {
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
    boolean shouldPublishEvent = false;
    shouldPublishEvent = !(this.authenticationPolicy().mfaPolicy().equals(mfaPolicy));
    shouldPublishEvent =
        this.authenticationPolicy().recoveryPolicy().equals(recoveryPolicy) || shouldPublishEvent;

    this.setAuthenticationPolicy(new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false));
    this.publishUpdateOuAuthenticationPolicyEvent(shouldPublishEvent);
  }

  public void updateAuthorizationPolicy(
      @NonNull List<LicenseContractId> licenseContractIds, @NonNull List<Role> roles) {
    this.throwErrorIfLicenseContractsInDifferentOrg(licenseContractIds);
    this.throwErrorIfRolesInDifferentOrg(roles);

    boolean isOuAnAudience =
        !(this.isLicenseContractsAssigned(licenseContractIds)) || !(this.isRolesAssigned(roles));
    boolean isUaAnAudience = !(this.isLicenseContractsAssigned(licenseContractIds));

    this.setAuthorizationPolicy(
        new AuthorizationPolicy(licenseContractIds, roles.stream().map(Role::id).toList(), false));

    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUaAnAudience);
  }

  private void throwErrorIfLicenseContractsInDifferentOrg(
      @NonNull List<LicenseContractId> licenseContractIds) {

    for (LicenseContractId licenseContractId : licenseContractIds) {
      if (!(licenseContractId.orgId() == this.orgId())) {
        throw new InvalidValueException(
            ExceptionReason.LICENSE_BELONGS_TO_DIFFERENT_ORG, licenseContractId.toString());
      }
    }
  }

  private void throwErrorIfRolesInDifferentOrg(@NonNull List<Role> roles) {

    for (Role role : roles) {
      if (!(role.accessibleBy(this.orgId()))) {
        throw new InvalidValueException(
            ExceptionReason.ROLES_BELONG_TO_DIFFERENT_ORG, role.id().toString());
      }
    }
  }

  private boolean isLicenseContractsAssigned(List<LicenseContractId> licenseContractIds) {
    return this.authorizationPolicy().assignedLicenseContractIds().equals(licenseContractIds);
  }

  private boolean isRolesAssigned(List<Role> roles) {
    return this.authorizationPolicy()
            .assignedRoleIds()
            .equals(roles.stream().map(Role::id).toList());
  }

  public void assignOu(
      Ou childOu,
      boolean shouldInheritAuthorizationPolicy,
      boolean shouldInheritAuthenticationPolicy) {
    if (childOu.parent() == this.id()) {
      return;
    }
    this.throwExceptionIfAssigningChildInDifferentOrg(childOu);
    this.throwExceptionIfAssigningSelf(childOu);
    this.throwExceptionIfAssigningParent(childOu);
    this.throwExceptionIfChildIsTopLevel(childOu);

    childOu.updateParent(this.id());

    if (shouldInheritAuthorizationPolicy) {
      childOu.synchronizeAuthorizationPolicyWith(this);
    } else {
      AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();
      AuthorizationPolicy newChildAuthorizationPolicy =
          childAuthorizationPolicy.copyWith(List.of(), List.of(), false);
      childOu.assignAuthorizationPolicy(newChildAuthorizationPolicy);
    }

    if (shouldInheritAuthenticationPolicy) {
      childOu.synchronizeAuthenticationPolicyWith(this);
    } else {
      AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();
      AuthenticationPolicy newChildAuthenticationPolicy =
          childAuthenticationPolicy.copyWith(null, null, false);
      childOu.assignAuthenticationPolicy(newChildAuthenticationPolicy);
    }
  }

  private void throwExceptionIfAssigningChildInDifferentOrg(Ou childOu) {
    if (!this.orgId().equals(childOu.orgId())) {
      throw new InvalidValueException(
          ExceptionReason.PARENT_AND_CHILD_ORG_CONFLICT, childOu.id().toString());
    }
  }

  private void throwExceptionIfAssigningSelf(Ou childOu) {
    if (childOu.id() == this.id()) {
      throw new InvalidValueException(ExceptionReason.ASSIGNING_SELF, childOu.id().toString());
    }
  }

  private void throwExceptionIfAssigningParent(@NonNull Ou childOu) {
    if (this.parent().equals(childOu.id())) {
      throw new InvalidValueException(
          ExceptionReason.ASSIGNING_PARENT_TO_CHILD, childOu.id().toString());
    }
  }

  private void throwExceptionIfChildIsTopLevel(Ou childOu) {
    if (childOu.isTopLevel()) {
      throw new InvalidValueException(
          ExceptionReason.ASSIGNING_TOP_LEVEL_OU_TO_PARENT, childOu.id().toString());
    }
  }

  public void synchronizeAuthorizationPolicyWith(Ou parentOu) {
    this.throwExceptionIfNotChildOf(parentOu);

    boolean isOuAnAudience = false;
    boolean isUserAnAudience = false;
    AuthorizationPolicy parentAuthorizationPolicy = parentOu.authorizationPolicy();

    if (!parentAuthorizationPolicy
        .assignedLicenseContractIds()
        .equals(authorizationPolicy.assignedLicenseContractIds())) {
      isOuAnAudience = true;
      isUserAnAudience = true;
    }

    if (!parentAuthorizationPolicy
        .assignedRoleIds()
        .equals(authorizationPolicy.assignedRoleIds())) {
      isOuAnAudience = true;
    }

    this.setAuthorizationPolicy(parentAuthorizationPolicy.copyWith(List.of(), List.of(), true));
    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUserAnAudience);
  }

  public void synchronizeAuthenticationPolicyWith(Ou parentOu) {
    this.throwExceptionIfNotChildOf(parentOu);

    boolean shouldPublishEvent = false;
    AuthenticationPolicy parentAuthenticationPolicy = parentOu.authenticationPolicy();

    if (!parentAuthenticationPolicy.mfaPolicy().equals(this.authenticationPolicy().mfaPolicy())
        || !parentAuthenticationPolicy
            .recoveryPolicy()
            .equals(this.authenticationPolicy().recoveryPolicy())) {
      shouldPublishEvent = true;
    }

    this.setAuthenticationPolicy(parentAuthenticationPolicy.copyWith(null, null, true));
    this.publishUpdateOuAuthenticationPolicyEvent(shouldPublishEvent);
  }

  private void throwExceptionIfNotChildOf(Ou parentOu) {
    if (!this.parent().equals(parentOu.id())) {
      throw new InvalidValueException(
          ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT, parentOu.id().toString());
    }
  }

  private void publishUpdateOuAuthorizationPolicyEvent(
      boolean isOuAnAudience, boolean isUserAnAudience) {
    // TODO: Publish updatedOuAuthorizationPolicy

  }

  private void publishUpdateOuAuthenticationPolicyEvent(boolean shouldPublishEvent) {
    // TODO: Publish updatedOuAuthenticationPolicy

  }
}
