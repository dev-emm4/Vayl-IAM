package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import org.jspecify.annotations.NonNull;

public class FieldConfigId {
  private String name;

  public FieldConfigId(String name) {
    this.setName(name);
  }

  public void setName(String name) {
    this.throwErrorIfNameIsBlank(name);
    this.name = name;
  }

  private void throwErrorIfNameIsBlank(@NonNull String name) {
    if (name.isBlank()) {
      throw new InvalidValueException(ExceptionReason.INVALID_FIELD_CONFIG_ID, name);
    }
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object anObject) {

    boolean isEqual = false;
    if (anObject != null && this.getClass() == anObject.getClass()) {
      FieldConfigId typedObject = (FieldConfigId) anObject;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }
}
