package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.Schedule;
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
    Schedule enforcementDate =
        new Schedule(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> invalidFieldTypes =
        List.of(FieldType.PASSCODE, FieldType.EMAIL, FieldType.PHONE);

    for (FieldType fieldType : invalidFieldTypes) {
      try {
        new UnverifiableFieldConfig(id, fieldType, enforcementDate);

        assert false : "Exception expected" + fieldType;
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_FIELD_CONFIG_TYPE);
      }
    }
  }

  @Test
  void constructor_withNullParameter_throwException() {
    FieldConfigId id = new FieldConfigId("address");
    Schedule enforcementDate = new Schedule(Instant.now().toString());

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) new UnverifiableFieldConfig(null, FieldType.STRING, enforcementDate);
        if (i == 1) new UnverifiableFieldConfig(id, null, enforcementDate);
        if (i == 2) new UnverifiableFieldConfig(id, FieldType.STRING, null);

        assert false : "Expected InvalidValueException was not thrown for null parameters";
      } catch (InvalidValueException e) {
        assert List.of(
                ExceptionReason.INVALID_FIELD_CONFIG_ID,
                ExceptionReason.INVALID_FIELD_CONFIG_TYPE,
                ExceptionReason.INVALID_ENFORCEMENT_DATE)
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
    List<FieldType> validFieldTypes =
        List.of(FieldType.STRING, FieldType.ADDRESS, FieldType.DATE, FieldType.USERNAME);

    for (FieldType fieldType : validFieldTypes) {
      UnverifiableFieldConfig fieldConfig =
          new UnverifiableFieldConfig(id, fieldType, enforcementDate);

      assert fieldConfig.fieldType().equals(fieldType)
          : "got: " + fieldConfig.fieldType() + " expected: " + fieldType;

      assert fieldConfig.fieldName().equals(fieldName)
          : "got: " + fieldConfig.fieldName() + " expected: " + fieldName;

      assert fieldConfig.enforcementDate().equals(enforcementDate)
          : "got: " + fieldConfig.enforcementDate() + " expected: " + enforcementDate;

      assert fieldConfig.id().equals(id) : "got: " + fieldConfig.id() + " expected: " + id;
    }
  }

  @Test
  void modify_withValidEnforcementDate_updatesEnforcementDate() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule initialEnforcementDate = new Schedule(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, initialEnforcementDate);

    Schedule newEnforcementDate =
        new Schedule(Instant.now().plusSeconds(86400).toString()); // add 1 day
    fieldConfig.modify(newEnforcementDate);

    assert fieldConfig.enforcementDate().equals(newEnforcementDate)
        : "got: " + fieldConfig.enforcementDate() + "expected: " + newEnforcementDate;
  }

  @Test
  void modify_withNullEnforcementDate_updatesEnforcementDate() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule initialEnforcementDate = new Schedule(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, initialEnforcementDate);

    try {
      fieldConfig.modify(null);

      assert false : "Expected InvalidValueException was not thrown for null parameter";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_ENFORCEMENT_DATE);
    }
  }

  @Test
  void isVerifiable_alwaysReturnsFalse() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Schedule enforcementDateInput = new Schedule(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, enforcementDateInput);

    assert !fieldConfig.isVerifiable() : "got: " + fieldConfig.isVerifiable() + " expected: false";
  }
}
