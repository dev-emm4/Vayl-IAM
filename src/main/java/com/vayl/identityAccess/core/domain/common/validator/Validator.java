package com.vayl.identityAccess.core.domain.common.validator;

import org.jspecify.annotations.NonNull;

public interface Validator {
      boolean isValid(@NonNull String value);
}
