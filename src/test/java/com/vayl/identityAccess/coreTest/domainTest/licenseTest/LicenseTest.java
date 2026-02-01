package com.vayl.identityAccess.coreTest.domainTest.licenseTest;

import com.vayl.identityAccess.core.domain.license.License;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseTest {

  @Test
  void constructor_withValidParameters_createSubscriptionCorrectly() {
    LicenseId licenseId = new LicenseId(UUID.randomUUID().toString());
    String licenseName = "Premium Plan";
    License license = new License(licenseId, licenseName);

    assert license.id().equals(licenseId)
        : "Subscription ID mismatch got "
            + license.id().toString()
            + " expected "
            + licenseId.toString();
    assert license.name().equals(licenseName)
        : "Subscription name mismatch got " + license.name() + " expected " + licenseName;
  }

}
