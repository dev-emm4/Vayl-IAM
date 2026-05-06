package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.UsernameInput;
import org.junit.jupiter.api.Test;

public class UsernameInputTest {

  @Test
  void of_withBlank_throwException() {
    try {
      new UsernameInput("");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_USERNAME
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_USERNAME;
    }
  }

  @Test
  void constructor_withNull_throwException() {
    try {
      new UsernameInput(null);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_USERNAME
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_USERNAME;
    }
  }

  @Test
  void of_withValidUsername_createsUsername() {
    UsernameInput u = new UsernameInput("alice");

    assert u.username().equals("alice");
  }
}
