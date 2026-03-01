package com.vayl.identityAccess.core.domain.organization.ou.authorizationPolicy;

import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.List;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record AuthorizationPolicy(
    List<LicenseContractId> licenseContractIds, List<RoleId> roleIds, boolean isInherited) {
  public AuthorizationPolicy(
      @NonNull List<LicenseContractId> licenseContractIds,
      @NonNull List<RoleId> roleIds,
      boolean isInherited) {
    AssertionConcern.isNotNull(licenseContractIds, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotNull(roleIds, ExceptionReason.INVALID_OU_ARG);

    this.licenseContractIds = licenseContractIds;
    this.roleIds = roleIds;
    this.isInherited = isInherited;
  }

  @Contract("_, _, _ -> new")
  public @NonNull AuthorizationPolicy copyWith(
      @NonNull List<LicenseContractId> licenseContractIds,
      @NonNull List<RoleId> roleIds,
      boolean isInherited) {
    AssertionConcern.isNotNull(licenseContractIds, ExceptionReason.INVALID_OU_ARG);
    AssertionConcern.isNotNull(roleIds, ExceptionReason.INVALID_OU_ARG);

    List<LicenseContractId> newLicenseContractIds =
        licenseContractIds.isEmpty() ? this.licenseContractIds() : licenseContractIds;
    List<RoleId> newAssignedRoleIds = roleIds.isEmpty() ? this.roleIds() : roleIds;
    return new AuthorizationPolicy(newLicenseContractIds, newAssignedRoleIds, isInherited);
  }

  @Override
  public @NonNull String toString() {
    return "AuthorizationPolicy{"
        + "licenseContractIds="
        + this.licenseContractIds
        + ", roleIds="
        + this.roleIds
        + ", isInherited="
        + this.isInherited
        + '}';
  }
}
