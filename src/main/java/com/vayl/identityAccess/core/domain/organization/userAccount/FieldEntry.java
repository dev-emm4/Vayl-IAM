package com.vayl.identityAccess.core.domain.organization.userAccount;

import com.vayl.identityAccess.core.application.ExceptionEvent;
import com.vayl.identityAccess.core.application.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import org.jspecify.annotations.NonNull;

public class FieldEntry {
  String name;
  InputtableValue value;

  public FieldEntry(@NonNull String name, InputtableValue value) {
    this.setName(name);
    this.setValue(value);
  }

  private void setName(@NonNull String name) {
    this.throwErrorIfNameIsBlank(name);
    this.name = name;
  }

  private void throwErrorIfNameIsBlank(@NonNull String name){
    if (name.isBlank()){
      throw new InvalidValueException(
          ExceptionReason.BLANK_NAME_IN_FIELD_ENTRY,
          name);
    }
  }

  private void setValue(InputtableValue value) {
    this.value = value;
  }
}
