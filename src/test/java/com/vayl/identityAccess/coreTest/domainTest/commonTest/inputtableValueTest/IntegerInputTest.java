package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.IntegerInput;
import org.junit.jupiter.api.Test;

public class IntegerInputTest {

  @Test
  void constructor_withNull_throwException() {
    try {
      new IntegerInput(null);

      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_INTEGER
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_INTEGER;
    }
  }

  @Test
  void constructor_withValidInteger_createsIntegerInput() {
    IntegerInput i = new IntegerInput(42);

    assert i.value().equals(42);
  }
}
