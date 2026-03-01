package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.PhoneInput;
import org.junit.jupiter.api.Test;

public class PhoneInputTest {

  @Test
  void constructor_withMissingCountryCode_throwsInvalidValueException() {
    try {
      new PhoneInput(null, "1234567");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
    }
  }

  @Test
  void constructor_withMissingSubscriber_throwsInvalidValueException() {
    try {
      new PhoneInput("+1", null);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
    }
  }

  @Test
  void constructor_withInvalidCountryCode_throwsInvalidValueException() {
    try {
      new PhoneInput("1", "1234567");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
    }
  }

  @Test
  void constructor_withInvalidSubscriber_throwsInvalidValueException() {
    try {
      new PhoneInput("+1", "abcde");
      assert false : "Expected InvalidValueException was not thrown for invalid subscriber";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_PHONE_NUMBER_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_PHONE_NUMBER_INPUT;
    }
  }

  @Test
  void of_withValidPhone_createsPhone() {
    PhoneInput p = new PhoneInput("+1", "2025550125");

    assert p.countryCode().equals("+1") : "got " + p.countryCode() + " expected +1";
    assert p.subscriberNumber().equals("2025550125")
        : "got " + p.subscriberNumber() + " expected 2025550125";
  }
}
