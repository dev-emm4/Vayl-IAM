package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValue;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import org.junit.jupiter.api.Test;

public class FieldConfigIdTest {
  @Test
  void constructor_withInvalidFieldName_throwsInvalidValueException() {
    String invalidFieldName = " ";

    try {
      new FieldConfigId(invalidFieldName);

      assert false
          : "Expected InvalidValueException was not thrown for blank string" + invalidFieldName;
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ID)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_FIELD_CONFIG_ID;

      assert e.invalidValue().equals(invalidFieldName)
          : "InvalidValueError invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidFieldName;
    }
  }

  @Test
  void constructor_withValidFieldName_createsId() {
    java.lang.String fieldName = "address";
    FieldConfigId fieldConfigId = new FieldConfigId(fieldName);

    assert true
        : "FieldConfigId mismatch after creation got: "
            + fieldConfigId.toString()
            + " expected: "
            + fieldName;
  }

  @Test
  void equals_withSameId_returnsTrue() {
    String fieldName = "address";
    FieldConfigId id1 = new FieldConfigId(fieldName);
    FieldConfigId id2 = new FieldConfigId(fieldName);

    assert id1.equals(id2);
  }

  @Test
  void equals_withDifferentId_returnsFalse() {
    FieldConfigId id1 = new FieldConfigId("address");
    FieldConfigId id2 = new FieldConfigId("phoneNumber");

    assert !id1.equals(id2) : "FieldConfigIds with different ids should not be equal";
  }

  @Test
  void toString_returnsIdString() {
    String fieldName = "address";
    FieldConfigId fieldConfigId = new FieldConfigId(fieldName);

    assert fieldConfigId.toString().equals(fieldName) : "toString should return the id string";
  }

  @Test
  void hashCode_withSameId_returnsSameHashCode() {
    String fieldName = "address";
    FieldConfigId id1 = new FieldConfigId(fieldName);
    FieldConfigId id2 = new FieldConfigId(fieldName);

    assert id1.hashCode() == id2.hashCode()
        : "FieldConfigIds with same ids should have the same hash code";
  }
}
