package com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry;

import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Set;

public class LicenseRestriction {
  private LicenseRestrictable licenseRestrictable;
  private Set<LicenseId> blockingLicense = new HashSet<LicenseId>();

  public LicenseRestriction(
      @NonNull LicenseRestrictable licenseRestrictable, @NonNull Set<LicenseId> licenseIds) {
    setLicenseRestrictable(licenseRestrictable);
    setBlockingLicense(licenseIds);
  }

  private void setLicenseRestrictable(LicenseRestrictable licenseRestrictable) {
    this.licenseRestrictable = licenseRestrictable;
  }

  private void setBlockingLicense(@NonNull Set<LicenseId> licenseIds) {
    this.blockingLicense = licenseIds;
  }

  public LicenseRestrictable licenseRestrictable() {
    return licenseRestrictable;
  }

  public Set<LicenseId> blockingLicenses() {
    return blockingLicense;
  }

  public boolean isBlockingLicense(LicenseId licenseId) {
    return this.blockingLicense.contains(licenseId);
  }

  @Override
  public String toString() {
    return "LicenseRestriction{"
        + "licenseRestrictable="
        + this.licenseRestrictable.toString()
        + ", blacklistedLicenses="
        + this.blockingLicense.toString()
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;

    LicenseRestriction that = (LicenseRestriction) o;

    if (!this.licenseRestrictable.equals(that.licenseRestrictable)) return false;
    return this.blockingLicense.equals(that.blockingLicense);
  }

  @Override
  public int hashCode() {
    int result = licenseRestrictable.hashCode();
    result = 31 * result + blockingLicense.hashCode();
    return result;
  }
}
