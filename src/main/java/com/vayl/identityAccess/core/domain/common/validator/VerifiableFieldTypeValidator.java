package com.vayl.identityAccess.core.domain.common.validator;

import com.vayl.identityAccess.core.domain.fieldConfiguration.FieldType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;

public class VerifiableFieldTypeValidator implements Validator {
  private final Set<String> verifiableFieldType =
      new HashSet<>(
          List.of(
              FieldType.EMAIL.toString(),
              FieldType.PHONE.toString(),
              FieldType.PASSCODE.toString()));

  @Override
  public boolean isValid(@NonNull String value) {
    if (value == null) return false;
    return this.verifiableFieldType.contains(value);
  }
}
