package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

public record AddressInput(@NonNull String street, @NonNull String state, @NonNull String country)
    implements InputtableValue {
  public AddressInput {
    this.throwErrorIfAddressIsInvalid(street, state, country);
  }

  private void throwErrorIfAddressIsInvalid(String street, String state, String country) {
    AssertionConcern.isNotNull(street, ExceptionReason.INVALID_ADDRESS);
    AssertionConcern.isNotNull(state, ExceptionReason.INVALID_ADDRESS);
    AssertionConcern.isNotBlank(state, ExceptionReason.INVALID_ADDRESS);
    AssertionConcern.isNotNull(country, ExceptionReason.INVALID_ADDRESS);
    AssertionConcern.isNotBlank(country, ExceptionReason.INVALID_ADDRESS);
  }
}
