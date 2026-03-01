package com.vayl.identityAccess.core.domain.common.inputtableValue;

import com.vayl.identityAccess.core.domain.common.AssertionConcern;
import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import org.jspecify.annotations.NonNull;

/** A small wrapper so plain Strings can be used where an {@link InputtableValue} is required. */
public record StringInput(@NonNull String string) implements InputtableValue {
  public StringInput {
    AssertionConcern.isNotNull(string, ExceptionReason.INVALID_STRING_INPUT);
  }
}
