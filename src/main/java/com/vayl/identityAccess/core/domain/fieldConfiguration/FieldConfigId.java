package com.vayl.identityAccess.core.domain.fieldConfiguration;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class FieldConfigId {
  private String id;

  public FieldConfigId(String id) {
    this.setId(id);
  }

  public void setId(String id) {
    this.throwErrorOnInvalidId(id);
    this.id = id;
  }

  private void throwErrorOnInvalidId(String id) {
    if (!IdValidator.isValid(id)) {
      throw new InvalidValueException(
          ExceptionEvent.FIELDCONFIG_ID_CREATION,
          ExceptionReason.INVALID_ID,
          id,
          ExceptionLevel.ERROR);
    }
  }

  @Override
  public String toString() {
    return this.id;
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
    return this.id.hashCode();
  }
}
