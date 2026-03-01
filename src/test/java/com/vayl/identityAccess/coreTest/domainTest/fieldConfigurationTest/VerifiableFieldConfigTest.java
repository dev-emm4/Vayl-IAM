package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.fieldConfiguration.VerifiableFieldConfig;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VerifiableFieldConfigTest {
  @Test
  void constructor_withInvalidFieldType_throwException() {
    String fieldName = "address";
    FieldConfigId id = new FieldConfigId(fieldName);

    DateInput enforcementDateInput =
        new DateInput(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> invalidFieldTypes =
        List.of(FieldType.STRING, FieldType.DATE, FieldType.ADDRESS, FieldType.USERNAME);

    for (FieldType fieldType : invalidFieldTypes) {
      try {
        new VerifiableFieldConfig(id, fieldType, true, enforcementDateInput);

        assert false : "Exception expected";
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
        if (i == 0) new VerifiableFieldConfig(null, FieldType.EMAIL, true, enforcementDateInput);
        if (i == 1) new VerifiableFieldConfig(id, null, true, enforcementDateInput);
        if (i == 2) new VerifiableFieldConfig(id, FieldType.EMAIL, true, null);

        assert false : "Exception expected";
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
    List<FieldType> validFieldTypes = List.of(FieldType.EMAIL, FieldType.PHONE, FieldType.PASSCODE);

    for (FieldType fieldType : validFieldTypes) {
      VerifiableFieldConfig fieldConfig =
          new VerifiableFieldConfig(id, fieldType, true, enforcementDateInput);

      assert fieldConfig.fieldName().equals(fieldName)
          : "got: " + fieldConfig.fieldName() + " expected: " + fieldName;
      assert fieldConfig.fieldType().equals(fieldType)
          : "got: " + fieldConfig.fieldType() + " expected: " + fieldType;
      assert fieldConfig.enforcementDate().equals(enforcementDateInput)
          : "got: " + fieldConfig.enforcementDate() + " expected: " + enforcementDateInput;
      assert fieldConfig.isVerifiable() : "got: false expected: true";
      assert fieldConfig.id().equals(id) : "got: " + fieldConfig.id() + " expected: " + id;
    }
  }

  @Test
  void modify_ifFieldNameIsPrimaryEmailAndEnforcementDateChanged_throwException() {
    String fieldName = "PRIMARY_EMAIL";

    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput initialEnforcementDateInput = new DateInput(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDateInput);

    DateInput newEnforcementDateInput =
        new DateInput(Instant.now().plusSeconds(86400).toString()); // add 1 day

    try {
      fieldConfig.modify(newEnforcementDateInput, false);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_FIELD_CONFIG_ARG;
    }
  }

  @Test
  void
      modify_ifFieldNameIsPrimaryEmailAndEnforcementDateUnchanged_updatesVerificationRequirementCorrectly() {
    String fieldName = "Primary_Email";
    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput initialEnforcementDateInput = new DateInput(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDateInput);

    // Modify with the same enforcement date but change verification requirement
    fieldConfig.modify(initialEnforcementDateInput, false);

    assert fieldConfig.enforcementDate().equals(initialEnforcementDateInput)
        : "got: " + fieldConfig.enforcementDate() + " expected: " + initialEnforcementDateInput;
    assert !fieldConfig.isVerifiable() : "got: " + fieldConfig.isVerifiable() + " expected: false";
  }

  @Test
  void modify_withValidParameters_updatesSuccessfully() {
    String fieldName = "Test Field";

    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput initialEnforcementDateInput = new DateInput(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDateInput);

    DateInput newEnforcementDateInput =
        new DateInput(Instant.now().plusSeconds(86400).toString()); // add 1 day
    fieldConfig.modify(newEnforcementDateInput, false);

    assert fieldConfig.enforcementDate().equals(newEnforcementDateInput)
        : " got: " + fieldConfig.enforcementDate() + "expected: " + newEnforcementDateInput;
    assert !fieldConfig.isVerifiable() : "got: " + fieldConfig.isVerifiable() + " expected: false";
  }

  @Test
  void modify_withNullEnforcementDate_throwException() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    DateInput initialEnforcementDateInput = new DateInput(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.PHONE, true, initialEnforcementDateInput);

    try {
      fieldConfig.modify(null, true);

      assert false : "ExceptionN Expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_FIELD_CONFIG_ARG;
    }
  }
}
