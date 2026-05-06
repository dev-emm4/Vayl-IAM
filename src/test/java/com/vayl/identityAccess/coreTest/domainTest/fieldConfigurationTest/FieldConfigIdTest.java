package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import org.junit.jupiter.api.Test;

public class FieldConfigIdTest {
  @Test
  void constructor_withBlankFieldName_throwsException() {
    String invalidFieldName = "";

    try {
      new FieldConfigId(invalidFieldName);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ID);
    }
  }

  @Test
  void constructor_withNullFieldName_throwsInvalidValueException() {
    String nullFieldName = null;

    try {
      new FieldConfigId(nullFieldName);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ID);
    }
  }

  @Test
  void constructor_withValidFieldName_createsId() {
    String fieldName = "address";
    FieldConfigId fieldConfigId = new FieldConfigId(fieldName);

    assert true : "got: " + fieldConfigId.toString() + " expected: " + fieldName;
  }
}
