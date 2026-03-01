package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.AddressInput;
import org.junit.jupiter.api.Test;

public class AddressInputTest {

  @Test
  void constructor_withMissingCountry_throwException() {
    try {
      new AddressInput("123 Main St", "CA", null);
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
    }
  }

  @Test
  void constructor_withMissingState_throwsInvalidValueException() {
    try {
      new AddressInput("123 Main St", null, "USA");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
    }
  }

  @Test
  void constructor_withMissingStreet_throwsInvalidValueException() {
    try {
      new AddressInput(null, "CA", "USA");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
    }
  }

  @Test
  void constructor_withBlankState_throwsInvalidValueException() {
    try {
      new AddressInput("123 Main St", " ", "USA");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
    }
  }

  @Test
  void constructor_withBlankCountry_throwsInvalidValueException() {
    try {
      new AddressInput("123 Main St", "CA", "   ");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
    }
  }

  @Test
  void constructor_withValidAddress_createsAddress() {
    AddressInput a = new AddressInput("123 Main St", "CA", "USA");

    assert a.street().equals("123 Main St") : "got " + a.street() + " expected 123 Main St";
    assert a.state().equals("CA") : "got " + a.state() + " expected CA";
    assert a.country().equals("USA") : "got " + a.country() + " expected USA";
  }
}
