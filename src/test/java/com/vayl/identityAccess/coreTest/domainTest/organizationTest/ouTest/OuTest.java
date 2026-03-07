package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.api.Api;
import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.role.Role;
import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.MfaType;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
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

  private static final DateInput DEFAULT_MFA_EXPIRY = new DateInput("2023-12-01T00:00:00Z");

  private List<LicenseId> licenseIds;
  private OrgId orgId;
  private List<Role> roles;

  private List<LicenseContractId> licenseContractIds;

  private Ou topLevelOu;

  @BeforeAll
  public void initAll() {
    // domain ids and APIs
    this.orgId = new OrgId(UUID.randomUUID().toString());

    Api api = this.createApi();
    this.roles = List.of(api.createDefaultRole("admin", List.of()));

    // licenses and org license contracts
    this.licenseIds = createLicenseIds();
    this.licenseContractIds = createLicenseContractId(this.orgId, this.licenseIds);

    // initial OU under test
    this.topLevelOu =
        createTopLevelOu(
            orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, DEFAULT_MFA_EXPIRY));
  }

  @Test
  void constructor_withNullParameters_throwException() {
    for (int i = 0; i < 4; i++) {
      try {
        new Ou(
            i == 0 ? null : this.orgId,
            i == 1 ? null : new OuId(UUID.randomUUID().toString()),
            i == 2 ? null : new AuthorizationPolicy(List.of(), List.of(), true),
            i == 3
                ? null
                : new AuthenticationPolicy(
                    new RecoveryPolicy(MfaType.EMAIL),
                    new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY),
                    true));

        assert false : "Exception expected ";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
      }
    }
  }

  @Test
  void constructor_withValidParameters_shouldCreateTopLevelOuWithCorrectFields() {
    Ou ou =
        createTopLevelOu(
            this.orgId,
            this.licenseContractIds,
            this.roles.stream().map(Role::id).toList(),
            new RecoveryPolicy(MfaType.EMAIL),
            new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY));

    assert ou.orgId().equals(this.orgId) : "got: " + ou.orgId() + " expected: " + this.orgId;
    assert ou.parent() == null : "Top-level OU should not have a parent";
    assert ou.authorizationPolicy().licenseContractIds().equals(this.licenseContractIds)
        : "got: "
            + ou.authorizationPolicy().licenseContractIds()
            + " expected: "
            + this.licenseContractIds;
    assert ou.authorizationPolicy().roleIds().equals(this.roles.stream().map(Role::id).toList())
        : "got: "
            + ou.authorizationPolicy().roleIds()
            + " expected: "
            + this.roles.stream().map(Role::id).toList();
    assert ou.authenticationPolicy()
            .mfaPolicy()
            .equals(new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY))
        : "got: "
            + ou.authenticationPolicy().mfaPolicy()
            + " expected: "
            + new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY);
    assert ou.authenticationPolicy().recoveryPolicy().equals(new RecoveryPolicy(MfaType.EMAIL))
        : "got: "
            + ou.authenticationPolicy().recoveryPolicy()
            + " expected: "
            + new RecoveryPolicy(MfaType.EMAIL);
  }

  @Test
  void createOu_withBlankName_throwException() {
    try {
      this.topLevelOu.createOu("");

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  void createOu_withNullName_throwException() {
    try {
      this.topLevelOu.createOu(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void createOu_withValidParameter_shouldCreateOuWithCorrectFields() {

    Ou childOu = this.topLevelOu.createOu("developers");

    AuthorizationPolicy parentauthorizationPolicy = this.topLevelOu.authorizationPolicy();
    AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();

    AuthenticationPolicy parentAuthenticationPolicy = this.topLevelOu.authenticationPolicy();
    AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("developers") : "got: " + childOu.name() + " expected: developers";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "Child OU parentId should match parent OU id";
    assert !childOu.isTopLevel() : "got: " + childOu.isTopLevel() + " expected: " + false;
    assert childAuthorizationPolicy.isInherited() : "got: " + false + " expected: " + true;
    assert childAuthorizationPolicy
            .licenseContractIds()
            .equals(parentauthorizationPolicy.licenseContractIds())
        : "got: "
            + childAuthorizationPolicy.licenseContractIds()
            + " expected: "
            + parentauthorizationPolicy.licenseContractIds();
    assert childAuthorizationPolicy.roleIds().equals(parentauthorizationPolicy.roleIds())
        : "got: "
            + childAuthorizationPolicy.roleIds()
            + " expected: "
            + parentauthorizationPolicy.roleIds();
    assert childAuthenticationPolicy.isInherited() : "got: " + false + " expected: true";
    assert childAuthenticationPolicy.mfaPolicy().equals(parentAuthenticationPolicy.mfaPolicy())
        : "got: "
            + childAuthenticationPolicy.mfaPolicy()
            + " expected: "
            + parentAuthenticationPolicy.mfaPolicy();
    assert childAuthenticationPolicy
            .recoveryPolicy()
            .equals(parentAuthenticationPolicy.recoveryPolicy())
        : "got: "
            + childAuthenticationPolicy.recoveryPolicy()
            + " expected: "
            + parentAuthenticationPolicy.recoveryPolicy();
    ;
  }

  @Test
  void updateAuthorizationPolicy_withNullParameters_throwException() {
    for (int i = 0; i < 2; i++) {
      try {
        this.topLevelOu.updateAuthorizationPolicy(
            i == 0 ? null : this.licenseContractIds, i == 1 ? null : this.roles);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
      }
    }
  }

  @Test
  public void
      updateAuthorizationPolicy_newContractsAndRolesBelongToTheSameOrgAsOu_shouldUpdateAuthorizationPolicy() {
    List<LicenseContractId> newLicenseContractIds =
        this.createLicenseContractId(this.orgId, this.createLicenseIds());
    List<Role> newRoles = this.createDefaultRoles(this.createApi());

    this.topLevelOu.updateAuthorizationPolicy(newLicenseContractIds, newRoles);

    AuthorizationPolicy updatedAuthorizationPolicy = topLevelOu.authorizationPolicy();

    assert !updatedAuthorizationPolicy.isInherited() : "got: " + true + " expected: " + false;
    assert updatedAuthorizationPolicy.licenseContractIds().equals(newLicenseContractIds)
        : "got: "
            + updatedAuthorizationPolicy.licenseContractIds()
            + " expected: "
            + newLicenseContractIds;
    assert updatedAuthorizationPolicy.roleIds().equals(newRoles.stream().map(Role::id).toList())
        : "got: "
            + updatedAuthorizationPolicy.roleIds()
            + " expected: "
            + newRoles.stream().map(Role::id).toList();
  }

  @Test
  public void
      updateAuthorizationPolicy_newLicenseContractBelongsToDifferentOrg_shouldThrowException() {
    OrgId differentOrgId = new OrgId(UUID.randomUUID().toString());
    List<LicenseContractId> unauthorizedLicenseContracts =
        this.createLicenseContractId(differentOrgId, this.licenseIds);

    try {
      topLevelOu.updateAuthorizationPolicy(unauthorizedLicenseContracts, this.roles);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void updateAuthorizationPolicy_newRoleThatBelongsToDifferentOrg_shouldThrowException() {
    OrgId differentOrgId = new OrgId(UUID.randomUUID().toString());
    Role unauthorizedRole =
        this.createApi().createCustomRole("unauthorized-role", differentOrgId, List.of());

    try {
      topLevelOu.updateAuthorizationPolicy(this.licenseContractIds, List.of(unauthorizedRole));

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void
      updateAuthenticationPolicy_withValidMfaAndRecoveryPolicy_shouldUpdateAuthenticationPolicy() {
    MfaPolicy newMfaPolicy = new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY);
    RecoveryPolicy newRecoveryPolicy = new RecoveryPolicy(MfaType.EMAIL);

    this.topLevelOu.updateAuthenticationPolicy(newMfaPolicy, newRecoveryPolicy);

    assert topLevelOu.authenticationPolicy().mfaPolicy().equals(newMfaPolicy)
        : "got: " + topLevelOu.authenticationPolicy().mfaPolicy() + " expected: " + newMfaPolicy;

    assert topLevelOu.authenticationPolicy().recoveryPolicy().equals(newRecoveryPolicy)
        : "got: "
            + topLevelOu.authenticationPolicy().recoveryPolicy()
            + " expected: "
            + newRecoveryPolicy;
  }

  @Test
  public void updateAuthenticationPolicy_withNullParameters_throwException() {
    for (int i = 0; i < 2; i++) {
      try {
        this.topLevelOu.updateAuthenticationPolicy(
            i == 0 ? null : new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY),
            i == 1 ? null : new RecoveryPolicy(MfaType.EMAIL));

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
      }
    }
  }

  @Test
  public void
      assignOu_OusInTheSameOrgAndInheritanceToggleActive_shouldAssignChildToParentAndSynchronizeParentAndChild() {
    Ou parent = this.topLevelOu.createOu("managers");
    Ou childOu = this.topLevelOu.createOu("developers");

    // update parent OU's policies to non-inherited so that we can verify that child OU's policies
    // are synchronized with parent's after assignment
    List<LicenseContractId> newLicenseContractId =
        this.createLicenseContractId(this.orgId, this.createLicenseIds());
    List<Role> newRoles = this.createDefaultRoles(this.createApi());

    parent.updateAuthorizationPolicy(newLicenseContractId, newRoles);
    parent.updateAuthenticationPolicy(
        new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY), new RecoveryPolicy(MfaType.EMAIL));

    parent.assignOu(childOu, true, true);

    AuthorizationPolicy parentAuthorizationPolicy = parent.authorizationPolicy();
    AuthenticationPolicy parentAuthenticationPolicy = parent.authenticationPolicy();

    AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();
    AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("developers") : "got: " + childOu.name() + " expected: developers";
    assert childOu.parent().equals(parent.id())
        : "got: " + childOu.parent() + " expected: " + parent.id();
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert childAuthorizationPolicy.isInherited() : "got: " + false + " expected: " + true;
    assert childAuthorizationPolicy
            .licenseContractIds()
            .equals(parentAuthorizationPolicy.licenseContractIds())
        : "got: "
            + childAuthorizationPolicy.licenseContractIds()
            + " expected: "
            + parentAuthorizationPolicy.licenseContractIds();
    assert childAuthorizationPolicy.roleIds().equals(parentAuthorizationPolicy.roleIds())
        : "got: "
            + childAuthorizationPolicy.roleIds()
            + " expected: "
            + parentAuthorizationPolicy.roleIds();
    assert childAuthenticationPolicy.isInherited() : "got: " + false + " expected: " + true;
    assert childAuthenticationPolicy.mfaPolicy().equals(parentAuthenticationPolicy.mfaPolicy())
        : "got: "
            + childAuthenticationPolicy.mfaPolicy()
            + " expected: "
            + parentAuthenticationPolicy.mfaPolicy();
    assert childAuthenticationPolicy
            .recoveryPolicy()
            .equals(parentAuthenticationPolicy.recoveryPolicy())
        : "got: "
            + childAuthenticationPolicy.recoveryPolicy()
            + " expected: "
            + parentAuthenticationPolicy.recoveryPolicy();
  }

  @Test
  public void
      assignOu_OusInTheSameOrgAndInheritanceToggleInactive_shouldAssignChildToParentButNotSynchronizeParentAndChild() {
    Ou parent = this.topLevelOu.createOu("managers");
    Ou childOu = this.topLevelOu.createOu("accounting");

    // update parent OU's policies to non-inherited so that we can verify that child OU's policies
    // are not synchronized with parent's after assignment
    List<LicenseContractId> newLicenseContractId =
        this.createLicenseContractId(this.orgId, this.createLicenseIds());
    List<Role> newRoles = this.createDefaultRoles(this.createApi());
    parent.updateAuthorizationPolicy(newLicenseContractId, newRoles);
    parent.updateAuthenticationPolicy(
        new MfaPolicy(MfaType.SMS, DEFAULT_MFA_EXPIRY),
        new RecoveryPolicy(MfaType.AUTHENTICATOR_APP));

    AuthorizationPolicy oldAuthorizationPolicy = childOu.authorizationPolicy();
    AuthenticationPolicy oldAuthenticationPolicy = childOu.authenticationPolicy();

    parent.assignOu(childOu, false, false);

    AuthorizationPolicy newAuthorizationPolicy = childOu.authorizationPolicy();
    AuthenticationPolicy newAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("accounting") : "got: " + childOu.name() + " expected: accounting";
    assert childOu.parent().equals(parent.id())
        : "got: " + childOu.parent() + " expected: " + parent.id();
    assert !childOu.isTopLevel() : "got: " + childOu.isTopLevel() + " expected: " + false;
    assert !newAuthorizationPolicy.isInherited() : "got: " + true + " expected: " + false;
    assert newAuthorizationPolicy
            .licenseContractIds()
            .equals(oldAuthorizationPolicy.licenseContractIds())
        : "got: "
            + newAuthorizationPolicy.licenseContractIds()
            + " expected: "
            + oldAuthorizationPolicy.licenseContractIds();
    assert newAuthorizationPolicy.roleIds().equals(oldAuthorizationPolicy.roleIds())
        : "got: "
            + newAuthorizationPolicy.roleIds()
            + " expected: "
            + oldAuthorizationPolicy.roleIds();
    assert !newAuthenticationPolicy.isInherited() : "got: " + true + " expected: " + false;
    assert newAuthenticationPolicy.mfaPolicy().equals(oldAuthenticationPolicy.mfaPolicy())
        : "got: "
            + newAuthenticationPolicy.mfaPolicy()
            + " expected: "
            + oldAuthenticationPolicy.mfaPolicy();
    assert newAuthenticationPolicy.recoveryPolicy().equals(oldAuthenticationPolicy.recoveryPolicy())
        : "got: "
            + newAuthenticationPolicy.recoveryPolicy()
            + " expected: "
            + oldAuthenticationPolicy.recoveryPolicy();
  }

  @Test
  public void assignOu_ifChildOuIsInADifferentOrg_throwException() {
    OrgId differentOrgId = new OrgId(UUID.randomUUID().toString());
    Ou topLevelOuInDifferentLevel =
        this.createTopLevelOu(
            differentOrgId,
            List.of(),
            List.of(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, new DateInput("2023-12-01T00:00:00Z")));

    Ou parent = topLevelOuInDifferentLevel.createOu("retail");
    Ou childOu = this.topLevelOu.createOu("service");

    try {

      parent.assignOu(childOu, true, true);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void assignOu_ifTopLevelOuIsAssignedToOu_throwException() {
    Ou topLevelOu2 =
        this.createTopLevelOu(
            this.orgId,
            List.of(),
            List.of(),
            new RecoveryPolicy(MfaType.AUTHENTICATOR_APP),
            new MfaPolicy(MfaType.EMAIL, new DateInput("2023-12-01T00:00:00Z")));

    try {
      this.topLevelOu.assignOu(topLevelOu2, true, true);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void assignOu_ifParentIsAssignedToChild_throwException() {
    Ou parentOu = this.topLevelOu.createOu("warehouse");
    Ou childOu = parentOu.createOu("auditors");

    try {
      childOu.assignOu(parentOu, true, true);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void assignOu_ifAssignSelf_throwException() {
    Ou ou = this.topLevelOu.createOu("warehouse");

    try {
      ou.assignOu(ou, true, true);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void assignOu_withNullParameter_throwException() {
    Ou childOu = this.topLevelOu.createOu("finance");

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) this.topLevelOu.assignOu(null, true, true);
        if (i == 1) this.topLevelOu.assignOu(childOu, null, true);
        if (i == 2) this.topLevelOu.assignOu(childOu, true, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
            : "Exception reason mismatch got: "
                + e.reason()
                + " expected: "
                + ExceptionReason.INVALID_OU_ARG;
      }
    }
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_ifChildIsAssignedToParent_synchronize() {
    Ou parentOu = this.topLevelOu.createOu("admin");
    // update parent OU's policies to non-inherited so that we can verify that child OU's policies
    // are
    // synchronized with parent's after synchronization
    List<LicenseContractId> newLicenseContractId =
        this.createLicenseContractId(this.orgId, this.createLicenseIds());
    List<Role> newRoles = this.createDefaultRoles(this.createApi());
    parentOu.updateAuthorizationPolicy(newLicenseContractId, newRoles);

    Ou childOu = parentOu.createOu("developers");

    // assign child ou to topLevel ou but does not synchronize child authorizationPolicy with
    // parent's
    this.topLevelOu.assignOu(childOu, false, false);

    childOu.synchronizeAuthorizationPolicyWith(this.topLevelOu);

    AuthorizationPolicy parentAuthorizationPolicy = this.topLevelOu.authorizationPolicy();

    AuthorizationPolicy childAuthorizationPolicy = childOu.authorizationPolicy();

    assert childOu.name().equals("developers") : "got: " + childOu.name() + " expected: developers";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "got: " + childOu.parent() + " expected: " + this.topLevelOu.id();
    assert !childOu.isTopLevel() : "got: " + childOu.isTopLevel() + " expected: " + false;
    assert childAuthorizationPolicy.isInherited() : "got: " + false + " expected: " + true;
    assert childAuthorizationPolicy
            .licenseContractIds()
            .equals(parentAuthorizationPolicy.licenseContractIds())
        : "got: "
            + childAuthorizationPolicy.licenseContractIds()
            + " expected: "
            + parentAuthorizationPolicy.licenseContractIds();
    assert childAuthorizationPolicy.roleIds().equals(parentAuthorizationPolicy.roleIds())
        : "got: "
            + childAuthorizationPolicy.roleIds()
            + " expected: "
            + parentAuthorizationPolicy.roleIds();
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_ifChildNotAssignedToParent_throwException() {
    Ou parentOu = this.topLevelOu.createOu("admin");
    Ou childOu = parentOu.createOu("developers");

    try {
      childOu.synchronizeAuthorizationPolicyWith(this.topLevelOu);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void synchronizeAuthorizationPolicyWith_withNullParameter_throwException() {
    try {
      this.topLevelOu.synchronizeAuthorizationPolicyWith(null);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "Exception reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void synchronizeAuthenticationPolicyWith_ifChildIsAssignedToParent_synchronize() {
    Ou parentOu = this.topLevelOu.createOu("admin");

    // update parent OU's policies to non-inherited so that we can verify that child OU's policies
    // are synchronized with parent's after synchronization
    parentOu.updateAuthenticationPolicy(
        new MfaPolicy(MfaType.AUTHENTICATOR_APP, DEFAULT_MFA_EXPIRY),
        new RecoveryPolicy(MfaType.EMAIL));

    Ou childOu = parentOu.createOu("developers");

    // assign child ou to parent ou but does not synchronize child authenticationPolicy with
    // parent's
    this.topLevelOu.assignOu(childOu, false, false);

    childOu.synchronizeAuthenticationPolicyWith(this.topLevelOu);

    AuthenticationPolicy parentAuthenticationPolicy = this.topLevelOu.authenticationPolicy();
    AuthenticationPolicy childAuthenticationPolicy = childOu.authenticationPolicy();

    assert childOu.name().equals("developers") : "got: " + childOu.name() + " expected: developers";
    assert childOu.parent().equals(this.topLevelOu.id())
        : "got: " + childOu.parent() + " expected: " + this.topLevelOu.id();
    assert !childOu.isTopLevel() : "Child OU should not be top-level";
    assert childAuthenticationPolicy.isInherited()
        : "got: "
            + false
            + " expected: "
            + true
            + " Child OU authentication policy should be inherited";
    assert childAuthenticationPolicy.mfaPolicy().equals(parentAuthenticationPolicy.mfaPolicy())
        : "got: "
            + childAuthenticationPolicy.mfaPolicy()
            + " expected: "
            + parentAuthenticationPolicy.mfaPolicy();
    assert childAuthenticationPolicy
            .recoveryPolicy()
            .equals(parentAuthenticationPolicy.recoveryPolicy())
        : "got: "
            + childAuthenticationPolicy.recoveryPolicy()
            + " expected: "
            + parentAuthenticationPolicy.recoveryPolicy();
  }

  @Test
  public void synchronizeAuthenticationPolicyWith_ifChildNotAssignedToParent_throwException() {
    Ou parentOu = this.topLevelOu.createOu("admin");
    Ou childOu = parentOu.createOu("developers");

    try {
      childOu.synchronizeAuthenticationPolicyWith(this.topLevelOu);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
    }
  }

  @Test
  public void synchronizeAuthenticationPolicyWith_withNullParameter_throwException() {
    try {
      this.topLevelOu.synchronizeAuthenticationPolicyWith(null);

      assert false : "Expected an exception";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_OU_ARG;
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

  private @NonNull List<Role> createDefaultRoles(Api api) {
    List<Role> roles = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      roles.add(api.createDefaultRole("role" + i, List.of()));
    }
    return roles;
  }

  private @NonNull Ou createTopLevelOu(
      OrgId orgId,
      List<LicenseContractId> licenseContractIds,
      List<RoleId> roleIds,
      RecoveryPolicy recoveryPolicy,
      MfaPolicy mfaPolicy) {
    OuId ouId = new OuId(UUID.randomUUID().toString());
    AuthorizationPolicy authorizationPolicy =
        new AuthorizationPolicy(licenseContractIds, roleIds, false);
    AuthenticationPolicy authenticationPolicy =
        new AuthenticationPolicy(recoveryPolicy, mfaPolicy, false);

    return new Ou(orgId, ouId, authorizationPolicy, authenticationPolicy);
  }
}
