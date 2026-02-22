package com.vayl.identityAccess.coreTest.domainTest.licenseTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LicenseIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    String invalidId = "invalid-id!";

    try {
      new LicenseId(invalidId);

      assert false
          : "Expected InvalidValueException was not thrown for invalid UUIDv4 id" + invalidId;
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_LICENSE_ID)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_LICENSE_ID;

      assert e.invalidValue().equals(invalidId)
          : "InvalidValueError invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidId;
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = UUID.randomUUID().toString();
    LicenseId licenseId = new LicenseId(validId);

    assert true
        : "LicenseId mismatch after creation got: "
            + licenseId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    java.lang.String validId = UUID.randomUUID().toString();
    LicenseId id1 = new LicenseId(validId);
    LicenseId id2 = new LicenseId(validId);
    assert id1.equals(id2) : "LicenseIds with same ids should be equal";
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    LicenseId id1 = new LicenseId(UUID.randomUUID().toString());
    LicenseId id2 = new LicenseId(UUID.randomUUID().toString());
    assert !id1.equals(id2) : "LicenseIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    LicenseId licenseId = new LicenseId(validId);

    assert licenseId.toString().equals(validId)
        : "LicenseId toString mismatch got: " + licenseId.toString() + " expected: " + validId;
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String validId = UUID.randomUUID().toString();
    LicenseId id1 = new LicenseId(validId);
    LicenseId id2 = new LicenseId(validId);

    assert id1.hashCode() == id2.hashCode()
        : "LicenseIds with same ids should have the same hash code";
  }
}
