package com.vayl.identityAccess.core.domain.role;

import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class RoleId {
  String id;

  public RoleId(String anId) {
    this.setId(anId);
  }

  private void setId(String anId) {
    this.throwErrorOnInvalidId(anId);
    this.id = anId;
  }

  private void throwErrorOnInvalidId(String anId) {
    if (!IdValidator.isValid(anId)) {
      throw new InvalidValueException("DEFAULT_ROLE_ID_CREATION", anId);
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
      RoleId typedObject = (RoleId) anObject;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
