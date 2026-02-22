package com.vayl.identityAccess.core.domain.common.DomainErrors.inputViolation;

public class InvalidValue {
  private final String type;
  private final String value;

  public InvalidValue(String type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public String toString() {
    return "{" + this.type + ", " + this.value + "}";
  }

  @Override
  public boolean equals(Object object) {

    boolean isEqual = false;
    if (object != null && this.getClass() == object.getClass()) {
      InvalidValue typedObject = (InvalidValue) object;
      isEqual = typedObject.toString().equals(this.toString());
    }

    return isEqual;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }
}
