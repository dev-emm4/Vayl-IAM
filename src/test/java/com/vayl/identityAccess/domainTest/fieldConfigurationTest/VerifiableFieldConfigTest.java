package com.vayl.identityAccess.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.fieldConfiguration.VerifiableFieldConfig;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class VerifiableFieldConfigTest {
  @Test
  void constructor_withInvalidFieldType_throwsInvalidValueException() {
    FieldConfigId id = new FieldConfigId(UUID.randomUUID().toString());
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
    FieldConfigId id = new FieldConfigId(UUID.randomUUID().toString());
    Date enforcementDate =
        new Date(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> validFieldTypes =
        List.of(FieldType.PRIMARY_EMAIL, FieldType.EMAIL, FieldType.PHONE, FieldType.PASSCODE);

    for (FieldType fieldType : validFieldTypes) {
      VerifiableFieldConfig fieldConfig =
          new VerifiableFieldConfig(id, fieldType, true, enforcementDate);

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
  void modify_ifPrimaryEmailFieldTypeEnabledAndEnforcementDateChanged_throwInvalidValueException() {
    FieldConfigId id = new FieldConfigId(UUID.randomUUID().toString());
    Date initialEnforcementDate = new Date(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.PRIMARY_EMAIL, true, initialEnforcementDate);
    Date newEnforcementDate = new Date(Instant.now().plusSeconds(86400).toString()); // add 1 day

    try {
      fieldConfig.modify(newEnforcementDate, false);

      assert false
          : "VerifiableFieldConfig with fieldType = PRIMARY_EMAIL had enforcementDate modified.";
    } catch (InvalidValueException e) {
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
    FieldConfigId id = new FieldConfigId(UUID.randomUUID().toString());
    Date initialEnforcementDate = new Date(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.PRIMARY_EMAIL, true, initialEnforcementDate);

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
    FieldConfigId id = new FieldConfigId(UUID.randomUUID().toString());
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
