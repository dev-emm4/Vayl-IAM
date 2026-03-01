package com.vayl.identityAccess.coreTest.domainTest.organizationTest.licenseContractTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContract;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseContractTest {
  @Test
  void constructor_withNullParameters_throwException() {
    LicenseContractId licenseContractId =
        new LicenseContractId(
            new OrgId(UUID.randomUUID().toString()), new LicenseId(UUID.randomUUID().toString()));
    int amountAllocated = 10;
    int amountRemaining = 10;
    DateInput expireAt = new DateInput("2023-12-01T00:00:00Z");

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) new LicenseContract(null, amountAllocated, amountRemaining, expireAt);
        if (i == 1) new LicenseContract(licenseContractId, amountAllocated, amountRemaining, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_LICENSE_CONTRACT_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_LICENSE_CONTRACT_ARG;
      }
    }
  }

  @Test
  void constructor_withValidParameter_createLicenseContract() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);
    int amountAllocated = 10;
    int amountRemaining = 10;
    DateInput expireAt = new DateInput("2023-12-01T00:00:00Z");
    LicenseContract licenseContract =
        new LicenseContract(licenseContractId, amountAllocated, amountRemaining, expireAt);

    assert licenseContract.orgId().equals(orgId)
        : "got: " + licenseContract.orgId() + " expected: " + orgId;
    assert licenseContract.licenseId().equals(licenseId)
        : "got: " + licenseContract.licenseId() + " expected: " + licenseId;
    assert licenseContract.id().equals(licenseContractId)
        : "got: " + licenseContract.id() + " expected: " + licenseContract;
    assert licenseContract.amountAllocated() == amountAllocated
        : "got: " + licenseContract.amountAllocated() + " expected: " + amountAllocated;
    assert licenseContract.amountRemaining() == amountRemaining
        : "got: " + licenseContract.amountRemaining() + " expected: " + amountRemaining;
    ;
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
    DateInput expireAt = new DateInput("2023-12-01T00:00:00Z");
    return new LicenseContract(licenseContractId, amountAllocated, amountRemaining, expireAt);
  }
}
