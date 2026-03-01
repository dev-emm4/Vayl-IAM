package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.fieldConfiguration.UnverifiableFieldConfig;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UnverifiableFieldConfigTest {
  @Test
  void constructor_withInvalidFieldType_throwException() {
    FieldConfigId id = new FieldConfigId("address");
    DateInput enforcementDateInput =
        new DateInput(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> invalidFieldTypes =
        List.of(FieldType.PASSCODE, FieldType.EMAIL, FieldType.PHONE);

    for (FieldType fieldType : invalidFieldTypes) {
      try {
        new UnverifiableFieldConfig(id, fieldType, enforcementDateInput);

        assert false : "Exception expected" + fieldType;
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_FIELD_CONFIG_ARG;
      }
    }
  }

  @Test
  void constructor_withNullParameter_throwException() {
    FieldConfigId id = new FieldConfigId("address");
    DateInput enforcementDateInput = new DateInput(Instant.now().toString());

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) new UnverifiableFieldConfig(null, FieldType.STRING, enforcementDateInput);
        if (i == 1) new UnverifiableFieldConfig(id, null, enforcementDateInput);
        if (i == 2) new UnverifiableFieldConfig(id, FieldType.STRING, null);

        assert false : "Expected InvalidValueException was not thrown for null parameters";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_FIELD_CONFIG_ARG;
      }
    }
  }

  @Test
  void constructor_withValidFieldType_createsInstanceCorrectly() {
    String fieldName = "address";
    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput enforcementDateInput =
        new DateInput(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> validFieldTypes =
        List.of(FieldType.STRING, FieldType.ADDRESS, FieldType.DATE, FieldType.USERNAME);

    for (FieldType fieldType : validFieldTypes) {
      UnverifiableFieldConfig fieldConfig =
          new UnverifiableFieldConfig(id, fieldType, enforcementDateInput);

      assert fieldConfig.fieldType().equals(fieldType)
          : "got: " + fieldConfig.fieldType() + " expected: " + fieldType;

      assert fieldConfig.fieldName().equals(fieldName)
          : "got: " + fieldConfig.fieldName() + " expected: " + fieldName;

      assert fieldConfig.enforcementDate().equals(enforcementDateInput)
          : "got: " + fieldConfig.enforcementDate() + " expected: " + enforcementDateInput;

      assert fieldConfig.id().equals(id) : "got: " + fieldConfig.id() + " expected: " + id;
    }
  }

  @Test
  void modify_withValidEnforcementDate_updatesEnforcementDate() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput initialEnforcementDateInput = new DateInput(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, initialEnforcementDateInput);

    DateInput newEnforcementDateInput =
        new DateInput(Instant.now().plusSeconds(86400).toString()); // add 1 day
    fieldConfig.modify(newEnforcementDateInput);

    assert fieldConfig.enforcementDate().equals(newEnforcementDateInput)
        : "got: " + fieldConfig.enforcementDate() + "expected: " + newEnforcementDateInput;
  }

  @Test
  void modify_withNullEnforcementDate_updatesEnforcementDate() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput initialEnforcementDateInput = new DateInput(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, initialEnforcementDateInput);

    try {
      fieldConfig.modify(null);

      assert false : "Expected InvalidValueException was not thrown for null parameter";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_FIELD_CONFIG_ARG;
    }
  }

  @Test
  void isVerifiable_alwaysReturnsFalse() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput enforcementDateInput = new DateInput(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, enforcementDateInput);

    assert !fieldConfig.isVerifiable() : "got: " + fieldConfig.isVerifiable() + " expected: false";
  }
}
