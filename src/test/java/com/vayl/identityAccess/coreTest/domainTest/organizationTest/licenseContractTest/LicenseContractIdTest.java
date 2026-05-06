package com.vayl.identityAccess.coreTest.domainTest.organizationTest.licenseContractTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import com.vayl.identityAccess.core.domain.organization.OrgId;
import com.vayl.identityAccess.core.domain.organization.licenseContract.LicenseContractId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseContractIdTest {
  @Test
  void constructor_withNullParameters_throwException() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) new LicenseContractId(null, licenseId);
        if (i == 1) new LicenseContractId(orgId, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert List.of(ExceptionReason.INVALID_ORG_ID, ExceptionReason.INVALID_LICENSE_ID).contains(e.reason());
      }
    }
  }

  @Test
  void constructor_withValidParameters_createLicenseContractId() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);

    assert licenseContractId.licenseId().equals(licenseId)
        : "got: " + licenseContractId.licenseId() + " expected: " + licenseId;
    assert licenseContractId.orgId().equals(orgId)
        : "got: " + licenseContractId.orgId() + " expected: " + orgId;
  }

  @Test
  public void toString_returnsCorrectFormat() {
    OrgId orgId = new OrgId(UUID.randomUUID().toString());
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    LicenseContractId licenseContractId = new LicenseContractId(orgId, licenseId);
    String expectedString = orgId + "-" + licenseId;

    assert licenseContractId.toString().equals(expectedString)
        : "LicenseContractId toString mismatch got: "
            + licenseContractId
            + " expected: "
            + expectedString;
  }
}
