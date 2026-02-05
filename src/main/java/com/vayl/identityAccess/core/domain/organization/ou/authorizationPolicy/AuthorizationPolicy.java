package com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy;

import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import com.vayl.identityAccess.core.domain.role.Role;
import com.vayl.identityAccess.core.domain.role.RoleId;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationPolicy {
  private List<LicenseContractId> assignedLicenseContracts = new ArrayList<>();
  private List<RoleId> assignedRoles = new ArrayList<>();
  private boolean isInherited;

  public AuthorizationPolicy(
      List<LicenseContractId> assignLicenseContracts,
      List<RoleId> assignRoles,
      boolean isInherited) {
    this.setAssignedLicenseContracts(assignLicenseContracts);
    this.setAssignedRoles(assignRoles);
    this.setIsInherited(isInherited);
  }

  private void setAssignedLicenseContracts(List<LicenseContractId> assignLicenseContracts) {
    this.assignedLicenseContracts = assignLicenseContracts;
  }

  private void setAssignedRoles(List<RoleId> assignRoles) {
    this.assignedRoles = assignRoles;
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
      List<LicenseContractId> assignLicenseContracts,
      List<RoleId> assignRoles,
      boolean isInherited) {
    List<LicenseContractId> newLicenseContracts =
        assignLicenseContracts == null ? this.assignedLicenseContracts() : assignLicenseContracts;
    List<RoleId> newAssignedRoles = assignRoles == null ? this.assignedRoles() : assignRoles;
    return new AuthorizationPolicy(newLicenseContracts, newAssignedRoles, isInherited);
  }

  public boolean isLicenseContractsDifferent(List<LicenseContractId> licenseContractIds) {
    return this.assignedLicenseContracts().equals(licenseContractIds);
  }
  
  public boolean isRolesDifferent(List<RoleId> roleIds){
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
