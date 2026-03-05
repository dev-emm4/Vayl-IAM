package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.DateInput;
import org.junit.jupiter.api.Test;

public class DateInputTest {
  @Test
  void constructor_withInvalidDate_throwException() {
    String invalidDate = "2023-13-01T00:00:00Z"; // Invalid month

    try {
      new DateInput(invalidDate);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_DATE_INPUT)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_DATE_INPUT;
    }
  }

  @Test
  void constructor_withNullDate_throwsInvalidValueException() {
    try {
      new DateInput(null);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason().equals(ExceptionReason.INVALID_DATE_INPUT)
          : "got: " + e.reason() + " expected: " + ExceptionReason.INVALID_DATE_INPUT;
    }
  }

  @Test
  void constructor_withValidDate_createsDate() {
    String validDate = "2023-12-01T00:00:00Z";
    DateInput dateInput = new DateInput(validDate);
    assert dateInput.date().equals(validDate)
        : "Date mismatch after creation got: " + dateInput.date() + " expected: " + validDate;
  }

  @Test
  void toString_returnsDateString() {
    String validDate = "2023-12-01T00:00:00Z";
    DateInput dateInput = new DateInput(validDate);

    assert dateInput.toString().equals(validDate)
        : "Date toString mismatch got: " + dateInput + " expected: " + validDate;
  }
}
