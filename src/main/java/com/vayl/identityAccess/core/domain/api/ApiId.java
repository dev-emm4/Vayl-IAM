package com.vayl.identityAccess.core.domain.api;

import com.vayl.identityAccess.core.domain.common.DomainErrors.InvalidValueException;

public class ApiId {
  private String id;

  public ApiId(String anId) {
    this.setId(anId);
  }

  private void setId(String anId) {
    this.throwErrorOnInvalidId(anId);
    this.id = anId;
  }

  private void throwErrorOnInvalidId(String anId) {
    UrlValidator urlValidator = new UrlValidator();
    if (!urlValidator.isValid(anId)) {
      throw new InvalidValueException("API_ID_CREATION", anId);
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
