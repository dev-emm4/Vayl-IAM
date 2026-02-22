package com.vayl.identityAccess.coreTest.domainTest.organizationTest.ouTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.organization.ou.OuId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class OuIdTest {
  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    String invalidId = "invalid-id!";

    try {
      new OuId(invalidId);

      assert false
          : "Expected InvalidValueException was not thrown for invalid UUIDv4 id" + invalidId;
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_OU_ID)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_OU_ID;
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
    OuId ouId = new OuId(validId);

    assert true : "OuId mismatch after creation got: " + ouId.toString() + " expected: " + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String validId = UUID.randomUUID().toString();
    OuId id1 = new OuId(validId);
    OuId id2 = new OuId(validId);

    assert id1.equals(id2) : "OuId equality check failed for same id: " + validId;
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    OuId id1 = new OuId(UUID.randomUUID().toString());
    OuId id2 = new OuId(UUID.randomUUID().toString());

    assert !id1.equals(id2)
        : "OuId equality check incorrectly returned true for different ids: "
            + id1.toString()
            + " and "
            + id2.toString();
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    OuId ouId = new OuId(validId);

    assert ouId.toString().equals(validId)
        : "OuId toString() mismatch got: " + ouId.toString() + " expected: " + validId;
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String validId = UUID.randomUUID().toString();
    OuId id1 = new OuId(validId);
    OuId id2 = new OuId(validId);

    assert id1.hashCode() == id2.hashCode()
        : "OuId hashCode mismatch for same id got: " + id1.hashCode() + " and " + id2.hashCode();
  }
}
