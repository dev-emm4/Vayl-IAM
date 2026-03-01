package com.vayl.identityAccess.core.domain.organization.licenseContract;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import org.jspecify.annotations.NonNull;

public record LicenseContractId(OrgId orgId, LicenseId licenseId) {
  public LicenseContractId(@NonNull OrgId orgId, @NonNull LicenseId licenseId) {
    AssertionConcern.isNotNull(orgId, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
    AssertionConcern.isNotNull(licenseId, ExceptionReason.INVALID_LICENSE_CONTRACT_ARG);
    this.orgId = orgId;
    this.licenseId = licenseId;
  }

  @Override
  public @NonNull String toString() {
    return this.orgId + "-" + this.licenseId;
  }
}
