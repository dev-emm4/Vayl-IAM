package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest.authorizationPolicyTest;

import com.vayl.identityAccess.core.domain.organization.ou.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy.AuthorizationPolicy;
import com.vayl.identityAccess.core.domain.role.RoleId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class AuthorizationPolicyTest {
  @Test
  public void equals_sameAttributes_returnTrue() {
    List<LicenseContract> licenseContracts = List.of(new LicenseContract());
    List<RoleId> assignedRoles = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContracts, assignedRoles, false);
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContracts, assignedRoles, false);

    assert policy1.equals(policy2) : "Policies with the same attributes should be equal.";
  }

  @Test
  public void equals_differentAttributes_returnFalse() {
    List<LicenseContract> licenseContracts1 = List.of(new LicenseContract());
    List<RoleId> assignedRoles1 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContracts1, assignedRoles1, false);

    List<LicenseContract> licenseContracts2 = List.of(new LicenseContract());
    List<RoleId> assignedRoles2 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContracts2, assignedRoles2, true);

    assert !policy1.equals(policy2) : "Policies with different attributes should not be equal.";
  }

  @Test
  public void toString_validAttributes_returnCorrectString() {
    List<LicenseContract> licenseContracts = List.of(new LicenseContract());
    List<RoleId> assignedRoles = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy = new AuthorizationPolicy(licenseContracts, assignedRoles, false);

    String expectedString =
        "AuthorizationPolicy{"
            + "licenseContracts="
            + licenseContracts.toString()
            + ", assignedRoles="
            + assignedRoles.toString()
            + ", isInherited="
            + false
            + '}';

    assert policy.toString().equals(expectedString)
        : "toString should return the correct string representation.";
  }

  @Test
  public void hashCode_sameAttributes_returnSameHashCode() {
    List<LicenseContract> licenseContracts = List.of(new LicenseContract());
    List<RoleId> assignedRoles = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContracts, assignedRoles, false);
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContracts, assignedRoles, false);

    assert policy1.hashCode() == policy2.hashCode()
        : "Policies with the same attributes should have the same hash code.";
  }

  @Test
  public void hashCode_differentAttributes_returnDifferentHashCode() {
    List<LicenseContract> licenseContracts1 = List.of(new LicenseContract());
    List<RoleId> assignedRoles1 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy1 = new AuthorizationPolicy(licenseContracts1, assignedRoles1, false);

    List<LicenseContract> licenseContracts2 = List.of(new LicenseContract());
    List<RoleId> assignedRoles2 = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy policy2 = new AuthorizationPolicy(licenseContracts2, assignedRoles2, true);

    assert policy1.hashCode() != policy2.hashCode()
        : "Policies with different attributes should have different hash codes.";
  }

  @Test
  public void copyWith_changeIsInheritance_returnNewInstance() {
    List<LicenseContract> licenseContracts = List.of(new LicenseContract());
    List<RoleId> assignedRoles = List.of(new RoleId(UUID.randomUUID().toString()));
    AuthorizationPolicy originalPolicy =
        new AuthorizationPolicy(licenseContracts, assignedRoles, true);

    AuthorizationPolicy newPolicy = originalPolicy.copyWith(false);

    assert !newPolicy.isInherited() : "The new policy should have isInherited set to false.";
    assert newPolicy.licenseContracts().equals(originalPolicy.licenseContracts())
        : "License contracts should be the same.";
    assert newPolicy.assignedRoles().equals(originalPolicy.assignedRoles())
        : "Assigned roles should be the same.";
  }
}
