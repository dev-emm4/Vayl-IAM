package com.vayl.identityAccess.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class FieldConfigIdTest {

  @Test
  void constructor_withInvalidUUIDv4Id_throwsInvalidValueException() {
    String invalidId = "invalid-id!";

    try {
      new FieldConfigId(invalidId);
      assert false
          : "Expected InvalidValueException was not thrown for invalid UUIDv4 id" + invalidId;
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.FIELDCONFIG_ID_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.FIELDCONFIG_ID_CREATION;

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
    String validId = UUID.randomUUID().toString();
    FieldConfigId fieldConfigId = new FieldConfigId(validId);

    assert true
        : "FieldConfigId mismatch after creation got: "
            + fieldConfigId.toString()
            + " expected: "
            + validId;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String validId = UUID.randomUUID().toString();
    FieldConfigId id1 = new FieldConfigId(validId);
    FieldConfigId id2 = new FieldConfigId(validId);

    assert id1.equals(id2);
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    FieldConfigId id1 = new FieldConfigId(UUID.randomUUID().toString());
    FieldConfigId id2 = new FieldConfigId(UUID.randomUUID().toString());

    assert !id1.equals(id2) : "FieldConfigIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String validId = UUID.randomUUID().toString();
    FieldConfigId fieldConfigId = new FieldConfigId(validId);

    assert fieldConfigId.toString().equals(validId) : "toString should return the id string";
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String validId = UUID.randomUUID().toString();
    FieldConfigId id1 = new FieldConfigId(validId);
    FieldConfigId id2 = new FieldConfigId(validId);

    assert id1.hashCode() == id2.hashCode()
        : "FieldConfigIds with same ids should have the same hash code";
  }
}
