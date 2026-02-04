package com.vayl.identityAccess.coreTest.domainTest.licenseTest.licenseRestrictionRegistryTest;

import com.vayl.identityAccess.core.domain.api.ApiId;
import com.vayl.identityAccess.core.domain.api.LicenseRestrictable;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.license.licenseRestrictionRegistry.LicenseRestriction;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseRestrictionTest {

  @Test
  public void isBlockingLicense_withBlockingLicenses_returnsTrue() {
    LicenseRestriction licenseRestriction = createLicenseRestriction();
    LicenseId blockingLicenseId = licenseRestriction.blockingLicenses().iterator().next();

    assert licenseRestriction.isBlockingLicense(blockingLicenseId)
        : "Expected isBlockingLicense to return true for blocking license id "
            + blockingLicenseId.toString();
  }

  @Test
  public void isBlockingLicense_withNonBlockingLicenses_returnsFalse() {
    LicenseRestriction licenseRestriction = createLicenseRestriction();
    LicenseId nonBlockingLicenseId = new LicenseId(UUID.randomUUID().toString());

    assert !licenseRestriction.isBlockingLicense(nonBlockingLicenseId)
        : "Expected isBlockingLicense to return false for non-blocking license id "
            + nonBlockingLicenseId.toString();
  }

  @Test
  public void equals_withSameProperties_returnsTrue() {
    LicenseRestriction licenseRestriction1 = createLicenseRestriction();
    LicenseRestriction licenseRestriction2 =
        new LicenseRestriction(
            licenseRestriction1.licenseRestrictable(), licenseRestriction1.blockingLicenses());

    assert licenseRestriction1.equals(licenseRestriction2)
        : "Expected equals to return true for LicenseRestrictions with same properties";
  }

  @Test
  public void equals_withDifferentProperties_returnsFalse() {
    LicenseRestriction licenseRestriction1 = createLicenseRestriction();
    LicenseRestriction licenseRestriction2 = createLicenseRestriction();

    assert !licenseRestriction1.equals(licenseRestriction2)
        : "Expected equals to return false for LicenseRestrictions with different properties";
  }

  @Test
  public void hashCode_withSameProperties_returnsSameHashCode() {
    LicenseRestriction licenseRestriction1 = createLicenseRestriction();
    LicenseRestriction licenseRestriction2 =
        new LicenseRestriction(
            licenseRestriction1.licenseRestrictable(), licenseRestriction1.blockingLicenses());

    assert licenseRestriction1.hashCode() == licenseRestriction2.hashCode()
        : "Expected hashCode to return same value for LicenseRestrictions with same properties";
  }

  @Test
  public void hashCode_withDifferentProperties_returnsDifferentHashCode() {
    LicenseRestriction licenseRestriction1 = createLicenseRestriction();
    LicenseRestriction licenseRestriction2 = createLicenseRestriction();

    assert licenseRestriction1.hashCode() != licenseRestriction2.hashCode()
        : "Expected hashCode to return different values for LicenseRestrictions with different properties";
  }

  @Test
  public void toString_returnsCorrectFormat() {
    LicenseRestriction licenseRestriction = createLicenseRestriction();
    String expectedString =
        "LicenseRestriction{"
            + "licenseRestrictable="
            + licenseRestriction.licenseRestrictable().toString()
            + ", blacklistedLicenses="
            + licenseRestriction.blockingLicenses().toString()
            + '}';

    assert licenseRestriction.toString().equals(expectedString)
        : "toString output mismatch got: "
            + licenseRestriction.toString()
            + " expected: "
            + expectedString;
  }

  public LicenseRestriction createLicenseRestriction() {
    Set<LicenseId> blackListedLicense =
        new HashSet<LicenseId>(
            Set.of(
                new LicenseId(UUID.randomUUID().toString()),
                new LicenseId(UUID.randomUUID().toString())));
    LicenseRestrictable licenseRestrictable = new ApiId("example.com");

    return new LicenseRestriction(licenseRestrictable, blackListedLicense);
  }
}
