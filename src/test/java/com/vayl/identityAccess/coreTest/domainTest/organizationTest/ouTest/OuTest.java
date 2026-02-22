package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.role.Role;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.Ou;
import com.vayl.identityAccess.core.domain.organization.ou.OuId;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import java.util.*;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OuTest {

  private static final Date DEFAULT_MFA_EXPIRY = new Date("2023-12-01T00:00:00Z");

  private Api api;
  private List<LicenseId> licenseIds;
  private OrgId orgId;
  private List<Role> roles;

  private List<LicenseContractId> licenseContractIds;

  private Ou topLevelOu;

  @BeforeAll
  public void initAll() {
    // domain ids and APIs
    this.orgId = new OrgId(UUID.randomUUID().toString());

    this.api = this.createApi();
    this.roles = List.of(this.api.createDefaultRole("admin", List.of()));

    // licenses and org license contracts
    this.licenseIds = createLicenseIds();
    this.licenseContractIds = createLicenseContractId(this.orgId, this.licenseIds);

    // initial OU under test
    this.topLevelOu =
        createOu(
            "admin",
            true,
            orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, DEFAULT_MFA_EXPIRY));
  }

  @Test
  public void createOu_withParentOu_shouldCreateOuWithCorrectFields() {

    Ou childOu = this.topLevelOu.createOu("developers");

    AuthorizationPolicy parentauthorizationPolicy = this.topLevelOu.authorizationPolicy();
    AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();

    AuthenticationPolicy parentAuthenticationPolicy = this.topLevelOu.authenticationPolicy();
    AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("developers") : "Child OU name should be 'developers'";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "Child OU parentId should match parent OU id";
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert childAuthorizationPolicy.isInherited()
        : "Child OU authorization policy should be inherited";
    assert childAuthorizationPolicy
            .assignedLicenseContractIds()
            .equals(parentauthorizationPolicy.assignedLicenseContractIds())
        : "Child OU should inherit license contracts from parent OU";
    assert childAuthorizationPolicy
            .assignedRoleIds()
            .equals(parentauthorizationPolicy.assignedRoleIds())
        : "Child OU should inherit roles from parent OU";
    assert childAuthenticationPolicy.isInherited()
        : "Child OU authentication policy should be inherited";
    assert childAuthenticationPolicy.mfaPolicy().equals(parentAuthenticationPolicy.mfaPolicy())
        : "Child OU should inherit MFA type from parent OU";
    assert childAuthenticationPolicy
            .recoveryPolicy()
            .equals(parentAuthenticationPolicy.recoveryPolicy())
        : "Child OU should inherit recovery policy from parent OU";
  }

  @Test
  public void
      updateAuthorizationPolicy_ifContractsAndRolesBelongToOrg_shouldUpdateAuthorizationPolicy() {
    topLevelOu.updateAuthorizationPolicy(this.licenseContractIds, this.roles);

    AuthorizationPolicy updatedAuthorizationPolicy = topLevelOu.authorizationPolicy();

    assert !updatedAuthorizationPolicy.isInherited()
        : "Updated authorization policy should not be inherited";
    assert updatedAuthorizationPolicy.assignedLicenseContractIds().equals(this.licenseContractIds)
        : "Updated authorization policy should have the new license contracts assigned";
    assert updatedAuthorizationPolicy
            .assignedRoleIds()
            .equals(this.roles.stream().map(Role::id).toList())
        : "Updated authorization policy should have the new roles assigned";
  }

  @Test
  public void
      updateAuthorizationPolicy_OuHasLicenseContractThatBelongsToDifferentOrg_shouldThrowException() {
    OrgId differentOrgId = new OrgId(UUID.randomUUID().toString());
    List<LicenseContractId> unauthorizedLicenseContracts =
        this.createLicenseContractId(differentOrgId, this.licenseIds);

    try {
      topLevelOu.updateAuthorizationPolicy(unauthorizedLicenseContracts, this.roles);

      assert false
          : "Expected an exception to be thrown due to license contracts belonging to different org";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.LICENSE_BELONGS_TO_DIFFERENT_ORG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.LICENSE_BELONGS_TO_DIFFERENT_ORG;
      assert e.invalidValue().equals(unauthorizedLicenseContracts.getFirst().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + unauthorizedLicenseContracts.getFirst().toString();
    }
  }

  @Test
  public void
      updateAuthenticationPolicy_withMfaAndRecoveryPolicy_shouldUpdateAuthenticationPolicy() {
    MfaPolicy newMfaPolicy = new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY);
    RecoveryPolicy newRecoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);

    this.topLevelOu.updateAuthenticationPolicy(newMfaPolicy, newRecoveryPolicy);

    assert topLevelOu.authenticationPolicy().mfaPolicy().equals(newMfaPolicy)
        : "updated authenticationPolicy should have new mfaPolicy";

    assert topLevelOu.authenticationPolicy().recoveryPolicy().equals(newRecoveryPolicy)
        : "updated authenticationPolicy should have new mfaPolicy";
  }

  @Test
  public void
      assignOu_OusInTheSameOrgAndInheritanceToggleActive_shouldAssignChildToParentAndSynchronizeParentAndChild() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    this.topLevelOu.assignOu(childOu, true, true);

    AuthorizationPolicy parentAuthorizationPolicy = this.topLevelOu.authorizationPolicy();
    AuthenticationPolicy parentAuthenticationPolicy = this.topLevelOu.authenticationPolicy();

    AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();
    AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("developers") : "Child OU name should be 'developers'";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "Child OU parentId should match parent OU id";
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert childAuthorizationPolicy.isInherited()
        : "Child OU authorization policy should be inherited";
    assert childAuthorizationPolicy
            .assignedLicenseContractIds()
            .equals(parentAuthorizationPolicy.assignedLicenseContractIds())
        : "Child OU should inherit license contracts from parent OU";
    assert childAuthorizationPolicy
            .assignedRoleIds()
            .equals(parentAuthorizationPolicy.assignedRoleIds())
        : "Child OU should inherit roles from parent OU";
    assert childAuthenticationPolicy.isInherited()
        : "Child OU authentication policy should be inherited";
    assert childAuthenticationPolicy.mfaPolicy().equals(parentAuthenticationPolicy.mfaPolicy())
        : "Child OU should inherit MFA type from parent OU";
    assert childAuthenticationPolicy
            .recoveryPolicy()
            .equals(parentAuthenticationPolicy.recoveryPolicy())
        : "Child OU should inherit recovery policy from parent OU";
  }

  @Test
  public void
      assignOu_OusInTheSameOrgAndInheritanceToggleInactive_shouldAssignChildToParentButNotSynchronizeParentAndChild() {
    Ou childOu =
        this.createOu(
            "accounting",
            false,
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    AuthorizationPolicy oldAuthorizationPolicy = childOu.authorizationPolicy();
    AuthenticationPolicy oldAuthenticationPolicy = childOu.authenticationPolicy();

    this.topLevelOu.assignOu(childOu, false, false);

    AuthorizationPolicy newAuthorizationPolicy = childOu.authorizationPolicy();
    AuthenticationPolicy newAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("accounting") : "Child OU name should be 'developers'";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "Child OU parentId should match parent OU id";
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert !newAuthorizationPolicy.isInherited()
        : "Child OU authorization policy should be inherited";
    assert newAuthorizationPolicy
            .assignedLicenseContractIds()
            .equals(oldAuthorizationPolicy.assignedLicenseContractIds())
        : "Child OU should inherit license contracts from parent OU";
    assert newAuthorizationPolicy.assignedRoleIds().equals(oldAuthorizationPolicy.assignedRoleIds())
        : "Child OU should inherit roles from parent OU";
    assert !newAuthenticationPolicy.isInherited()
        : "Child OU authentication policy should be inherited";
    assert newAuthenticationPolicy.mfaPolicy().equals(oldAuthenticationPolicy.mfaPolicy())
        : "Child OU should inherit MFA type from parent OU";
    assert newAuthenticationPolicy.recoveryPolicy().equals(oldAuthenticationPolicy.recoveryPolicy())
        : "Child OU should inherit recovery policy from parent OU";
  }

  @Test
  public void assignOu_IfChildOuIsInADifferentOrg_throwException() {
    OrgId differentOrgId = new OrgId(UUID.randomUUID().toString());
    Ou childOu =
        this.createOu(
            "billing",
            false,
            differentOrgId,
            List.of(),
            List.of(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, new Date("2023-12-01T00:00:00Z")));

    try {
      this.topLevelOu.assignOu(childOu, true, true);

      assert false
          : "Expected an exception to be thrown due to parent ou and child ou org conflict";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.PARENT_AND_CHILD_ORG_CONFLICT)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.PARENT_AND_CHILD_ORG_CONFLICT;
      assert e.invalidValue().equals(childOu.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + childOu.id().toString();
    }
  }

  @Test
  public void assignOu_ifTopLevelOuIsAssignedToOu_throwException() {
    Ou ou =
        this.createOu(
            "warehouse",
            false,
            this.orgId,
            List.of(),
            List.of(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, new Date("2023-12-01T00:00:00Z")));

    try {
      ou.assignOu(this.topLevelOu, true, true);

      assert false : "Expected an exception to be thrown when assigning top-level ou to an ou";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.ASSIGNING_TOP_LEVEL_OU_TO_PARENT)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ASSIGNING_TOP_LEVEL_OU_TO_PARENT;
      assert e.invalidValue().equals(this.topLevelOu.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + this.topLevelOu.id().toString();
    }
  }

  @Test
  public void assignOu_ifParentIsAssignedToChild_throwException() {
    Ou parentOu = this.topLevelOu.createOu("warehouse");
    Ou childOu = parentOu.createOu("auditors");

    try {
      childOu.assignOu(parentOu, true, true);

      assert false : "Expected an exception to be thrown when assigning parent ou to child ou";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.ASSIGNING_PARENT_TO_CHILD)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ASSIGNING_PARENT_TO_CHILD;
      assert e.invalidValue().equals(parentOu.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + parentOu.id().toString();
    }
  }

  @Test
  public void assignOu_ifAssignSelf_throwException() {
    Ou ou = this.topLevelOu.createOu("warehouse");

    try {
      ou.assignOu(ou, true, true);

      assert false : "Expected an exception to be thrown when assigning parent ou to child ou";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.ASSIGNING_SELF)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ASSIGNING_SELF;
      assert e.invalidValue().equals(ou.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + ou.id().toString();
    }
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_ifChildIsAssignedToParent_synchronize() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    // assign child ou to parent ou but does not synchronize child authorizationPolicy with parent's
    this.topLevelOu.assignOu(childOu, false, false);

    childOu.synchronizeAuthorizationPolicyWith(this.topLevelOu);

    AuthorizationPolicy parentAuthorizationPolicy = this.topLevelOu.authorizationPolicy();

    AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();

    assert childOu.name().equals("developers") : "Child OU name should be 'developers'";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "Child OU parentId should match parent OU id";
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert childAuthorizationPolicy.isInherited()
        : "Child OU authorization policy should be inherited";
    assert childAuthorizationPolicy
            .assignedLicenseContractIds()
            .equals(parentAuthorizationPolicy.assignedLicenseContractIds())
        : "Child OU should inherit license contracts from parent OU";
    assert childAuthorizationPolicy
            .assignedRoleIds()
            .equals(parentAuthorizationPolicy.assignedRoleIds())
        : "Child OU should inherit roles from parent OU";
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_ifChildNotAssignedToParent_throwException() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    try {
      childOu.synchronizeAuthorizationPolicyWith(this.topLevelOu);

      assert false
          : "Expected an exception to be thrown when synchronizing ou with ou that is not parent";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT;
      assert e.invalidValue().equals(this.topLevelOu.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + this.topLevelOu.id().toString();
    }
  }

  @Test
  public void synchronizeAuthenticationPolicyWith_ifChildIsAssignedToParent_synchronize() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    // assign child ou to parent ou but does not synchronize child authenticationPolicy with
    // parent's
    this.topLevelOu.assignOu(childOu, false, false);

    childOu.synchronizeAuthenticationPolicyWith(this.topLevelOu);

    AuthenticationPolicy parentAuthenticationPolicy = this.topLevelOu.authenticationPolicy();
    AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("developers") : "Child OU name should be 'developers'";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "Child OU parentId should match parent OU id";
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert childAuthenticationPolicy.isInherited()
        : "Child OU authentication policy should be inherited";
    assert childAuthenticationPolicy.mfaPolicy().equals(parentAuthenticationPolicy.mfaPolicy())
        : "Child OU should inherit MFA type from parent OU";
    assert childAuthenticationPolicy
            .recoveryPolicy()
            .equals(parentAuthenticationPolicy.recoveryPolicy())
        : "Child OU should inherit recovery policy from parent OU";
  }

  @Test
  public void synchronizeAuthenticationPolicyWith_ifChildNotAssignedToParent_throwException() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    try {
      childOu.synchronizeAuthenticationPolicyWith(this.topLevelOu);

      assert false
          : "Expected an exception to be thrown when synchronizing ou with ou that is not parent";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.OU_NOT_ASSIGNED_TO_PARENT;
      assert e.invalidValue().equals(this.topLevelOu.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + this.topLevelOu.id().toString();
    }
  }

  private Api createApi() {
    ApiId apiId = new ApiId("subscribe.com");
    return new Api(apiId, "bank-app");
  }

  private List<LicenseId> createLicenseIds() {
    List<LicenseId> licenseIds = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      licenseIds.add(new LicenseId(UUID.randomUUID().toString()));
    }
    return licenseIds;
  }

  private @NonNull List<LicenseContractId> createLicenseContractId(
      OrgId orgId, List<LicenseId> licenseIds) {
    List<LicenseContractId> licenseContractIds = new ArrayList<>();
    for (LicenseId licenseId : licenseIds) {
      LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);
      licenseContractIds.add(licenseContractId);
    }

    return licenseContractIds;
  }

  private @NonNull Ou createOu(
      java.lang.String name,
      boolean isTopLevel,
      OrgId orgId,
      List<LicenseContractId> licenseContractIds,
      List<RoleId> roleIds,
      RecoveryPolicy recoveryPolicy,
      MfaPolicy mfaPolicy) {
    OuId ouId = new OuId(UUID.randomUUID().toString());
    OuId parentId = new OuId(UUID.randomUUID().toString());
    AuthorizationPolicy authorizationPolicy =
        new AuthorizationPolicy(licenseContractIds, roleIds, false);
    AuthenticationPolicy authenticationPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);
    return new Ou(
        orgId, ouId, name, isTopLevel, parentId, authorizationPolicy, authenticationPolicy);
  }
}
