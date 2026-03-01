package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.DateValidator;
import org.jspecify.annotations.NonNull;

public record DateInput(@NonNull String date) implements InputtableValue {
  public DateInput {
    this.throwErrorOnInvalidDate(date);
  }

  private void throwErrorOnInvalidDate(String date) {
    // This specifically looks for the YYYY-MM-DDTHH:mm:ssZ format
    AssertionConcern.isNotNull(date, ExceptionReason.INVALID_DATE_INPUT);
    AssertionConcern.isValid(new DateValidator(), date, ExceptionReason.INVALID_DATE_INPUT);
  }

  @Override
  public @NonNull String toString() {
    return this.date;
  }
}
