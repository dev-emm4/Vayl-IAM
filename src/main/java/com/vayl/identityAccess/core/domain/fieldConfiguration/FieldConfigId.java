package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import org.jspecify.annotations.NonNull;

public class FieldConfigId {
  private String name;

  public FieldConfigId(String name) {
    this.setName(name);
  }

  public void setName(String name) {
    this.throwErrorOnInvalidId(name);
    this.name = name;
  }

  private void throwErrorOnInvalidId(@NonNull String name) {
    if (name.isBlank()) {
      throw new InvalidValueException(
          ExceptionEvent.FIELD_CONFIG_ID_CREATION,
          ExceptionReason.INVALID_ID,
          name,
          ExceptionLevel.INFO);
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
