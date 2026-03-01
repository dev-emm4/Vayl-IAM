package com.vayl.identityAccess.core.domain.common.validator;

import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public class UnverifiableFieldTypeValidator implements Validator {
  private final Set<String> unverifiableFieldType =
      new HashSet<>(
          List.of(
              FieldType.STRING.toString(),
              FieldType.ADDRESS.toString(),
              FieldType.DATE.toString(),
              FieldType.USERNAME.toString()));

  @Override
  public boolean isValid(@NonNull String value) {
    if (value == null) return false;
    return this.unverifiableFieldType.contains(value);
  }
}
