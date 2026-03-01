package com.vayl.identityAccess.core.domain.common;

import com.vayl.identityAccess.core.domain.common.DomainException.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainException.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.validator.Validator;
import java.util.List;
import org.jspecify.annotations.NonNull;

public final class AssertionConcern {
  public static void isValid(
      @NonNull Validator validator, @NonNull String value, @NonNull ExceptionReason reason) {
    if (!validator.isValid(value)) {
      throw new InvalidValueException(reason);
    }
  }

  public static void isNotNull(Object object, @NonNull ExceptionReason reason) {
    if (object == null) {
      throw new InvalidValueException(reason);
    }
  }

  public static void isNotNull(List<Object> list, @NonNull ExceptionReason reason) {
    if (list == null) throw new InvalidValueException(reason);

    for (Object item : list) {
      if (item == null) throw new InvalidValueException(reason);
    }
  }

  public static void isTrue(boolean bool, @NonNull ExceptionReason reason) {
    if (!bool) {
      throw new InvalidValueException(reason);
    }
  }

  public static void isFalse(boolean bool, @NonNull ExceptionReason reason) {
    if (bool) {
      throw new InvalidValueException(reason);
    }
  }

  public static void isEqual(
      @NonNull Object firstValue, @NonNull Object secondValue, @NonNull ExceptionReason reason) {
    if (firstValue == null || secondValue == null) throw new InvalidValueException(reason);

    if (!firstValue.equals(secondValue)) {
      throw new InvalidValueException(reason);
    }
  }

  public static void isNotBlank(@NonNull String value, @NonNull ExceptionReason reason) {
    if (value.isBlank()) {
      throw new InvalidValueException(reason);
    }
  }
}
