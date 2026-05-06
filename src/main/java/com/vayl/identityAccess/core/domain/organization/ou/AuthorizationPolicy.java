package com.vayl.identityAccess.core.domain.organization.ou;

import com.vayl.identityAccess.core.domain.api.role.RoleId;
import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record AuthorizationPolicy(
    List<LicenseContractId> licenseContractIds, List<RoleId> roleIds, Boolean isInherited) {
  public AuthorizationPolicy(
      @NonNull List<LicenseContractId> licenseContractIds,
      @NonNull List<RoleId> roleIds,
      @NonNull Boolean isInherited) {
    AssertionConcern.isNotNull(licenseContractIds, ExceptionReason.INVALID_LICENSE_CONTRACT_ID);
    AssertionConcern.isNotNull(roleIds, ExceptionReason.INVALID_ROLE_ID);
    AssertionConcern.isNotNull(isInherited, ExceptionReason.INVALID_AUTHORIZATION_POLICY_INHERITANCE);

    this.licenseContractIds = licenseContractIds;
    this.roleIds = roleIds;
    this.isInherited = isInherited;
  }

  @Contract("_ -> new")
  public @NonNull AuthorizationPolicy copyWith(@NonNull Boolean isInherited) {
    AssertionConcern.isNotNull(isInherited, ExceptionReason.INVALID_AUTHORIZATION_POLICY_INHERITANCE);

    return new AuthorizationPolicy(this.licenseContractIds, this.roleIds, isInherited);
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
