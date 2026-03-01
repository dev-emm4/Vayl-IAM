package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.PhoneValidator;
import org.jspecify.annotations.NonNull;

/**
 * @param countryCode e.g. +1, +233
 * @param subscriberNumber rest of number without country code
 */
public record PhoneInput(@NonNull String countryCode, @NonNull String subscriberNumber)
    implements InputtableValue {
  public PhoneInput {
    this.throwErrorIfPhoneInvalid(countryCode, subscriberNumber);
  }

  private void throwErrorIfPhoneInvalid(String countryCode, String subscriberNumber) {
    AssertionConcern.isNotNull(countryCode, ExceptionReason.INVALID_PHONE_NUMBER_INPUT);
    AssertionConcern.isNotNull(subscriberNumber, ExceptionReason.INVALID_PHONE_NUMBER_INPUT);
    AssertionConcern.isNotBlank(countryCode, ExceptionReason.INVALID_PHONE_NUMBER_INPUT);
    AssertionConcern.isNotBlank(subscriberNumber, ExceptionReason.INVALID_PHONE_NUMBER_INPUT);
    String normalized = countryCode.trim() + subscriberNumber.trim();
    AssertionConcern.isValid(
        new PhoneValidator(), normalized, ExceptionReason.INVALID_PHONE_NUMBER_INPUT);
  }
}
