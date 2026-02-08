package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry.LicenseRestriction;
import com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry.LicenseRestrictionRegistry;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.Ou;
import com.vayl.identityAccess.core.domain.organization.ou.OuId;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.AuthenticationPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.MfaPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authenticationPolicy.RecoveryPolicy;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import com.vayl.identityAccess.core.domain.role.Role;
import com.vayl.identityAccess.core.domain.role.RoleId;
import java.util.*;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OuTest {

  private static final Date DEFAULT_MFA_EXPIRY = new Date("2023-12-01T00:00:00Z");

  private List<Role> unrestrictedApiRoles;

  private Api restrictedApi;
  private List<Role> restrictedApiRoles;
  private LicenseRestrictionRegistry licenseRestrictionRegistry;

  private OrgId orgId;
  private List<LicenseContractId> blackListedLicenseContractIds;
  private List<LicenseContractId> whiteListedLicenseContractIds;

  private Ou topLevelOu;

  @BeforeAll
  public void initAll() {
    // domain ids and APIs
    this.orgId = new OrgId(UUID.randomUUID().toString());

    Api unrestrictedApi = this.createApi("free.com", "email-app");
    this.unrestrictedApiRoles = List.of(unrestrictedApi.createDefaultRole("admin", List.of()));

    this.restrictedApi = this.createApi("subscribe.com", "bank-app");
    this.restrictedApiRoles = List.of(this.restrictedApi.createDefaultRole("admin", List.of()));

    // licenses and license contracts
    List<LicenseId> blackListedLicenseIds = createLicenseIds(3);
    List<LicenseId> whiteListedLicenseIds = createLicenseIds(3);

    this.blackListedLicenseContractIds = createLicenseContractId(orgId, blackListedLicenseIds);
    this.whiteListedLicenseContractIds = createLicenseContractId(orgId, whiteListedLicenseIds);

    // license restriction registry restricting access to restricted api
    this.licenseRestrictionRegistry =
        new LicenseRestrictionRegistry(
            List.of(
                createLicenseRestrictions(
                    restrictedApi.id(), new HashSet<>(blackListedLicenseIds))));

    // initial OU under test
    this.topLevelOu =
        createOu(
            "admin",
            true,
            orgId,
            this.whiteListedLicenseContractIds,
            this.restrictedApiRoles.stream().map(Role::id).toList(),
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
            .assignedLicenseContracts()
            .equals(parentauthorizationPolicy.assignedLicenseContracts())
        : "Child OU should inherit license contracts from parent OU";
    assert childAuthorizationPolicy
            .assignedRoles()
            .equals(parentauthorizationPolicy.assignedRoles())
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
      updateAuthorizationPolicy_ifOuDoesNotHaveLicenseToUnrestrictedApi_shouldUpdateAuthorizationPolicy() {
    topLevelOu.updateAuthorizationPolicy(
        this.blackListedLicenseContractIds, this.unrestrictedApiRoles, licenseRestrictionRegistry);

    AuthorizationPolicy updatedAuthorizationPolicy = topLevelOu.authorizationPolicy();

    assert !updatedAuthorizationPolicy.isInherited()
        : "Updated authorization policy should not be inherited";
    assert updatedAuthorizationPolicy
            .assignedLicenseContracts()
            .equals(this.blackListedLicenseContractIds)
        : "Updated authorization policy should have the new license contracts assigned";
    assert updatedAuthorizationPolicy
            .assignedRoles()
            .equals(this.unrestrictedApiRoles.stream().map(Role::id).toList())
        : "Updated authorization policy should have the new roles assigned";
  }

  @Test
  public void
      updateAuthorizationPolicy_ifOuDoesNotHaveLicenseToRestrictedApi_shouldThrowException() {

    try {
      topLevelOu.updateAuthorizationPolicy(
          blackListedLicenseContractIds, this.restrictedApiRoles, licenseRestrictionRegistry);
      assert false
          : "Expected an exception to be thrown due to missing required license for restricted API";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.UPDATING_AUTHORIZATION_POLICY)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.UPDATING_AUTHORIZATION_POLICY;
      assert e.reason()
              .equals(ExceptionReason.ACCESS_DENIED_TO_RESTRICTABLE_BY_LICENSE_RESTRICTION_REGISTRY)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.ACCESS_DENIED_TO_RESTRICTABLE_BY_LICENSE_RESTRICTION_REGISTRY;
      assert e.invalidValue().equals(this.restrictedApi.id().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + this.restrictedApi.id().toString();
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
    }
  }

  @Test
  public void
      updateAuthorizationPolicy_OuHasLicenseToRestrictedApi_shouldUpdateAuthorizationPolicy() {

    topLevelOu.updateAuthorizationPolicy(
        whiteListedLicenseContractIds, this.restrictedApiRoles, licenseRestrictionRegistry);

    AuthorizationPolicy updatedAuthorizationPolicy = topLevelOu.authorizationPolicy();

    assert !updatedAuthorizationPolicy.isInherited()
        : "Updated authorization policy should not be inherited";
    assert updatedAuthorizationPolicy
            .assignedLicenseContracts()
            .equals(this.whiteListedLicenseContractIds)
        : "Updated authorization policy should have the new license contracts assigned";
    assert updatedAuthorizationPolicy
            .assignedRoles()
            .equals(this.restrictedApiRoles.stream().map(Role::id).toList())
        : "Updated authorization policy should have the new roles assigned";
  }

  @Test
  public void
      updateAuthorizationPolicy_OuHasLicenseContractThatBelongsToDifferentOrg_shouldThrowException() {
    OrgId differentOrgId = new OrgId(UUID.randomUUID().toString());
    Ou billingOu =
        this.createOu(
            "billing",
            false,
            differentOrgId,
            List.of(),
            List.of(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, new Date("2023-12-01T00:00:00Z")));

    try {
      billingOu.updateAuthorizationPolicy(
          this.whiteListedLicenseContractIds, this.restrictedApiRoles, licenseRestrictionRegistry);

      assert false
          : "Expected an exception to be thrown due to license contracts belonging to different org";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.UPDATING_AUTHORIZATION_POLICY)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.UPDATING_AUTHORIZATION_POLICY;
      assert e.reason().equals(ExceptionReason.LICENSE_AND_OU_ORG_CONFLICT)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.LICENSE_AND_OU_ORG_CONFLICT;
      assert e.invalidValue().equals(this.orgId.toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + this.orgId.toString();
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
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
      assignOu_OuInTheSameOrgWithInheritanceToggleActive_shouldAssignChildToParentAndSynchronizeParentAndChild() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.blackListedLicenseContractIds,
            this.unrestrictedApiRoles.stream().map(Role::id).toList(),
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
            .assignedLicenseContracts()
            .equals(parentAuthorizationPolicy.assignedLicenseContracts())
        : "Child OU should inherit license contracts from parent OU";
    assert childAuthorizationPolicy
            .assignedRoles()
            .equals(parentAuthorizationPolicy.assignedRoles())
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
      assignOu_OuInTheSameOWithInheritanceToggleInactive_shouldAssignChildToParentButNotSynchronizeParentAndChild() {
    Ou childOu =
        this.createOu(
            "accounting",
            false,
            this.orgId,
            this.blackListedLicenseContractIds,
            this.unrestrictedApiRoles.stream().map(Role::id).toList(),
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
            .assignedLicenseContracts()
            .equals(oldAuthorizationPolicy.assignedLicenseContracts())
        : "Child OU should inherit license contracts from parent OU";
    assert newAuthorizationPolicy.assignedRoles().equals(oldAuthorizationPolicy.assignedRoles())
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
      assert e.event().equals(ExceptionEvent.OU_ASSIGNMENT)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.OU_ASSIGNMENT;
      assert e.reason().equals(ExceptionReason.PARENT_AND_CHILD_ORG_CONFLICT)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.PARENT_AND_CHILD_ORG_CONFLICT;
      assert e.invalidValue().equals(childOu.orgId().toString())
          : "Exception invalid value mismatch got: "
              + e.invalidValue()
              + " expected: "
              + childOu.orgId().toString();
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
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
      assert e.event().equals(ExceptionEvent.OU_ASSIGNMENT)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.OU_ASSIGNMENT;
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
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
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
      assert e.event().equals(ExceptionEvent.OU_ASSIGNMENT)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.OU_ASSIGNMENT;
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
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
    }
  }

  @Test
  public void assignOu_ifAssignSelf_throwException() {
    Ou ou = this.topLevelOu.createOu("warehouse");

    try {
      ou.assignOu(ou, true, true);

      assert false : "Expected an exception to be thrown when assigning parent ou to child ou";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.OU_ASSIGNMENT)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.OU_ASSIGNMENT;
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
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
    }
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_ifChildIsAssignedToParent_synchronize() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.blackListedLicenseContractIds,
            this.unrestrictedApiRoles.stream().map(Role::id).toList(),
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
            .assignedLicenseContracts()
            .equals(parentAuthorizationPolicy.assignedLicenseContracts())
        : "Child OU should inherit license contracts from parent OU";
    assert childAuthorizationPolicy
            .assignedRoles()
            .equals(parentAuthorizationPolicy.assignedRoles())
        : "Child OU should inherit roles from parent OU";
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_ifChildNotAssignedToParent_throwException() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.blackListedLicenseContractIds,
            this.unrestrictedApiRoles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    try {
      childOu.synchronizeAuthorizationPolicyWith(this.topLevelOu);

      assert false
          : "Expected an exception to be thrown when synchronizing ou with ou that is not parent";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.SYNCHRONIZING_OU_WITH_PARENT)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.SYNCHRONIZING_OU_WITH_PARENT;
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
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
    }
  }

  @Test
  public void synchronizeAuthenticationPolicyWith_ifChildIsAssignedToParent_synchronize() {
    Ou childOu =
        this.createOu(
            "developers",
            false,
            this.orgId,
            this.blackListedLicenseContractIds,
            this.unrestrictedApiRoles.stream().map(Role::id).toList(),
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
            this.blackListedLicenseContractIds,
            this.unrestrictedApiRoles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    try {
      childOu.synchronizeAuthenticationPolicyWith(this.topLevelOu);

      assert false
          : "Expected an exception to be thrown when synchronizing ou with ou that is not parent";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.SYNCHRONIZING_OU_WITH_PARENT)
          : "Exception event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.SYNCHRONIZING_OU_WITH_PARENT;
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
      assert e.level().equals(ExceptionLevel.INFO)
          : "Exception level mismatch got: " + e.event() + " expected: " + ExceptionLevel.INFO;
    }
  }

  private Api createApi(String audience, String name) {
    ApiId apiId = new ApiId(audience);
    return new Api(apiId, name);
  }

  private List<LicenseId> createLicenseIds(int amount) {
    List<LicenseId> licenseIds = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
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

  private LicenseRestriction createLicenseRestrictions(
      LicenseRestrictable restrictable, Set<LicenseId> licenseIds) {
    return new LicenseRestriction(restrictable, licenseIds);
  }

  private @NonNull Ou createOu(
      String name,
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
