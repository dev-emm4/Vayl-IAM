package com.vayl.identityAccess.coreTest.domainTest.licenseTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.license.License;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseTest {

  @Test
  void constructor_withValidParameters_createLicenseCorrectly() {
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    String licenseName = "Premium Plan";
    License license = new License(licenseId, licenseName);

    assert license.id().equals(licenseId) : "got " + license.id() + " expected " + licenseId;
    assert license.name().equals(licenseName)
        : "got " + license.name() + " expected " + licenseName;
  }

  @Test
  void constructor_withBlankLicenseName_throwException() {
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());

    try {
      new License(licenseId, " ");

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_LICENSE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_LICENSE_ARG;
    }
  }

  @Test
  void constructor_withNullParameters_throwException() {
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    String licenseName = "Basic Plan";

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) new License(null, licenseName);
        if (i == 1) new License(licenseId, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_LICENSE_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_LICENSE_ARG;
      }
    }
  }

  @Test
  public void createLicenseContract_withValidArgs_createLicenseContractSuccessfully() {
    License license = this.createLicense();
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    int amountAllocated = 20;
    DateInput expireAt = new DateInput("2024-12-01T00:00:00Z");

    LicenseContract licenseContract =
        license.createLicenseContract(orgId, amountAllocated, expireAt);

    assert licenseContract.id().equals(new LicenseContractId(orgId, license.id()))
        : "LicenseContractId does not match";
    assert licenseContract.orgId().equals(orgId) : "OrgId does not match";
    assert licenseContract.amountAllocated() == amountAllocated : "AmountAllocated does not match";
    assert licenseContract.amountRemaining() == amountAllocated : "AmountRemaining does not match";
    assert licenseContract.expireAt().equals(expireAt) : "ExpireAt does not match";
  }

  @Test
  public void createLicenseContract_withNullArgs_throwException() {
    License license = this.createLicense();
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    int amountAllocated = 20;
    DateInput expireAt = new DateInput("2024-12-01T00:00:00Z");

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) license.createLicenseContract(null, amountAllocated, expireAt);
        if (i == 1) license.createLicenseContract(orgId, null, expireAt);
        if (i == 2) license.createLicenseContract(orgId, amountAllocated, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_LICENSE_CONTRACT_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_LICENSE_CONTRACT_ARG;
      }
    }
  }

  private License createLicense() {
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    String name = "Test License";
    return new License(licenseId, name);
  }
}
