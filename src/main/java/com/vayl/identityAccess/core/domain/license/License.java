package com.vayl.identityAccess.core.domain.license;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;

public class License {
  private LicenseId id;
  private String name;

  public License(LicenseId id, String name) {
    this.setId(id);
    this.setName(name);
  }

  private void setId(LicenseId id) {
    this.id = id;
  }

  private void setName(String name) {
    this.name = name;
  }

  public LicenseId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public LicenseContract createLicenseContract(OrgId orgId, int amountAllocated, Date expireAt) {
    LicenseContractId licenseContractId = new LicenseContractId(orgId, this.id);
    return new LicenseContract(licenseContractId, amountAllocated, amountAllocated, expireAt);
  }
}
