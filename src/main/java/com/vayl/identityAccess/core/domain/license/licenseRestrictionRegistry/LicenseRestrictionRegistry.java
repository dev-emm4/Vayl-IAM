package com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry;

import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseRestrictionRegistry {
  private List<LicenseId> assignedLicenses;
  private Map<LicenseRestrictable, LicenseRestriction> licenseRestrictionMap = new HashMap<>();

  public LicenseRestrictionRegistry() {}

  public LicenseRestrictionRegistry(List<LicenseRestriction> licenseRestrictions) {
    this.setLicenseRestriction(licenseRestrictions);
  }

  private void setLicenseRestriction(List<LicenseRestriction> licenseRestrictions) {
    for (LicenseRestriction licenseRestriction : licenseRestrictions) {
      this.licenseRestrictionMap.put(licenseRestriction.licenseRestrictable(), licenseRestriction);
    }
  }

  public void initializeAssignedLicense(List<LicenseId> assignedLicenses) {
    this.assignedLicenses = assignedLicenses;
  }

  public void updateLicenseRestriction(List<LicenseRestriction> updatedLicenseRestrictions) {
    if (updatedLicenseRestrictions.isEmpty()) {
      return;
    }

    for (LicenseRestriction licenseRestriction : updatedLicenseRestrictions) {
      if (licenseRestriction.blockingLicenses().isEmpty()) {
        this.licenseRestrictionMap.remove(licenseRestriction.licenseRestrictable());
      } else {
        this.licenseRestrictionMap.put(
            licenseRestriction.licenseRestrictable(), licenseRestriction);
      }
    }

    return;
  }

  public boolean canAccessLicenseRestrictable(LicenseRestrictable licenseRestrictable) {
    this.throwErrorIfNotInitialized();
    LicenseRestriction licenseRestriction = this.licenseRestrictionMap.get(licenseRestrictable);

    if (licenseRestriction == null) {
      return true;
    }

    return this.doesAssignedLicenseAllowAccessToRestrictable(licenseRestriction);
  }

  private void throwErrorIfNotInitialized() {
    if (this.assignedLicenses == null || this.assignedLicenses.isEmpty()) {
      throw new InvalidValueException(
          ExceptionEvent.CHECKING_ACCESS_TO_LICENSE_RESTRICTABLE,
          ExceptionReason.LICENSE_RESTRICTION_REGISTRY_NOT_INITIALIZED,
          null,
          ExceptionLevel.ERROR);
    }
  }

  private boolean doesAssignedLicenseAllowAccessToRestrictable(
      LicenseRestriction licenseRestriction) {
    for (LicenseId licenseId : this.assignedLicenses) {
      if (!licenseRestriction.isBlockingLicense(licenseId)) {
        return true;
      }
    }
    return false;
  }

  public LicenseRestriction getLicenseRestriction(LicenseRestrictable licenseRestrictable) {
    return this.licenseRestrictionMap.get(licenseRestrictable);
  }
}
