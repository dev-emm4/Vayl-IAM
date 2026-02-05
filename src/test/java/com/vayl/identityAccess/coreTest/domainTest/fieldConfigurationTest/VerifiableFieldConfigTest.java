package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.fieldConfiguration.VerifiableFieldConfig;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VerifiableFieldConfigTest {
  @Test
  void constructor_withInvalidFieldType_throwsInvalidValueException() {
    String fieldName = "address";
    FieldConfigId id = new FieldConfigId(fieldName);

    Date enforcementDate =
        new Date(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> invalidFieldTypes =
        List.of(FieldType.STRING, FieldType.DATE, FieldType.ADDRESS, FieldType.USERNAME);

    for (FieldType fieldType : invalidFieldTypes) {
      try {
        VerifiableFieldConfig fieldConfig =
            new VerifiableFieldConfig(id, fieldType, true, enforcementDate);
        assert false
            : "Expected InvalidValueError was not thrown for invalid field type: " + fieldType;
      } catch (InvalidValueException e) {
        assert e.event().equals(ExceptionEvent.VERIFIABLE_FIELD_CONFIG_CREATION)
            : "InvalidValueError event mismatch got: "
                + e.event()
                + " expected: "
                + ExceptionEvent.UNVERIFIABLE_FIELD_CONFIG_CREATION;

        assert e.reason().equals(ExceptionReason.INVALID_FIELD_TYPE)
            : "InvalidValueError reason mismatch got: "
                + e.reason()
                + " expected: "
                + ExceptionReason.INVALID_FIELD_TYPE;

        assert e.level().equals(ExceptionLevel.INFO)
            : "InvalidValueError level mismatch got: "
                + e.level()
                + " expected: "
                + ExceptionLevel.INFO;

        assert e.invalidValue().equals(fieldType.toString())
            : "InvalidValueException invalidValue mismatch got: "
                + e.invalidValue()
                + " expected: "
                + fieldType.toString();
      }
    }
  }

  @Test
  void constructor_withValidFieldType_createsInstanceCorrectly() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Date enforcementDate =
        new Date(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> validFieldTypes = List.of(FieldType.EMAIL, FieldType.PHONE, FieldType.PASSCODE);

    for (FieldType fieldType : validFieldTypes) {
      VerifiableFieldConfig fieldConfig =
          new VerifiableFieldConfig(id, fieldType, true, enforcementDate);

      assert fieldConfig.fieldName().equals(fieldName)
          : "Field fieldName mismatch got: " + fieldConfig.fieldName() + " expected: " + fieldName;
      assert fieldConfig.fieldType().equals(fieldType)
          : "FieldType mismatch got: "
              + fieldConfig.fieldType().toString()
              + " expected: "
              + fieldType.toString();
      assert fieldConfig.enforcementDate().equals(enforcementDate)
          : "Enforcement date mismatch got: "
              + fieldConfig.enforcementDate().toString()
              + " expected: "
              + enforcementDate.toString();
      assert fieldConfig.isVerifiable()
          : "Verification requirement mismatch got: false expected: true";
      assert fieldConfig.id().equals(id)
          : "FieldConfigId mismatch got: "
              + fieldConfig.id().toString()
              + " expected: "
              + id.toString();
    }
  }

  @Test
  void modify_ifPrimaryEmailFieldConfigAndEnforcementDateChanged_throwInvalidValueException() {
    String fieldName = "PRIMARY_EMAIL";

    FieldConfigId id = new FieldConfigId(fieldName);
    Date initialEnforcementDate = new Date(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDate);

    Date newEnforcementDate = new Date(Instant.now().plusSeconds(86400).toString()); // add 1 day

    try {
      fieldConfig.modify(newEnforcementDate, false);

      assert false
          : "VerifiableFieldConfig with fieldName = PRIMARY_EMAIL had enforcementDate modified.";
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.VERIFIABLE_FIELD_CONFIG_MODIFICATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.VERIFIABLE_FIELD_CONFIG_MODIFICATION;

      assert e.reason().equals(ExceptionReason.PRIMARY_EMAIL_ENFORCEMENT_DATE_CANNOT_BE_CHANGED)
          : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_FIELD_TYPE;

      assert e.level().equals(ExceptionLevel.INFO)
          : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;

      assert e.invalidValue().equals(newEnforcementDate.toString())
          : "InvalidValueException invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + newEnforcementDate.toString();
    }
  }

  @Test
  void
      modify_ifPrimaryEmailFieldTypeEnabledAndEnforcementDateUnchanged_updatesVerificationRequirementCorrectly() {
    String fieldName = "Primary_Email";
    FieldConfigId id = new FieldConfigId(fieldName);
    Date initialEnforcementDate = new Date(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDate);

    // Modify with the same enforcement date but change verification requirement
    fieldConfig.modify(initialEnforcementDate, false);

    assert fieldConfig.enforcementDate().equals(initialEnforcementDate)
        : "Enforcement date should remain unchanged. expected: "
            + initialEnforcementDate.toString()
            + " got: "
            + fieldConfig.enforcementDate().toString();
    assert !fieldConfig.isVerifiable()
        : "Verification requirement was not updated correctly. expected: false got: true";
  }

  @Test
  void modify_withValidParameters_updatesSuccessfully() {
    String fieldName = "Test Field";

    FieldConfigId id = new FieldConfigId(fieldName);
    Date initialEnforcementDate = new Date(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDate);

    Date newEnforcementDate = new Date(Instant.now().plusSeconds(86400).toString()); // add 1 day
    fieldConfig.modify(newEnforcementDate, false);

    assert fieldConfig.enforcementDate().equals(newEnforcementDate)
        : "Enforcement date was not updated correctly. expected: "
            + newEnforcementDate.toString()
            + " got: "
            + fieldConfig.enforcementDate().toString();
    assert !fieldConfig.isVerifiable()
        : "Verification requirement was not updated correctly. expected: false got: true";
  }
}
