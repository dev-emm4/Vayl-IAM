package com.vayl.identityAccess.coreTest.domainTest.fieldConfigurationTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldConfigId;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.fieldConfiguration.UnverifiableFieldConfig;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UnverifiableFieldConfigTest {
  @Test
  void constructor_withInvalidFieldType_throwInvalidValueException() {
    FieldConfigId id = new FieldConfigId("address");
    Date enforcementDate =
        new Date(Instant.now().toString()); // generate a UTC ISO format date string
    List<FieldType> invalidFieldTypes =
        List.of(FieldType.PASSCODE, FieldType.EMAIL, FieldType.PHONE);

    for (FieldType fieldType : invalidFieldTypes) {
      try {
        UnverifiableFieldConfig fieldConfig =
            new UnverifiableFieldConfig(id, fieldType, enforcementDate);

        assert false
            : "Expected InvalidValueException was not thrown for invalid field type: " + fieldType;
      } catch (InvalidValueException e) {
        assert e.event().equals(ExceptionEvent.UNVERIFIABLE_FIELD_CONFIG_CREATION)
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
    List<FieldType> validFieldTypes =
        List.of(FieldType.STRING, FieldType.ADDRESS, FieldType.DATE, FieldType.USERNAME);

    for (FieldType fieldType : validFieldTypes) {
      UnverifiableFieldConfig fieldConfig =
          new UnverifiableFieldConfig(id, fieldType, enforcementDate);

      assert fieldConfig.fieldType().equals(fieldType)
          : "FieldType mismatch got: "
              + fieldConfig.fieldType().toString()
              + " expected: "
              + fieldType.toString();

      assert fieldConfig.fieldName().equals(fieldName)
          : "Name mismatch got: " + fieldConfig.fieldName() + " expected: " + fieldName;

      assert fieldConfig.enforcementDate().equals(enforcementDate)
          : "Enforcement date mismatch got: "
              + fieldConfig.enforcementDate().toString()
              + " expected: "
              + enforcementDate.toString();

      assert fieldConfig.id().equals(id)
          : "ID mismatch got: " + fieldConfig.id().toString() + " expected: " + id.toString();
    }
  }

  @Test
  void modify_updatesEnforcementDateSuccessfully() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Date initialEnforcementDate = new Date(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, initialEnforcementDate);

    Date newEnforcementDate = new Date(Instant.now().plusSeconds(86400).toString()); // add 1 day
    fieldConfig.modify(newEnforcementDate);

    assert fieldConfig.enforcementDate().equals(newEnforcementDate)
        : "Enforcement date was not updated correctly. expected: "
            + newEnforcementDate.toString()
            + " got: "
            + fieldConfig.enforcementDate().toString();
  }

  @Test
  void isVerifiable_alwaysReturnsFalse() {
    String fieldName = "address";

    FieldConfigId id = new FieldConfigId(fieldName);
    Date enforcementDate = new Date(Instant.now().toString());
    UnverifiableFieldConfig fieldConfig =
        new UnverifiableFieldConfig(id, FieldType.STRING, enforcementDate);

    assert !fieldConfig.isVerifiable()
        : "isVerifiable should return false for UnVerifiableFieldConfig.";
  }
}
