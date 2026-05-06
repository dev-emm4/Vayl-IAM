package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.Schedule;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.AuthorizationPolicy;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class AuthorizationPolicyTest {
  @Test
  public void toString_validAttributes_returnCorrectString() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContractIds, roleIds, false);

    String expectedString =
        "AuthorizationPolicy{"
            + "licenseContractIds="
            + licenseContractIds
            + ", roleIds="
            + roleIds
            + ", isInherited="
            + false
            + '}';

    assert policy.toString().equals(expectedString)
        : "got: " + policy + " expected: " + expectedString;
  }

  @Test
  void constructor_withNullParameters_throwException() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) new AuthorizationPolicy(null, roleIds, true);
        if (i == 1) new AuthorizationPolicy(licenseContractIds, null, true);
        if (i == 2) new AuthorizationPolicy(licenseContractIds, roleIds, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert List.of(ExceptionReason.INVALID_LICENSE_CONTRACT_ID, ExceptionReason.INVALID_ROLE_ID, ExceptionReason.INVALID_AUTHORIZATION_POLICY_INHERITANCE).contains(e.reason());
      }
    }
  }

  @Test
  void constructor_withValidParameter_createAuthorizationPolicy() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    boolean isInherited = false;
    AuthorizationPolicy authorizationPolicy =
        new AuthorizationPolicy(licenseContractIds, roleIds, isInherited);

    assert authorizationPolicy.licenseContractIds().equals(licenseContractIds)
        : "got: " + authorizationPolicy.licenseContractIds() + " expected: " + licenseContractIds;
    assert authorizationPolicy.roleIds().equals(roleIds)
        : "got: " + authorizationPolicy.roleIds() + " expected: " + roleIds;
    assert authorizationPolicy.isInherited() == isInherited
        : "got: " + true + " expected: " + isInherited;
  }

  @Test
  public void copyWith_withValidParameter_createCopy() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy originalPolicy = new AuthorizationPolicy(licenseContractIds, roleIds, true);

    AuthorizationPolicy newPolicy = originalPolicy.copyWith(false);

    assert !(newPolicy.isInherited()) : "got: " + true + " expected: " + false;
    assert newPolicy.licenseContractIds().equals(licenseContractIds)
        : "got: " + newPolicy.licenseContractIds() + " expected: " + licenseContractIds;
    assert newPolicy.roleIds().equals(roleIds)
        : "got: " + newPolicy.roleIds() + " expected: " + roleIds;
  }

  @Test
  void copyWith_withNullParameter_throwException() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy authorizationPolicy =
        new AuthorizationPolicy(licenseContractIds, roleIds, true);

    try {
      authorizationPolicy.copyWith(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_AUTHORIZATION_POLICY_INHERITANCE
          : "got: " + e.reason() + "expected: " + ExceptionReason.INVALID_AUTHORIZATION_POLICY_INHERITANCE;
    }
  }

  private LicenseContract createLicenseContract() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);
    int amountAllocated = 10;
    int amountRemaining = 10;
    Schedule expireAt = new Schedule("2023-12-01T00:00:00Z");
    return new LicenseContract(licenseContractId, amountAllocated, amountRemaining, expireAt);
  }
}
