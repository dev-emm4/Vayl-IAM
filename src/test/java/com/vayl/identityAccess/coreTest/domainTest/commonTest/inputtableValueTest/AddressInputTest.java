package com.vayl.identityAccess.coreTest.domainTest.commonTest.inputtableValueTest;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.inputtableValue.AddressInput;
import org.junit.jupiter.api.Test;

public class AddressInputTest {

  @Test
  void constructor_withNullFields_throwException() {
    String street = "123 Main St";
    String state = "CA";
    String country = "USA";

    for (int i = 0; i < 3; i++) {
      try {
        if (i == 0) new AddressInput(null, state, country);
        if (i == 1) new AddressInput(street, null, country);
        if (i == 2) new AddressInput(street, state, null);
        assert false : "Exception expected ";
      } catch (InvalidValueException e) {
        assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
            : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
      }
    }
  }

  @Test
  void constructor_withBlankState_throwException() {
    try {
      new AddressInput("123 Main St", " ", "USA");
      assert false : "Exception expected";
    } catch (InvalidValueException e) {
      assert e.reason() == ExceptionReason.INVALID_ADDRESS_INPUT
          : "got " + e.reason() + " expected " + ExceptionReason.INVALID_ADDRESS_INPUT;
    }
  }

  @Test
  void constructor_withBlankCountry_throwException() {
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
    String street = "123 Main St";
    String state = "CA";
    String country = "USA";
    AddressInput address = new AddressInput(street, state, country);

    assert address.street().equals(street) : "got " + address.street() + " expected " + street;
    assert address.state().equals(state) : "got " + address.state() + " expected " + state;
    assert address.country().equals(country) : "got " + address.country() + " expected " + country;
  }
}
