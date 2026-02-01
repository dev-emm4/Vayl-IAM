package com.vayl.identityAccess.coreTest.domainTest.licenseTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.license.LicenseId;
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
      assert e.event().equals(ExceptionEvent.SUBSCRIPTION_ID_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.SUBSCRIPTION_ID_CREATION;

      assert e.reason().equals(ExceptionReason.INVALID_ID)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_ID;

      assert e.level().equals(ExceptionLevel.ERROR)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.ERROR;

      assert e.invalidValue().equals(invalidId)
          : "InvalidValueError invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidId;
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    String validId = java.util.UUID.randomUUID().toString();
    LicenseId subscriptionId = new LicenseId(validId);

    assert true
        : "SubscriptionId mismatch after creation got: "
            + subscriptionId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String validId = java.util.UUID.randomUUID().toString();
    LicenseId id1 = new LicenseId(validId);
    LicenseId id2 = new LicenseId(validId);
    assert id1.equals(id2) : "SubscriptionIds with same ids should be equal";
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    LicenseId id1 = new LicenseId(java.util.UUID.randomUUID().toString());
    LicenseId id2 = new LicenseId(java.util.UUID.randomUUID().toString());
    assert !id1.equals(id2) : "SubscriptionIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String validId = java.util.UUID.randomUUID().toString();
    LicenseId subscriptionId = new LicenseId(validId);

    assert subscriptionId.toString().equals(validId)
        : "SubscriptionId toString mismatch got: "
            + subscriptionId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String validId = java.util.UUID.randomUUID().toString();
    LicenseId id1 = new LicenseId(validId);
    LicenseId id2 = new LicenseId(validId);

    assert id1.hashCode() == id2.hashCode()
        : "SubscriptionIds with same ids should have the same hash code";
  }
}
