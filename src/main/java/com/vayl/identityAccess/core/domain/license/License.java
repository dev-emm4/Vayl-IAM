package com.vayl.identityAccess.core.domain.license;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.Schedule;
import com.vayl.identityAccess.core.domain.common.validator.UuidValidator;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import org.jspecify.annotations.NonNull;

public class License {
  private LicenseId id;
  private String name;

  public License(@NonNull LicenseId id, @NonNull String name) {
    this.setId(id);
    this.setName(name);
  }

  private void setId(LicenseId id) {
    AssertionConcern.isNotNull(id, ExceptionReason.INVALID_LICENSE_ID);
    AssertionConcern.isValid(
        new UuidValidator(), id.toString(), ExceptionReason.INVALID_LICENSE_ID);
    this.id = id;
  }

  private void setName(String name) {
    AssertionConcern.isNotNull(name, ExceptionReason.INVALID_LICENSE_NAME);
    AssertionConcern.isNotBlank(name, ExceptionReason.INVALID_LICENSE_NAME);
    this.name = name;
  }

  public @NonNull LicenseId id() {
    return this.id;
  }

  public @NonNull String name() {
    return this.name;
  }

  public @NonNull LicenseContract createLicenseContract(
      @NonNull OrgId orgId, Integer amountAllocated, @NonNull Schedule expireAt) {
    LicenseContractId licenseContractId = new LicenseContractId(orgId, this.id);
    return new LicenseContract(licenseContractId, amountAllocated, amountAllocated, expireAt);
  }
}
