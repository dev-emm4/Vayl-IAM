package com.vayl.identityAccess.core.domain.api.role;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class RoleId {
  String id;

  public RoleId(String id) {
    this.setId(id);
  }

  private void setId(String id) {
    this.throwErrorOnInvalidId(id);
    this.id = id;
  }

  private void throwErrorOnInvalidId(String id) {
    if (!IdValidator.isValid(id)) {
      throw new InvalidValueException(ExceptionReason.INVALID_ROLE_ID, id);
    }
  }

  @Override
  public String toString() {
    return this.id;
  }

  @Override
  public boolean equals(Object object) {

    boolean isEqual = false;
    if (object != null && this.getClass() == object.getClass()) {
      RoleId typedObject = (RoleId) object;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
