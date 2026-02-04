package com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy;

import com.vayl.identityAccess.core.domain.organization.ou.LicenseContract;
import com.vayl.identityAccess.core.domain.role.RoleId;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationPolicy {
  private List<LicenseContract> licenseContracts = new ArrayList<>();
  private List<RoleId> assignedRoles = new ArrayList<>();
  private boolean isInherited;

  public AuthorizationPolicy(
      List<LicenseContract> licenseContracts, List<RoleId> assignedRoles, boolean isInherited) {
    this.setLicenseContracts(licenseContracts);
    this.setAssignedRoles(assignedRoles);
    this.setIsInherited(isInherited);
  }

  private void setLicenseContracts(List<LicenseContract> licenseContracts) {
    this.licenseContracts = licenseContracts;
  }

  private void setAssignedRoles(List<RoleId> assignedRoles) {
    this.assignedRoles = assignedRoles;
  }

  private void setIsInherited(boolean isInherited) {
    this.isInherited = isInherited;
  }

  public List<LicenseContract> licenseContracts() {
    return this.licenseContracts;
  }

  public List<RoleId> assignedRoles() {
    return this.assignedRoles;
  }

  public boolean isInherited() {
    return this.isInherited;
  }

  public AuthorizationPolicy copyWith(boolean isInherited) {
    return new AuthorizationPolicy(this.licenseContracts(), this.assignedRoles(), isInherited);
  }

  @Override
  public String toString() {
    return "AuthorizationPolicy{"
        + "licenseContracts="
        + this.licenseContracts
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
    if (!this.licenseContracts.equals(that.licenseContracts)) return false;
    return this.assignedRoles.equals(that.assignedRoles);
  }

  @Override
  public int hashCode() {
    int result = this.licenseContracts.hashCode();
    result = 31 * result + this.assignedRoles.hashCode();
    result = 31 * result + (this.isInherited ? 1 : 0);
    return result;
  }
}
