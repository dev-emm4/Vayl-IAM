package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

public record IntegerInput(@NonNull Integer value) implements InputtableValue {

  public IntegerInput {
    AssertionConcern.isNotNull(value, ExceptionReason.INVALID_INTEGER);
  }
}
