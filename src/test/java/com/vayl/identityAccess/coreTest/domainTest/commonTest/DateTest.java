package com.vayl.identityAccess.coreTest.domainTest.commonTest;

import com.vayl.identityAccess.core.domain.common.Date;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import org.junit.jupiter.api.Test;

public class DateTest {
  @Test
  void constructor_withInvalidDate_throwsInvalidValueException() {
    String invalidDate = "2023-13-01T00:00:00Z"; // Invalid month

    try {
      new Date(invalidDate);
      assert false
          : "Expected InvalidValueException was not thrown for invalid date: " + invalidDate;
    } catch (InvalidValueException e) {
      assert e.event().equals(ExceptionEvent.DATE_CREATION)
          : "InvalidValueError event mismatch got: "
              + e.event()
              + " expected: "
              + ExceptionEvent.DATE_CREATION;

      assert e.reason().equals(ExceptionReason.INVALID_DATE_FORMAT)
              : "InvalidValueError reason mismatch got: "
              + e.reason()
              + " expected: "
              + ExceptionReason.INVALID_DATE_FORMAT;

      assert e.level().equals(ExceptionLevel.INFO)
              : "InvalidValueError level mismatch got: "
              + e.level()
              + " expected: "
              + ExceptionLevel.INFO;

      assert e.invalidValue().equals(invalidDate)
          : "InvalidValueError invalidValue mismatch got: "
              + e.invalidValue()
              + " expected: "
              + invalidDate;
    }
  }

  @Test
  void constructor_withValidDate_createsDate() {
    String validDate = "2023-12-01T00:00:00Z";
    Date date = new Date(validDate);
    assert date.toString().equals(validDate)
        : "Date mismatch after creation got: " + date.toString() + " expected: " + validDate;
  }

  @Test
  void equals_withSameDate_returnsTrue() {
    String validDate = "2023-12-01T00:00:00Z";
    Date date1 = new Date(validDate);
    Date date2 = new Date(validDate);

    assert date1.equals(date2);
  }

  @Test
  void equals_withDifferentDate_returnsFalse() {
    Date date1 = new Date("2023-12-01T00:00:00Z");
    Date date2 = new Date("2024-01-01T00:00:00Z");

    assert !date1.equals(date2) : "Dates with different values should not be equal";
  }

  @Test
  void toString_returnsDateString() {
    String validDate = "2023-12-01T00:00:00Z";
    Date date = new Date(validDate);

    assert date.toString().equals(validDate)
        : "Date toString mismatch got: " + date.toString() + " expected: " + validDate;
  }

  @Test
  void hashCode_withSameDate_returnsSameHashCode() {
    String validDate = "2023-12-01T00:00:00Z";
    Date date1 = new Date(validDate);
    Date date2 = new Date(validDate);

    assert date1.hashCode() == date2.hashCode() : "Hash codes for same dates should be equal";
  }
}
