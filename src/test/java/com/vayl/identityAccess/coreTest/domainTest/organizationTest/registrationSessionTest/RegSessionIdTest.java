package com.vayl.identityAccess.coreTest.domainTest.organizationTest.registrationSessionTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.registrationSession.RegSessionId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class RegSessionIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    java.lang.String invalidId = "invalid-id!";

    try {
      new RegSessionId(invalidId);
      assert false
          : "Expected InvalidValueException was not thrown for invalid UUIDv4 id" + invalidId;
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_REG_SESSION_ID)
          : "InvalidValueException reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_REG_SESSION_ID;
      assert e.invalidValue().equals(invalidId)
          : "InvalidValueException invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidId;
    }
  }

  @Test
  void constructor_withUUIDv4Id_createsId() {
    java.lang.String validId = UUID.randomUUID().toString();
    RegSessionId regSessionId = new RegSessionId(validId);

    assert true
        : "regSessionId mismatch after creation got: "
            + regSessionId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    java.lang.String validId = UUID.randomUUID().toString();
    RegSessionId id1 = new RegSessionId(validId);
    RegSessionId id2 = new RegSessionId(validId);
    assert id1.equals(id2) : "regSessionIds with same ids should be equal";
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    RegSessionId id1 = new RegSessionId(java.util.UUID.randomUUID().toString());
    RegSessionId id2 = new RegSessionId(java.util.UUID.randomUUID().toString());
    assert !id1.equals(id2) : "regSessionIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    java.lang.String validId = UUID.randomUUID().toString();
    RegSessionId regSessionId = new RegSessionId(validId);

    assert regSessionId.toString().equals(validId)
        : "regSessionId toString mismatch got: " + regSessionId + " expected: " + validId;
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    java.lang.String validId = UUID.randomUUID().toString();
    RegSessionId id1 = new RegSessionId(validId);
    RegSessionId id2 = new RegSessionId(validId);

    assert id1.hashCode() == id2.hashCode()
        : "regSessionIds with same ids should have the same hash code";
  }
}
