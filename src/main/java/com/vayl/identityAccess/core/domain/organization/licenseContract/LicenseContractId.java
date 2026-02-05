package com.vayl.identityAccess.core.domain.organization.licenseContract;

import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;

public class LicenseContractId {
  private final OrgId orgId;
  private final LicenseId licenseId;

  public LicenseContractId(OrgId orgId, LicenseId licenseId) {
    this.orgId = orgId;
    this.licenseId = licenseId;
  }

  public OrgId orgId() {
    return this.orgId;
  }

  public LicenseId licenseId() {
    return this.licenseId;
  }

  public LicenseId licenseContractId() {
    return this.licenseId;
  }

  @Override
  public String toString() {
    return "LicenseContractId{" + "orgId=" + this.orgId + ", licenseId=" + this.licenseId + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LicenseContractId that = (LicenseContractId) o;

    if (!this.orgId.equals(that.orgId)) return false;
    return this.licenseId.equals(that.licenseId);
  }

  @Override
  public int hashCode() {
    int result = this.orgId.hashCode();
    result = 31 * result + this.licenseId.hashCode();
    return result;
  }
}
