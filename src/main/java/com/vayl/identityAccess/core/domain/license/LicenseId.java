package com.vayl.identityAccess.core.domain.license;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;
import com.vayl.identityAccess.core.domain.common.IdValidator;

public class LicenseId {
  private String id;

  public LicenseId(String anId) {
    this.setId(anId);
  }

  public void setId(String anId) {
    this.throwErrorOnInvalidId(anId);
    this.id = anId;
  }

  private void throwErrorOnInvalidId(String anId) {
    if (!IdValidator.isValid(anId)) {
      throw new InvalidValueException(
          ExceptionEvent.SUBSCRIPTION_ID_CREATION,
          ExceptionReason.INVALID_ID,
          anId,
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
      LicenseId typedObject = (LicenseId) anObject;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
