package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.validator.SimpleDateValidator;
import org.jspecify.annotations.NonNull;

public record DateInput(@NonNull String date) implements InputtableValue {
  public DateInput {
    this.throwErrorOnInvalidDate(date);
  }

  private void throwErrorOnInvalidDate(String date) {
    AssertionConcern.isNotNull(date, ExceptionReason.INVALID_DATE);
    AssertionConcern.isValid(new SimpleDateValidator(), date, ExceptionReason.INVALID_DATE);
  }

  @Override
  public @NonNull String toString() {
    return this.date;
  }
}
