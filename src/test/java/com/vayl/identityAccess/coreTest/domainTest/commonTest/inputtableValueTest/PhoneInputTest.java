package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.PhoneInput;
import org.junit.jupiter.api.Test;

public class PhoneInputTest {

  @Test
  void constructor_withNullParameters_throwException() {
    String countryCode = "+1";
    String subscriberNumber = "2025550125";

    for (int i = 0; i < 2; i++) {
      try {
        if (i == 0) new PhoneInput(null, subscriberNumber);
        if (i == 1) new PhoneInput(countryCode, null);
        assert false : "Exception expected";
      } catch (InvalidValueException e) {
        assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
            : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
      }
    }
  }

  @Test
  void constructor_withInvalidCountryCode_throwsInvalidValueException() {
    try {
      new PhoneInput("1", "2025550125");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
    }
  }

  @Test
  void constructor_withInvalidSubscriber_throwsInvalidValueException() {
    try {
      new PhoneInput("+1", "1234567");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
    }
  }

  @Test
  void of_withValidPhone_createsPhone() {
    String countryCode = "+1";
    String subscriberNumber = "2025550125";

    PhoneInput p = new PhoneInput(countryCode, subscriberNumber);

    assert p.countryCode().equals(countryCode)
        : "got " + p.countryCode() + " expected " + countryCode;
    assert p.subscriberNumber().equals(subscriberNumber)
        : "got " + p.subscriberNumber() + " expected " + subscriberNumber;
  }
}
