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

public class Ou {
  private OrgId orgId;
  private OuId id;
  private boolean isTopLevel;
  private OuId parent;
  private AuthorizationPolicy authorizationPolicy;
  private AuthenticationPolicy authenticationPolicy;

  public Ou(
      OrgId orgId,
      OuId id,
      boolean isTopLevel,
      OuId parent,
      AuthorizationPolicy authorizationPolicy,
      AuthenticationPolicy authenticationPolicy) {
    this.setOrgId(orgId);
    this.setId(id);
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

  public void assignAuthorizationPolicy(AuthorizationPolicy authorizationPolicy) {
    this.setAuthorizationPolicy(authorizationPolicy);
  }

  public void assignAuthenticationPolicy(AuthenticationPolicy authenticationPolicy) {
    this.setAuthenticationPolicy(authenticationPolicy);
  }

  public void updateParent(OuId parentId) {
    this.setParent(parentId);
  }

  public Ou createOu(String name) {
    OuId newOuId = new OuId(UUID.randomUUID().toString());
    AuthorizationPolicy newAuthorizationPolicy =
        this.authorizationPolicy().copyWith(null, null, true);
    AuthenticationPolicy newAuthenticationPolicy =
        this.authenticationPolicy().copyWith(null, null, true);

    return new Ou(
        this.orgId(), newOuId, false, this.id(), newAuthorizationPolicy, newAuthenticationPolicy);
  }

  public void updateAuthenticationPolicy(MfaPolicy mfaPolicy, RecoveryPolicy recoveryPolicy) {
    boolean shouldPublishEvent = false;
    shouldPublishEvent = !(this.authenticationPolicy().mfaPolicy().equals(mfaPolicy));
    shouldPublishEvent =
        this.authenticationPolicy().recoveryPolicy().equals(recoveryPolicy) || shouldPublishEvent;

    this.publishUpdateOuAuthenticationPolicyEvent(shouldPublishEvent);
  }

  public void updateAuthorizationPolicy(
      List<LicenseContractId> licenseContractIds,
      List<Role> roles,
      LicenseRestrictionRegistry registry) {
    this.throwErrorIfLicenseRegistryIsNull(registry);

    boolean isOuAnAudience = false;
    boolean isUaAnAudience = false;
    List<RoleId> accessibleRoleIds;
    List<LicenseContractId> accessibleContractIds;

    if (!(licenseContractIds == null)) {
      accessibleContractIds = this.filterOrgLicenseContractIds(licenseContractIds);
      isUaAnAudience =
          this.authorizationPolicy().isLicenseContractsDifferent(accessibleContractIds);
      isOuAnAudience =
          this.authorizationPolicy().isLicenseContractsDifferent(accessibleContractIds);
    } else {
      accessibleContractIds = this.authorizationPolicy().assignedLicenseContracts();
    }

    registry.initializeAssignedLicense(getLicenseIdsFromContractsId(accessibleContractIds));

    if (!(roles == null)) {
      accessibleRoleIds = filterOrgRoleIds(rolesAllowedByRegistry(roles, registry));
      isOuAnAudience =
          this.authorizationPolicy().isRolesDifferent(accessibleRoleIds) || isOuAnAudience;
    } else {
      accessibleRoleIds = this.authorizationPolicy().assignedRoles();
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

  private List<LicenseContractId> filterOrgLicenseContractIds(
      List<LicenseContractId> licenseContractIds) {
    List<LicenseContractId> orgLicenseContractIds = new ArrayList<LicenseContractId>();

    for (LicenseContractId licenseContractId : licenseContractIds) {
      if (licenseContractId.orgId() == this.orgId()) {
        orgLicenseContractIds.add(licenseContractId);
      }
    }

    return orgLicenseContractIds;
  }

  private List<LicenseId> getLicenseIdsFromContractsId(List<LicenseContractId> licenseContractIds) {
    List<LicenseId> licenseIds = new ArrayList<>();

    for (LicenseContractId licenseContractId : licenseContractIds) {
      licenseIds.add(licenseContractId.licenseId());
    }

    return licenseIds;
  }

  private List<Role> rolesAllowedByRegistry(List<Role> roles, LicenseRestrictionRegistry registry) {
    List<Role> accessibleRoles = new ArrayList<Role>();

    for (Role role : roles) {
      if (registry.canAccessLicenseRestrictable(role.assignedApi())) {
        accessibleRoles.add(role);
      }
    }

    return accessibleRoles;
  }

  private List<RoleId> filterOrgRoleIds(List<Role> roles) {
    List<RoleId> accessibleRoles = new ArrayList<RoleId>();

    for (Role role : roles) {
      if (role.belongsTo(this.orgId())) {
        accessibleRoles.add(role.id());
      }
    }

    return accessibleRoles;
  }

  public void assignOu(
      Ou childOu,
      boolean shouldInheritAuthorizationPolicy,
      boolean shouldInheritAuthenticationPolicy) {
    if (childOu.parent() == this.id()) {
      return;
    }
    throwExceptionIfNotSameOrgWith(childOu);
    throwExceptionIfChildIsTopLevel(childOu);

    childOu.updateParent(this.id());

    if (shouldInheritAuthorizationPolicy) {
      childOu.synchronizeAuthorizationPolicyWith(this);
    } else {
      AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();
      AuthorizationPolicy newChildAuthorizationPolicy =
          childAuthorizationPolicy.copyWith(null, null, false);
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

    return;
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
    this.throwExceptionIfNotSameOrgWith(parentOu);
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

    this.setAuthorizationPolicy(parentAuthorizationPolicy.copyWith(null, null, true));
    this.publishUpdateOuAuthorizationPolicyEvent(isOuAnAudience, isUserAnAudience);
  }

  public void synchronizeAuthenticationPolicyWith(Ou parentOu) {
    this.throwExceptionIfNotSameOrgWith(parentOu);
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

  private void throwExceptionIfNotSameOrgWith(Ou ou) {
    if (!this.orgId().equals(ou.orgId())) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ASSIGNMENT,
          ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT,
          "child OU OrgId: "
              + this.orgId().toString()
              + ", parent OU OrgId: "
              + ou.orgId().toString(),
          ExceptionLevel.ERROR);
    }
  }

  private void throwExceptionIfNotChildOf(Ou parentOu) {
    if (!this.parent().equals(parentOu.id())) {
      throw new InvalidValueException(
          ExceptionEvent.OU_ASSIGNMENT,
          ExceptionReason.OU_ORG_MISMATCH,
          "child OU orgId: "
              + this.orgId().toString()
              + ", parent OU Id: "
              + parentOu.orgId().toString(),
          ExceptionLevel.ERROR);
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
