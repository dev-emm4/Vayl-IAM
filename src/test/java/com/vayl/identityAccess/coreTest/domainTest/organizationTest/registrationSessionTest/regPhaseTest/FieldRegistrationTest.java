package com.vayl.identityAccess.coreTest.domainTest.organizationTest.registrationSessionTest.regPhaseTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import com.vayl.identityAccess.core.domain.organization.registrationSession.regPhase.FieldRegistration;
import org.junit.jupiter.api.Test;

public class FieldRegistrationTest {
  @Test
  public void constructor_withEmptyFieldName_throwException() {
    try {
      new FieldRegistration("", null, FieldType.EMAIL, true, true, false);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_REG_SESSION_ARG)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_REG_SESSION_ARG;
    }
  }

  @Test
  public void constructor_withNullParameters_throwException() {
    for (int i = 0; i < 5; i++) {
      try {
        if (i == 0) new FieldRegistration(null, null, FieldType.EMAIL, true, true, false);
        if (i == 1) new FieldRegistration("PRIMARY_EMAIL", null, null, true, true, false);
        if (i == 2)
          new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, null, true, false);
        if (i == 3)
          new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, null, false);
        if (i == 4) new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, true, null);

        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason().equals(ExceptionReason.INVALID_REG_SESSION_ARG)
            : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_REG_SESSION_ARG;
      }
    }
  }

  @Test
  public void constructor_withValidParameters_createFieldRegistration() {
    FieldRegistration fieldRegistration =
        new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, true, false);

    assert fieldRegistration.fieldName().equals("PRIMARY_EMAIL")
        : "got: " + fieldRegistration.fieldName() + " expected: " + "PRIMARY_EMAIL";
    assert fieldRegistration.fieldValue() == null
        : "got: " + fieldRegistration.fieldValue() + " expected: " + null;
    assert fieldRegistration.type() == FieldType.EMAIL
        : "got: " + fieldRegistration.type() + " expected: " + FieldType.EMAIL;
    assert fieldRegistration.isValueRequired() : "got: " + false + " expected: " + true;
    assert fieldRegistration.isVerificationRequired() : "got: " + false + " expected: " + true;
    assert !fieldRegistration.isVerified() : "got: " + true + " expected: " + false;
  }

  @Test
  public void isVerified_ifVerificationIsNotRequired_returnTrue() {
    FieldRegistration fieldRegistration1 =
        new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, false, false);

    assert fieldRegistration1.isVerified() : "got: " + false + " expected: " + true;

    FieldRegistration fieldRegistration2 =
        new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, false, true);

    assert fieldRegistration2.isVerified() : "got: " + false + " expected: " + true;
  }

  @Test
  public void isVerified_ifVerificationIstRequired_returnIsVerified() {
    boolean isVerified = false;
    FieldRegistration fieldRegistration1 =
        new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, true, isVerified);

    assert !fieldRegistration1.isVerified() : "got: " + true + " expected: " + false;

    isVerified = true;
    FieldRegistration fieldRegistration2 =
        new FieldRegistration("PRIMARY_EMAIL", null, FieldType.EMAIL, true, true, isVerified);

    assert fieldRegistration2.isVerified() : "got: " + false + " expected: " + true;
  }
}
