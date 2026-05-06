package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.Schedule;
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

    Schedule enforcementDate =
        new Schedule(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> invalidFieldTypes =
        List.of(FieldType.STRING, FieldType.DATE, FieldType.ADDRESS, FieldType.USERNAME);

    for (FieldType fieldType : invalidFieldTypes) {
      try {
        new VerifiableFieldConfig(id, fieldType, true, enforcementDate);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_TYPE);
      }
    }
  }

  @Test
  void constructor_withNullParameter_throwException() {
    FieldConfigId id = new FieldConfigId("address");
    Schedule enforcementDate = new Schedule(Instant.now().toString());

    for (int i = 0; i < 4; i++) {
      try {
        if (i == 0) new VerifiableFieldConfig(null, FieldType.EMAIL, true, enforcementDate);
        if (i == 1) new VerifiableFieldConfig(id, null, true, enforcementDate);
        if (i == 2) new VerifiableFieldConfig(id, FieldType.EMAIL, null, enforcementDate);
        if (i == 3) new VerifiableFieldConfig(id, FieldType.EMAIL, true, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert List.of(
                ExceptionReason.INVALID_FIELD_CONFIG_ID,
                ExceptionReason.INVALID_FIELD_CONFIG_TYPE,
                ExceptionReason.INVALID_ENFORCEMENT_DATE,
                ExceptionReason.INVALID_VERIFICATION_REQUIREMENT)
            .contains(e.reason());
      }
    }
  }

  @Test
  void constructor_withValidFieldType_createsInstanceCorrectly() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule enforcementDate =
        new Schedule(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> validFieldTypes = List.of(FieldType.EMAIL, FieldType.PHONE, FieldType.PASSCODE);

    for (FieldType fieldType : validFieldTypes) {
      VerifiableFieldConfig fieldConfig =
          new VerifiableFieldConfig(id, fieldType, true, enforcementDate);

      assert fieldConfig.fieldName().equals(fieldName)
          : "got: " + fieldConfig.fieldName() + " expected: " + fieldName;
      assert fieldConfig.fieldType().equals(fieldType)
          : "got: " + fieldConfig.fieldType() + " expected: " + fieldType;
      assert fieldConfig.enforcementDate().equals(enforcementDate)
          : "got: " + fieldConfig.enforcementDate() + " expected: " + enforcementDate;
      assert fieldConfig.isVerifiable() : "got: false expected: true";
      assert fieldConfig.id().equals(id) : "got: " + fieldConfig.id() + " expected: " + id;
    }
  }

  @Test
  void modify_ifFieldNameIsPrimaryEmailAndEnforcementDateChanged_throwException() {
    String fieldName = "PRIMARY_EMAIL";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule initialEnforcementDate = new Schedule(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDate);

    Schedule newEnforcementDate =
        new Schedule(Instant.now().plusSeconds(86400).toString()); // add 1 day

    try {
      fieldConfig.modify(newEnforcementDate, false);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ENFORCEMENT_DATE);
    }
  }

  @Test
  void
      modify_ifFieldNameIsPrimaryEmailAndEnforcementDateUnchanged_updatesVerificationRequirementCorrectly() {
    String fieldName = "Primary_Email";
    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule initialEnforcementDate = new Schedule(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDate);

    // Modify with the same enforcement date but change verification requirement
    fieldConfig.modify(initialEnforcementDate, false);

    assert fieldConfig.enforcementDate().equals(initialEnforcementDate)
        : "got: " + fieldConfig.enforcementDate() + " expected: " + initialEnforcementDate;
    assert !fieldConfig.isVerifiable() : "got: " + fieldConfig.isVerifiable() + " expected: false";
  }

  @Test
  void modify_withValidParameters_updatesSuccessfully() {
    String fieldName = "Test Field";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule initialEnforcementDate = new Schedule(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.EMAIL, true, initialEnforcementDate);

    Schedule newEnforcementDateInput =
        new Schedule(Instant.now().plusSeconds(86400).toString()); // add 1 day
    fieldConfig.modify(newEnforcementDateInput, false);

    assert fieldConfig.enforcementDate().equals(newEnforcementDateInput)
        : " got: " + fieldConfig.enforcementDate() + "expected: " + newEnforcementDateInput;
    assert !fieldConfig.isVerifiable() : "got: " + fieldConfig.isVerifiable() + " expected: false";
  }

  @Test
  void modify_withNullParameters_throwException() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule initialEnforcementDate = new Schedule(Instant.now().toString());
    VerifiableFieldConfig fieldConfig =
        new VerifiableFieldConfig(id, FieldType.PHONE, true, initialEnforcementDate);

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) fieldConfig.modify(null, true);
        if (i == 1) fieldConfig.modify(initialEnforcementDate, null);

        assert false : "Exception Expected";
      } catch (InvalidValueException e) {
        assert List.of(
                ExceptionReason.INVALID_ENFORCEMENT_DATE,
                ExceptionReason.INVALID_VERIFICATION_REQUIREMENT)
            .contains(e.reason());
      }
    }
  }
}
