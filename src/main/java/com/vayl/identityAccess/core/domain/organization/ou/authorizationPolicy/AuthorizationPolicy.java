package com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy;

import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.role.RoleId;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;

public class AuthorizationPolicy {
  private List<LicenseContractId> assignedLicenseContracts = new ArrayList<>();
  private List<RoleId> assignedRoles = new ArrayList<>();
  private boolean isInherited;

  public AuthorizationPolicy(
      @NonNull List<LicenseContractId> assignLicenseContracts,
      @NonNull List<RoleId> assignRoles,
      boolean isInherited) {
    this.setAssignedLicenseContracts(assignLicenseContracts);
    this.setAssignedRoles(assignRoles);
    this.setIsInherited(isInherited);
  }

  private void setAssignedLicenseContracts(List<LicenseContractId> licenseContractIds) {
    this.assignedLicenseContracts = licenseContractIds;
  }

  private void setAssignedRoles(List<RoleId> roleIds) {
    this.assignedRoles = roleIds;
  }

  private void setIsInherited(boolean isInherited) {
    this.isInherited = isInherited;
  }

  public List<LicenseContractId> assignedLicenseContracts() {
    return this.assignedLicenseContracts;
  }

  public List<RoleId> assignedRoles() {
    return this.assignedRoles;
  }

  public boolean isInherited() {
    return this.isInherited;
  }

  public AuthorizationPolicy copyWith(
      @NonNull List<LicenseContractId> licenseContractIds,
      @NonNull List<RoleId> roleIds,
      boolean isInherited) {
    List<LicenseContractId> newLicenseContractIds =
        licenseContractIds.isEmpty() ? this.assignedLicenseContracts() : licenseContractIds;
    List<RoleId> newAssignedRoleIds = roleIds.isEmpty() ? this.assignedRoles() : roleIds;
    return new AuthorizationPolicy(newLicenseContractIds, newAssignedRoleIds, isInherited);
  }

  public boolean isLicenseContractsEquals(List<LicenseContractId> licenseContractIds) {
    return this.assignedLicenseContracts().equals(licenseContractIds);
  }

  public boolean isRoleIdsEquals(List<RoleId> roleIds) {
    return this.assignedRoles().equals(roleIds);
  }

  @Override
  public String toString() {
    return "AuthorizationPolicy{"
        + "assignedLicenseContracts="
        + this.assignedLicenseContracts
        + ", assignedRoles="
        + this.assignedRoles
        + ", isInherited="
        + this.isInherited
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AuthorizationPolicy that = (AuthorizationPolicy) o;

    if (this.isInherited != that.isInherited) return false;
    if (!this.assignedLicenseContracts.equals(that.assignedLicenseContracts)) return false;
    return this.assignedRoles.equals(that.assignedRoles);
  }

  @Override
  public int hashCode() {
    int result = this.assignedLicenseContracts.hashCode();
    result = 31 * result + this.assignedRoles.hashCode();
    result = 31 * result + (this.isInherited ? 1 : 0);
    return result;
  }
}
