package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authorizationPolicyTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import com.vayl.identityAccess.core.domain.role.RoleId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class AuthorizationPolicyTest {
  @Test
  public void equals_sameAttributes_returnTrue() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContractIds, roleIds, false);
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContractIds, roleIds, false);

    assert policy1.equals(policy2) : "Policies with the same attributes should be equal.";
  }

  @Test
  public void equals_differentAttributes_returnFalse() {
    List<LicenseContractId> licenseContractIds1 = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds1 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContractIds1, roleIds1, false);

    List<LicenseContractId> licenseContractIds2 = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContractIds2, roleIds, true);

    assert !policy1.equals(policy2) : "Policies with different attributes should not be equal.";
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContractIds, roleIds, false);

    String expectedString =
        "AuthorizationPolicy{"
            + "assignedLicenseContracts="
            + licenseContractIds.toString()
            + ", assignedRoles="
            + roleIds.toString()
            + ", isInherited="
            + false
            + '}';

    assert policy.toString().equals(expectedString)
        : "toString should return the correct string representation.";
  }

  @Test
  public void hashCode_sameAttributes_returnSameHashCode() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContractIds, roleIds, false);
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContractIds, roleIds, false);

    assert policy1.hashCode() == policy2.hashCode()
        : "Policies with the same attributes should have the same hash code.";
  }

  @Test
  public void hashCode_differentAttributes_returnDifferentHashCode() {
    List<LicenseContractId> licenseContractIds1 = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContractIds1, roleIds, false);

    List<LicenseContractId> licenseContractIds2 = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds2 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContractIds2, roleIds2, true);

    assert policy1.hashCode() != policy2.hashCode()
        : "Policies with different attributes should have different hash codes.";
  }

  @Test
  public void copyWith_passingEmptyList_doesNotChangeField() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy originalPolicy = new AuthorizationPolicy(licenseContractIds, roleIds, true);

    AuthorizationPolicy newPolicy = originalPolicy.copyWith(List.of(), List.of(), false);

    assert !(newPolicy.isInherited()) : "The new policy should have isInherited set to false.";
    assert newPolicy.assignedLicenseContracts().equals(originalPolicy.assignedLicenseContracts())
        : "License contracts should be the same.";
    assert newPolicy.assignedRoles().equals(originalPolicy.assignedRoles())
        : "Assigned roles should be the same.";
  }

  @Test
  public void copyWith_passingNewValues_changesFields() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy originalPolicy = new AuthorizationPolicy(licenseContractIds, roleIds, true);

    List<LicenseContractId> newLicenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> newRoleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy newPolicy =
        originalPolicy.copyWith(newLicenseContractIds, newRoleIds, false);

    assert !(newPolicy.isInherited()) : "The new policy should have isInherited set to false.";
    assert newPolicy.assignedLicenseContracts().equals(newLicenseContractIds)
        : "License contracts should be updated.";
    assert newPolicy.assignedRoles().equals(newRoleIds) : "Assigned roles should be updated.";
  }

  @Test
  public void isLicenseContractIdsEquals_sameLicenseContracts_returnTrue() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContractIds, roleIds, false);

    assert policy.isLicenseContractsEquals(licenseContractIds)
        : "isLicenseContractsEquals should return true for the same license contracts.";
  }

  @Test
  public void isLicenseContractIdsEquals_differentLicenseContracts_returnFalse() {
    List<LicenseContractId> licenseContractIds1 = List.of(this.createLicenseContract().id());
    List<LicenseContractId> licenseContractIds2 = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContractIds1, roleIds, false);
  }

  @Test
  public void isRoleIdsEquals_sameRoleIds_returnTrue() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContractIds, roleIds, false);

    assert policy.isRoleIdsEquals(roleIds) : "isRolesEquals should return true for the same roles.";
  }

  @Test
  public void isRoleIdsEquals_differentRoleIds_returnFalse() {
    List<LicenseContractId> licenseContractIds = List.of(this.createLicenseContract().id());
    List<RoleId> roleIds1 = List.of(new RoleId(UUID.randomUUID().toString()));
    List<RoleId> roleIds2 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContractIds, roleIds1, false);

    assert !policy.isRoleIdsEquals(roleIds2)
        : "isRolesEquals should return false for different roles.";
  }

  private LicenseContract createLicenseContract() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);
    int amountAllocated = 10;
    int amountRemaining = 10;
    Date expireAt = new Date("2023-12-01T00:00:00Z");
    return new LicenseContract(licenseContractId, amountAllocated, amountRemaining, expireAt);
  }
}
