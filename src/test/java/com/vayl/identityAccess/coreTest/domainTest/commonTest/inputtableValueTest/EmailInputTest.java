package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.EmailInput;
import org.junit.jupiter.api.Test;

public class EmailInputTest {

  @Test
  void constructor_withInvalidEmail_throwException() {
    try {
      new EmailInput("not-an-email");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_EMAIL_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_EMAIL_INPUT;
    }
  }

  @Test
  void constructor_withNullEmail_throwException() {
    try {
      new EmailInput(null);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_EMAIL_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_EMAIL_INPUT;
    }
  }

  @Test
  void constructor_withValidEmail_createsEmail() {
    String emailAddress = "user@example.com";
    EmailInput emailInput = new EmailInput(emailAddress);

    assert emailInput.email().equals(emailAddress)
        : "got " + emailInput.email() + " expected " + emailAddress;
  }
}
