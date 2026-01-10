package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionEvent;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionLevel;
import com.vayl.identityAccess.core.domain.common.DomainErrors.ExceptionReason;
import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class ApiId {
  private String id;

  public ApiId(String id) {
    this.setId(id);
  }

  private void setId(String id) {
    this.throwErrorOnInvalidId(id);
    this.id = id;
  }

  private void throwErrorOnInvalidId(String id) {
    UrlValidator urlValidator = new UrlValidator();
    if (!urlValidator.isValid(id)) {
      throw new InvalidValueException(
          ExceptionEvent.API_ID_CREATION, ExceptionReason.INVALID_ID, id, ExceptionLevel.INFO);
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
      ApiId typedObject = (ApiId) anObject;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
