package com.vayl.identityAccess.coreTest.domainTest.licenseTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwException() {
    String invalidId = "invalid-id!";

    try {
      new LicenseId(invalidId);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_LICENSE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_LICENSE_ARG;
    }
  }

  @Test
  void constructor_withNullId_throwsInvalidValueException() {
    try {
      new LicenseId(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_LICENSE_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_LICENSE_ARG;
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    LicenseId licenseId = new LicenseId(validId);

    assert licenseId.id().equals(validId)
        : "got: " + licenseId.id() + " expected: " + validId;
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    LicenseId licenseId = new LicenseId(validId);

    assert licenseId.toString().equals(validId)
        : "got: " + licenseId + " expected: " + validId;
  }
}
