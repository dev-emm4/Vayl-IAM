package com.vayl.identityAccess.coreTest.domainTest.organizationTest.licenseContractTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.license.License;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseContractTest {
  @Test
  public void createLicenseContract_createLicenseContractSuccessfully() {
    License license = this.createLicense();
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    int amountAllocated = 20;
    Date expireAt = new Date("2024-12-01T00:00:00Z");

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
  public void increaseAmountRemaining_increaseAmountRemainingSuccessfully() {
    LicenseContract licenseContract = this.createLicenseContract();
    int expectedAmountRemaining = licenseContract.amountRemaining() + 1;

    licenseContract.increaseAmountRemaining();

    assert licenseContract.amountRemaining() == expectedAmountRemaining
        : "AmountRemaining was not increased correctly";
  }

  @Test
  public void increaseAllocatedAmount_increaseAllocatedAmountSuccessfully() {
    LicenseContract licenseContract = this.createLicenseContract();
    int increaseBy = 5;
    int expectedAmountAllocated = licenseContract.amountAllocated() + increaseBy;
    int expectedAmountRemaining = licenseContract.amountRemaining() + increaseBy;

    licenseContract.increaseAllocatedAmount(increaseBy);

    assert licenseContract.amountAllocated() == expectedAmountAllocated
        : "AmountAllocated was not increased correctly";
    assert licenseContract.amountRemaining() == expectedAmountRemaining
        : "AmountRemaining was not increased correctly";
  }

  private LicenseContract createLicenseContract() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);
    int amountAllocated = 10;
    int amountRemaining = 10;
    Date expireAt = new Date("2023-12-01T00:00:00Z");
    return new LicenseContract(licenseContractId, amountAllocated, amountRemaining, expireAt);
  }

  private License createLicense() {
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    String name = "Test License";
    return new License(licenseId, name);
  }
}
