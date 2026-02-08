package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry.LicenseRestrictionRegistry;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import com.vayl.identityAccess.core.domain.role.Role;
import com.vayl.identityAccess.core.domain.role.RoleId;
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
      @NonNull List<LicenseContractId> licenseContractIds,
      @NonNull List<Role> roles,
      @NonNull LicenseRestrictionRegistry registry) {
    this.throwErrorIfLicenseRegistryIsNull(registry);
    this.throwErrorIfLicenseContractsInDifferentOrg(licenseContractIds);
    this.throwErrorIfRolesInDifferentOrg(roles);

    boolean isOuAnAudience = false;
    boolean isUaAnAudience = false;
    List<RoleId> accessibleRoleIds = new ArrayList<RoleId>();
    List<LicenseContractId> accessibleContractIds = new ArrayList<LicenseContractId>();

    if (!(licenseContractIds.isEmpty())) {
      accessibleContractIds = licenseContractIds;
      isUaAnAudience =
          !(this.authorizationPolicy().isLicenseContractsEquals(accessibleContractIds));
      isOuAnAudience =
          !(this.authorizationPolicy().isLicenseContractsEquals(accessibleContractIds));
    }

    registry.initializeAssignedLicense(getLicenseIdsFromContractsId(accessibleContractIds));
    this.throwErrorIfRolesIsNotAllowedByRegistry(roles, registry);

    if (!(roles.isEmpty())) {
      accessibleRoleIds = this.getRoleIds(roles);
      isOuAnAudience =
          !(this.authorizationPolicy().isRoleIdsEquals(accessibleRoleIds)) || isOuAnAudience;
    }

    this.setAuthorizationPolicy(
        new AuthorizationPolicy(accessibleContractIds, accessibleRoleIds, false));

    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUaAnAudience);
  }

  private void throwErrorIfLicenseRegistryIsNull(LicenseRestrictionRegistry registry) {
    if (registry == null) {
      throw new InvalidValueException(
          ExceptionEvent.UPDATING_AUTHORIZATION_POLICY,
          ExceptionReason.LICENSE_RESTRICTION_REGISTRY_NOT_PROVIDED,
          null,
          ExceptionLevel.ERROR);
    }
  }

  private void throwErrorIfLicenseContractsInDifferentOrg(
      @NonNull List<LicenseContractId> licenseContractIds) {

    for (LicenseContractId licenseContractId : licenseContractIds) {
      if (!(licenseContractId.orgId() == this.orgId())) {
        throw new InvalidValueException(
            ExceptionEvent.UPDATING_AUTHORIZATION_POLICY,
            ExceptionReason.LICENSE_AND_OU_ORG_CONFLICT,
            licenseContractId.orgId().toString(),
            ExceptionLevel.INFO);
      }
    }
  }

  private void throwErrorIfRolesInDifferentOrg(@NonNull List<Role> roles) {

    for (Role role : roles) {
      if (!(role.belongsTo(this.orgId()))) {
        throw new InvalidValueException(
            ExceptionEvent.UPDATING_AUTHORIZATION_POLICY,
            ExceptionReason.ROLES_AND_OU_ORG_CONFLICT,
            "role orgId: " + role.id().toString() + ", OU orgId: " + this.orgId().toString(),
            ExceptionLevel.INFO);
      }
    }
  }

  private List<LicenseId> getLicenseIdsFromContractsId(List<LicenseContractId> licenseContractIds) {
    List<LicenseId> licenseIds = new ArrayList<>();

    for (LicenseContractId licenseContractId : licenseContractIds) {
      licenseIds.add(licenseContractId.licenseId());
    }

    return licenseIds;
  }

  private void throwErrorIfRolesIsNotAllowedByRegistry(
      List<Role> roles, LicenseRestrictionRegistry registry) {

    for (Role role : roles) {
      if (!(registry.canAccessLicenseRestrictable(role.assignedApi()))) {
        throw new InvalidValueException(
            ExceptionEvent.UPDATING_AUTHORIZATION_POLICY,
            ExceptionReason.ACCESS_DENIED_TO_RESTRICTABLE_BY_LICENSE_RESTRICTION_REGISTRY,
            role.assignedApi().toString(),
            ExceptionLevel.INFO);
      }
    }
  }

  private List<RoleId> getRoleIds(List<Role> roles) {
    List<RoleId> roleIds = new ArrayList<RoleId>();

    for (Role role : roles) {
      if (role.belongsTo(this.orgId())) {
        roleIds.add(role.id());
      }
    }

    return roleIds;
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

  private void throwExceptionIfAssigningChildInDifferentOrg(Ou ou) {
    if (!this.orgId().equals(ou.orgId())) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ASSIGNMENT,
          ExceptionReason.PARENT_AND_CHILD_ORG_CONFLICT,
          ou.orgId().toString(),
          ExceptionLevel.INFO);
    }
  }

  private void throwExceptionIfAssigningSelf(Ou childOu) {
    if (childOu.id() == this.id()) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ASSIGNMENT,
          ExceptionReason.ASSIGNING_SELF,
          childOu.id().toString(),
          ExceptionLevel.INFO);
    }
  }

  private void throwExceptionIfAssigningParent(@NonNull Ou childOu) {
    if (this.parent().equals(childOu.id())) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ASSIGNMENT,
          ExceptionReason.ASSIGNING_PARENT_TO_CHILD,
          childOu.id().toString(),
          ExceptionLevel.INFO);
    }
  }

  private void throwExceptionIfChildIsTopLevel(Ou childOu) {
    if (childOu.isTopLevel()) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ASSIGNMENT,
          ExceptionReason.ASSIGNING_TOP_LEVEL_OU_TO_PARENT,
          childOu.id().toString(),
          ExceptionLevel.INFO);
    }
  }

  public void synchronizeAuthorizationPolicyWith(Ou parentOu) {
    this.throwExceptionIfNotChildOf(parentOu);

    boolean isOuAnAudience = false;
    boolean isUserAnAudience = false;
    AuthorizationPolicy parentAuthorizationPolicy = parentOu.authorizationPolicy();

    if (!parentAuthorizationPolicy
        .assignedLicenseContracts()
        .equals(authorizationPolicy.assignedLicenseContracts())) {
      isOuAnAudience = true;
      isUserAnAudience = true;
    }

    if (!parentAuthorizationPolicy.assignedRoles().equals(authorizationPolicy.assignedRoles())) {
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
          ExceptionEvent.SYNCHRONIZING_OU_WITH_PARENT,
          ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT,
          parentOu.id().toString(),
          ExceptionLevel.INFO);
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
